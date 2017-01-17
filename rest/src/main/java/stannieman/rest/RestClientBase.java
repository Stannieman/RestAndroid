package stannieman.rest;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import stannieman.commonservices.models.ServiceResult;
import stannieman.rest.helpers.ArrayHelper;
import stannieman.rest.helpers.QueryParamsHelper;
import stannieman.rest.models.ErrorResponseDataBase;
import stannieman.rest.models.RequestProperties;
import stannieman.rest.models.RestResult;
import stannieman.rest.models.SuccessResponseDataBase;

abstract class RestClientBase implements IRestClient {
    public static final int REQUEST_TIMED_OUT = 1;
    public static final int REQUEST_FAILED = 2;
    public static final int REQUEST_INTERRUPTED = 3;
    public static final int RESPONSE_IS_NOT_VALID_JSON = 4;
    public static final int JSON_RESPONSE_DATA_TYPE_MISMATCH = 5;
    public static final int JSON_ERROR_DATA_TYPE_MISMATCH = 6;
    public static final int CANNOT_CREATE_OBJECT_FROM_SUCCESS_RESPONSE = 7;
    public static final int CANNOT_CREATE_OBJECT_FROM_ERROR_RESPONSE = 8;
    public static final int CANNOT_CREATE_JSON_STRING_FROM_OBJECT = 9;
    public static final int CANNOT_CREATE_URI = 10;

    private static final String ENCODING = "UTF-8";

    private static final Map<String, String> REQUEST_DEFAULT_HEADERS;
    static {
        Map<String, String> requestDefaultHeaders = new HashMap<>();
        requestDefaultHeaders.put("Accept-Type", "application/json; charset=" + ENCODING);
        REQUEST_DEFAULT_HEADERS = Collections.unmodifiableMap(requestDefaultHeaders);
    }

    private final ObjectMapper objectMapper;
    private final RequestQueue requestQueue;

    private final String scheme;
    private final String host;
    private final int port;
    private final String absoluteEndpointPath;
    private final long timeout;

    protected RestClientBase(ObjectMapper objectMapper, RequestQueue requestQueue, Scheme scheme, String host, int port, String apiBasePath, String endpointPath, long timeout) {
        this.objectMapper = objectMapper;
        this.requestQueue = requestQueue;

        this.scheme = scheme.name().toLowerCase();
        this.host = stripSlashes(host);
        this.port = port;
        this.absoluteEndpointPath = getAbsoluteEndpointPath(apiBasePath, endpointPath);
        this.timeout = timeout;
    }

    protected <SuccessResponseDataType extends SuccessResponseDataBase, ErrorResponseDataType extends ErrorResponseDataBase> ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> doRequest(int method, RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, List<AbstractMap.SimpleEntry<String, String>> queryParameters, Map<String, String> headers) {
        ServiceResult<String> uriResult = getUriString(requestProperties.getSubPath(), requestProperties.getSubPathParameters(), queryParameters);
        if (!uriResult.isSuccess()) {
            return new ServiceResult<>(false, uriResult.getResultCode());
        }
        String uriString = uriResult.getData();

        ServiceResult<String> bodyStringResult = getBodyString(requestProperties.getBody());
        if (!bodyStringResult.isSuccess()) {
            return new ServiceResult<>(false, bodyStringResult.getResultCode());
        }
        String bodyString = bodyStringResult.getData();

        ServiceResult<NetworkResponse> networkResponseResult = getNetworkResponse(method, uriString, headers, bodyString);
        if (!networkResponseResult.isSuccess()) {
            return new ServiceResult<>(false, networkResponseResult.getResultCode());
        }
        NetworkResponse networkResponse = networkResponseResult.getData();

        return createRestResultFromNetworkResponse(requestProperties.getSuccessResponseDataType(), requestProperties.getErrorResponseDataType(), networkResponse, requestProperties.getSuccessStatusCodes());
    }

    private ServiceResult<String> getUriString(String subPath, List<String> subPathParameters, List<AbstractMap.SimpleEntry<String, String>> queryParameters) {
        String parametrizedSubPath = getParametrizedSubPath(subPath, subPathParameters);

        try {
            String uriString = new URI(scheme, null, host, port, absoluteEndpointPath + parametrizedSubPath, null, null).toString();
            String queryString = QueryParamsHelper.getQueryString(queryParameters, ENCODING);
            return new ServiceResult<>(uriString + queryString);
        } catch (Exception e) {
            return new ServiceResult<>(false, CANNOT_CREATE_URI);
        }
    }

    private ServiceResult<String> getBodyString(Object body) {
        if (body != null) {
            try {
                return new ServiceResult<>(objectMapper.writeValueAsString(body));
            } catch (Exception e) {
                return new ServiceResult<>(false, CANNOT_CREATE_JSON_STRING_FROM_OBJECT);
            }
        }
        return new ServiceResult<>();
    }

