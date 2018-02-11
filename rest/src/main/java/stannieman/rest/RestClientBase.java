package stannieman.rest;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
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
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import stannieman.commonservices.helpers.ResultCodeHelper;
import stannieman.commonservices.models.DataServiceResult;
import stannieman.commonservices.models.GeneralResultCodes;
import stannieman.commonservices.models.IHasDataAndSuccessState;
import stannieman.commonservices.models.ServiceResult;
import stannieman.rest.helpers.ArrayHelper;
import stannieman.rest.helpers.QueryParamsHelper;
import stannieman.rest.models.ErrorResponseDataBase;
import stannieman.rest.models.RequestProperties;
import stannieman.rest.models.RestResult;

import static stannieman.rest.RestClientResultCodes.CANNOT_CREATE_URI;

abstract class RestClientBase implements IRestClient {
    private static final String ENCODING = "UTF-8";
    private static final int DEFAULT_SUCCESS_STATUS_CODES_MIN = 200;
    private static final int DEFAULT_SUCCESS_STATUS_CODES_MAX = 299;

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
    private final RetryPolicy retryPolicy;

    protected RestClientBase(ObjectMapper objectMapper, RequestQueue requestQueue, Scheme scheme, String host, int port, String apiBasePath, String endpointPath, long timeout, RetryPolicy retryPolicy) {
        this.objectMapper = objectMapper;
        this.requestQueue = requestQueue;

        this.scheme = scheme.name().toLowerCase(Locale.US);
        this.host = stripSlashes(host);
        this.port = port;
        this.absoluteEndpointPath = getAbsoluteEndpointPath(apiBasePath, endpointPath);
        this.timeout = timeout;
        this.retryPolicy = retryPolicy;
    }

    protected <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> doRequest(int method, RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, List<AbstractMap.SimpleEntry<String, String>> queryParameters, Map<String, String> headers) {
        IHasDataAndSuccessState<String> uriResult = getUriString(requestProperties.getSubPath(), requestProperties.getSubPathParameters(), queryParameters);
        if (!uriResult.isSuccess()) {
            RestClientResultCodes resultCode = ResultCodeHelper.GetResultCodeOrNull(uriResult, RestClientResultCodes.class);
            return resultCode != null
                    ? new DataServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>, RestClientResultCodes>(resultCode)
                    : new DataServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>, RestClientResultCodes>(RestClientResultCodes.CANNOT_CREATE_URI);
        }
        String uriString = uriResult.getData();

        IHasDataAndSuccessState<String> bodyStringResult = getBodyString(requestProperties.getBody());
        if (!bodyStringResult.isSuccess()) {
            RestClientResultCodes resultCode = ResultCodeHelper.GetResultCodeOrNull(uriResult, RestClientResultCodes.class);
            return resultCode != null
                    ? new DataServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>, RestClientResultCodes>(resultCode)
                    : new DataServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>, RestClientResultCodes>(RestClientResultCodes.CANNOT_CREATE_JSON_STRING_FROM_OBJECT);
        }
        String bodyString = bodyStringResult.getData();

        IHasDataAndSuccessState<NetworkResponse> networkResponseResult = getNetworkResponse(method, uriString, headers, bodyString);
        if (!networkResponseResult.isSuccess()) {
            RestClientResultCodes resultCode = ResultCodeHelper.GetResultCodeOrNull(uriResult, RestClientResultCodes.class);
            return resultCode != null
                    ? new DataServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>, RestClientResultCodes>(resultCode)
                    : new DataServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>, RestClientResultCodes>(RestClientResultCodes.REQUEST_FAILED);
        }
        NetworkResponse networkResponse = networkResponseResult.getData();

        return createRestResultFromNetworkResponse(requestProperties.getSuccessResponseDataType(), requestProperties.getErrorResponseDataType(), networkResponse, requestProperties.getSuccessStatusCodes());
    }

    private IHasDataAndSuccessState<String> getUriString(String subPath, String[] subPathParameters, List<AbstractMap.SimpleEntry<String, String>> queryParameters) {
        String parametrizedSubPath = getParametrizedSubPath(subPath, subPathParameters);

        try {
            String uriString = new URI(scheme, null, host, port, absoluteEndpointPath + parametrizedSubPath, null, null).toString();
            String queryString = QueryParamsHelper.getQueryString(queryParameters, ENCODING);
            return new DataServiceResult<>(uriString + queryString, GeneralResultCodes.OK);
        } catch (Exception e) {
            return new DataServiceResult<>(CANNOT_CREATE_URI);
        }
    }

    private IHasDataAndSuccessState<String> getBodyString(Object body) {
        if (body != null) {
            try {
                return new DataServiceResult<>(objectMapper.writeValueAsString(body), GeneralResultCodes.OK);
            } catch (Exception e) {
                return new DataServiceResult<>(RestClientResultCodes.CANNOT_CREATE_JSON_STRING_FROM_OBJECT);
            }
        }
        return new DataServiceResult<>();
    }

