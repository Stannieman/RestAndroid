package stannieman.rest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.fasterxml.jackson.databind.ObjectMapper;

import stannieman.commonservices.models.ServiceResult;
import stannieman.rest.models.ErrorResponseDataBase;
import stannieman.rest.models.RequestProperties;
import stannieman.rest.models.RestResult;

/**
 * A simple REST client.
 * No authentication is supported.
 */
public final class SimpleRestClient extends RestClientBase {

    SimpleRestClient(ObjectMapper objectMapper, RequestQueue requestQueue, Scheme schema, String host, int port, String apiBasePath, String endpointPath, long timeout) {
        super(objectMapper, requestQueue, schema, host, port, apiBasePath, endpointPath, timeout);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> get(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doSimpleRequest(Request.Method.GET, requestProperties);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> post(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doSimpleRequest(Request.Method.POST, requestProperties);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> put(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doSimpleRequest(Request.Method.PUT, requestProperties);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> patch(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doSimpleRequest(Request.Method.PATCH, requestProperties);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void getAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        doRequestAsync(Request.Method.GET, requestProperties, requestResponseListener);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void postAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        doRequestAsync(Request.Method.POST, requestProperties, requestResponseListener);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void putAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        doRequestAsync(Request.Method.PUT, requestProperties, requestResponseListener);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void patchAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        doRequestAsync(Request.Method.PATCH, requestProperties, requestResponseListener);
    }

    private <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void doRequestAsync(int method, RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        new DoRequestAsyncTask<>(method, requestProperties, requestResponseListener).execute();
    }

    private <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> doSimpleRequest(int method, RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doRequest(method, requestProperties, requestProperties.getQueryParameters(), requestProperties.getHeaders());
    }

    private class DoRequestAsyncTask<SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> extends DoRequestAsyncTaskBase<SuccessResponseDataType, ErrorResponseDataType> {
        DoRequestAsyncTask(int method, RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
            super(method, requestProperties, requestResponseListener);
        }

        @Override
        protected ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> doInBackground(Void... voids) {
            return doSimpleRequest(method, requestProperties);
        }
    }
}