    private ServiceResult<NetworkResponse> getNetworkResponse(int method, String uriString, Map<String, String> headers, String bodyString) {
        RequestFuture<NetworkResponse> future = RequestFuture.newFuture();
        requestQueue.add(new NetworkResponseRequest(method, uriString, getHeadersWithRequestDefaultHeaders(headers), bodyString, future, future, ENCODING));

        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        NetworkResponse response;
        try {
            response = future.get(timeout, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e) {
            return new ServiceResult<>(false, REQUEST_INTERRUPTED);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (!(cause instanceof VolleyError)) {
                return new ServiceResult<>(false, REQUEST_FAILED);
            }
            VolleyError error = (VolleyError) cause;
            if (error.networkResponse == null) {
                return new ServiceResult<>(false, REQUEST_FAILED);
            }
            response = error.networkResponse;
        } catch (TimeoutException e) {
            return new ServiceResult<>(false, REQUEST_TIMED_OUT);
        }

        return new ServiceResult<>(response);
    }

    private <SuccessResponseDataType extends SuccessResponseDataBase, ErrorResponseDataType extends ErrorResponseDataBase> ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> createRestResultFromNetworkResponse(Class<SuccessResponseDataType> successResponseDataType, Class<ErrorResponseDataType> errorResponseDataType, NetworkResponse networkResponse, Integer[] successStatusCodes) {
        String jsonString = NetworkResponseRequest.parseToString(networkResponse);

        if (successStatusCodes != null && ArrayHelper.Contains(successStatusCodes, networkResponse.statusCode)) {
            if (successResponseDataType == null) {
                return new ServiceResult<>(new RestResult<SuccessResponseDataType, ErrorResponseDataType>(networkResponse.statusCode));
            }
            else {
                try {
                    SuccessResponseDataType successObject = objectMapper.readValue(jsonString, successResponseDataType);
                    return new ServiceResult<>(new RestResult<SuccessResponseDataType, ErrorResponseDataType>(networkResponse.statusCode, successObject));
                } catch (JsonMappingException e) {
                    return new ServiceResult<>(false, JSON_RESPONSE_DATA_TYPE_MISMATCH);
                } catch (JsonParseException e) {
                    return new ServiceResult<>(false, RESPONSE_IS_NOT_VALID_JSON);
                } catch (IOException e) {
                    return new ServiceResult<>(false, CANNOT_CREATE_OBJECT_FROM_SUCCESS_RESPONSE);
                }
            }
        }

        if (errorResponseDataType == null) {
            return new ServiceResult<>(new RestResult<SuccessResponseDataType, ErrorResponseDataType>(false, networkResponse.statusCode));
        }
        else {
            try {
                ErrorResponseDataType errorObject = objectMapper.readValue(jsonString, errorResponseDataType);
                return new ServiceResult<>(new RestResult<SuccessResponseDataType, ErrorResponseDataType>(networkResponse.statusCode, errorObject));
            } catch (JsonMappingException e) {
                return new ServiceResult<>(false, JSON_ERROR_DATA_TYPE_MISMATCH);
            } catch (JsonParseException e) {
                return new ServiceResult<>(false, RESPONSE_IS_NOT_VALID_JSON);
            } catch (IOException e) {
                return new ServiceResult<>(false, CANNOT_CREATE_OBJECT_FROM_ERROR_RESPONSE);
            }
        }
    }
    
    private String stripSlashes(String string) {
        int startIndex = 0;
        int endIndex = 0;

        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != '/') {
                startIndex = i;
                break;
            }
        }
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] != '/') {
                endIndex = i + 1;
                break;
            }
        }

        return string.substring(startIndex, endIndex);
    }

    private Map<String, String> getHeadersWithRequestDefaultHeaders(Map<String, String> headers) {
        Map<String, String> headersWithRequestDefaultHeaders = new HashMap<>(REQUEST_DEFAULT_HEADERS);

        if (headers != null) {
            headersWithRequestDefaultHeaders.putAll(headers);
        }

        return headersWithRequestDefaultHeaders;
    }

    private String getAbsoluteEndpointPath(String apiBasePath, String endpointPath) {
        return appendPathIfNeeded(appendPathIfNeeded("", apiBasePath), endpointPath);
    }

    private String getParametrizedSubPath(String subPath, List<String> subPathParameters) {
        if (subPath == null) {
            return "";
        }
        String parametrizedSubPath = stripSlashes(subPath);
        if (parametrizedSubPath == "") {
            return "";
        }
        parametrizedSubPath = "/" + parametrizedSubPath;

        if (subPathParameters == null) {
            return parametrizedSubPath;
        }

        return String.format(parametrizedSubPath, subPathParameters.toArray());
    }

    private String appendPathIfNeeded(String destination, String path) {
        if (path != null) {
            String strippedPath = stripSlashes(path);
            if (strippedPath != null) {
                destination += "/" + strippedPath;
            }
        }

        return destination;
    }
}