    private IHasDataAndSuccessState<NetworkResponse> getNetworkResponse(int method, String uriString, Map<String, String> headers, String bodyString) {
        RequestFuture<NetworkResponse> future = RequestFuture.newFuture();
        requestQueue.add(new NetworkResponseRequest(
                method,
                uriString,
                getHeadersWithRequestDefaultHeaders(headers),
                bodyString,
                retryPolicy,
                future,
                future,
                ENCODING));

        NetworkResponse response;
        try {
            response = future.get(timeout, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e) {
            return new DataServiceResult<>(RestClientResultCodes.REQUEST_INTERRUPTED);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (!(cause instanceof VolleyError)) {
                return new DataServiceResult<>(RestClientResultCodes.REQUEST_FAILED);
            }
            VolleyError error = (VolleyError) cause;
            if (error.networkResponse == null) {
                return new DataServiceResult<>(RestClientResultCodes.REQUEST_FAILED);
            }
            response = error.networkResponse;
        } catch (TimeoutException e) {
            return new DataServiceResult<>(RestClientResultCodes.REQUEST_TIMED_OUT);
        }

        return new DataServiceResult<>(response, GeneralResultCodes.OK);
    }

    private <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> createRestResultFromNetworkResponse(Class<SuccessResponseDataType> successResponseDataType, Class<ErrorResponseDataType> errorResponseDataType, NetworkResponse networkResponse, Integer[] successStatusCodes) {
        String jsonString = NetworkResponseRequest.parseToString(networkResponse);

        if (isStatusCodeOk(networkResponse.statusCode, successStatusCodes)) {
            if (successResponseDataType == null) {
                return new DataServiceResult<>(new RestResult<SuccessResponseDataType, ErrorResponseDataType>(networkResponse.statusCode), GeneralResultCodes.OK);
            }
            else {
                try {
                    SuccessResponseDataType successObject = objectMapper.readValue(jsonString, successResponseDataType);
                    return new DataServiceResult<>(new RestResult<SuccessResponseDataType, ErrorResponseDataType>(networkResponse.statusCode, successObject), GeneralResultCodes.OK);
                } catch (JsonMappingException e) {
                    return new DataServiceResult<>(RestClientResultCodes.JSON_RESPONSE_DATA_TYPE_MISMATCH);
                } catch (JsonParseException e) {
                    return new DataServiceResult<>(RestClientResultCodes.RESPONSE_IS_NOT_VALID_JSON);
                } catch (IOException e) {
                    return new DataServiceResult<>(RestClientResultCodes.CANNOT_CREATE_OBJECT_FROM_SUCCESS_RESPONSE);
                }
            }
        }

        if (errorResponseDataType == null) {
            return new DataServiceResult<>(new RestResult<SuccessResponseDataType, ErrorResponseDataType>(false, networkResponse.statusCode));
        }
        else {
            try {
                ErrorResponseDataType errorObject = objectMapper.readValue(jsonString, errorResponseDataType);
                return new DataServiceResult<>(new RestResult<SuccessResponseDataType, ErrorResponseDataType>(networkResponse.statusCode, errorObject), GeneralResultCodes.OK);
            } catch (JsonMappingException e) {
                return new DataServiceResult<>(RestClientResultCodes.JSON_ERROR_DATA_TYPE_MISMATCH);
            } catch (JsonParseException e) {
                return new DataServiceResult<>(RestClientResultCodes.RESPONSE_IS_NOT_VALID_JSON);
            } catch (IOException e) {
                return new DataServiceResult<>(RestClientResultCodes.CANNOT_CREATE_OBJECT_FROM_ERROR_RESPONSE);
            }
        }
    }

    private boolean isStatusCodeOk(int statusCode, Integer[] successStatusCodes) {
        if (successStatusCodes == null) {
            return statusCode >= DEFAULT_SUCCESS_STATUS_CODES_MIN && statusCode <= DEFAULT_SUCCESS_STATUS_CODES_MAX;
        }

        return ArrayHelper.Contains(successStatusCodes, statusCode);
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

    private String getParametrizedSubPath(String subPath, String[] subPathParameters) {
        if (subPath == null) {
            return "";
        }
        String parametrizedSubPath = stripSlashes(subPath);
        if (parametrizedSubPath.equals("")) {
            return "";
        }
        parametrizedSubPath = "/" + parametrizedSubPath;

        if (subPathParameters == null) {
            return parametrizedSubPath;
        }

        return String.format(parametrizedSubPath, (Object[])subPathParameters);
    }

    private String appendPathIfNeeded(String destination, String path) {
        if (path != null) {
            String strippedPath = stripSlashes(path);
            destination += "/" + strippedPath;
        }

        return destination;
    }
}
