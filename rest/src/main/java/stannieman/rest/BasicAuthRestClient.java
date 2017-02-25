package stannieman.rest;

import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import stannieman.commonservices.models.*;
import stannieman.rest.models.ErrorResponseDataBase;
import stannieman.rest.models.RequestProperties;
import stannieman.rest.models.RestResult;

/**
 * A simple REST client that supports basic authentication.
 * Authentication is done by setting a basic authentication header in the request.
 */
public final class BasicAuthRestClient extends RestClientBase {
    private static final String AuthHeaderKey = "Authorization";
    private static final String BasicAuthHeaderValuePrefix = "Basic ";
    private final Map<String, String> authHeader;

    BasicAuthRestClient(ObjectMapper objectMapper, RequestQueue requestQueue, Scheme schema, String host, int port, String apiBasePath, String endpointPath, long timeout, String username, String password) {
        super(objectMapper, requestQueue, schema, host, port, apiBasePath, endpointPath, timeout);
        authHeader = getBasicAuthHeader(username, password);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> get(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doRequestWithAuthHeader(Request.Method.GET, requestProperties);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> post(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doRequestWithAuthHeader(Request.Method.POST, requestProperties);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> put(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doRequestWithAuthHeader(Request.Method.PUT, requestProperties);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> patch(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doRequestWithAuthHeader(Request.Method.PATCH, requestProperties);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void getAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        doRequestWithAuthHeaderAsync(Request.Method.GET, requestProperties, requestResponseListener);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void postAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        doRequestWithAuthHeaderAsync(Request.Method.POST, requestProperties, requestResponseListener);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void putAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        doRequestWithAuthHeaderAsync(Request.Method.PUT, requestProperties, requestResponseListener);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void patchAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        doRequestWithAuthHeaderAsync(Request.Method.PATCH, requestProperties, requestResponseListener);
    }

    private <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> doRequestWithAuthHeader(int method, RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doRequest(method, requestProperties, requestProperties.getQueryParameters(), getHeadersWithAuthHeader(requestProperties.getHeaders()));
    }

    private <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void doRequestWithAuthHeaderAsync(int method, RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        new DoRequestWithAuthHeaderAsyncTask<>(method, requestProperties, requestResponseListener).execute();
    }

    private class DoRequestWithAuthHeaderAsyncTask<SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> extends DoRequestAsyncTaskBase<SuccessResponseDataType, ErrorResponseDataType> {
        DoRequestWithAuthHeaderAsyncTask(int method, RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
            super(method, requestProperties, requestResponseListener);
        }

        @Override
        protected IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> doInBackground(Void... voids) {
            return doRequestWithAuthHeader(method, requestProperties);
        }
    }

    private Map<String, String> getBasicAuthHeader(String username, String password) {
        Map<String, String> basicAuthHeader = new HashMap<>();
        basicAuthHeader.put(AuthHeaderKey, BasicAuthHeaderValuePrefix + Base64.encodeToString(String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP));
        return basicAuthHeader;
    }

    private Map<String, String> getHeadersWithAuthHeader(Map<String, String> headers) {
        Map<String, String> headersWithAuthHeader = new HashMap<>(authHeader);

        if (headers != null) {
            headersWithAuthHeader.putAll(headers);
        }

        return headersWithAuthHeader;
    }
}
