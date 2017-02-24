package stannieman.rest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import stannieman.commonservices.models.IHasDataAndSuccessState;
import stannieman.rest.models.ErrorResponseDataBase;
import stannieman.rest.models.RequestProperties;
import stannieman.rest.models.RestResult;

/**
 * A simple REST client that supports authentication with an API key.
 * Authentication is done by passing an API key as query parameter in the request URI.
 */
public final class KeyAuthRestClient extends RestClientBase {
    private final AbstractMap.SimpleEntry<String, String> keyQueryParameter;

    KeyAuthRestClient(ObjectMapper objectMapper, RequestQueue requestQueue, Scheme schema, String host, int port, String apiBasePath, String endpointPath, long timeout, String keyParameterName, String key) {
        super(objectMapper, requestQueue, schema, host, port, apiBasePath, endpointPath, timeout);
        keyQueryParameter = new AbstractMap.SimpleEntry<>(keyParameterName, key);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> get(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doRequestWithKeyParameter(Request.Method.GET, requestProperties);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> post(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doRequestWithKeyParameter(Request.Method.POST, requestProperties);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> put(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doRequestWithKeyParameter(Request.Method.PUT, requestProperties);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> patch(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doRequestWithKeyParameter(Request.Method.PATCH, requestProperties);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void getAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        doRequestWithKeyParameterAsync(Request.Method.GET, requestProperties, requestResponseListener);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void postAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        doRequestWithKeyParameterAsync(Request.Method.POST, requestProperties, requestResponseListener);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void putAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        doRequestWithKeyParameterAsync(Request.Method.PUT, requestProperties, requestResponseListener);
    }

    @Override
    public <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void patchAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        doRequestWithKeyParameterAsync(Request.Method.PATCH, requestProperties, requestResponseListener);
    }

    private <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> doRequestWithKeyParameter(int method, RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties) {
        return doRequest(method, requestProperties, getQueryParametersWithKey(requestProperties.getQueryParameters()), requestProperties.getHeaders());
    }

    private <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void doRequestWithKeyParameterAsync(int method, RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        new DoRequestWithKeyParameterAsyncTask<>(method, requestProperties, requestResponseListener).execute();
    }

    private class DoRequestWithKeyParameterAsyncTask<SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> extends DoRequestAsyncTaskBase<SuccessResponseDataType, ErrorResponseDataType> {
        DoRequestWithKeyParameterAsyncTask(int method, RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
            super(method, requestProperties, requestResponseListener);
        }

        @Override
        protected IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> doInBackground(Void... voids) {
            return doRequestWithKeyParameter(method, requestProperties);
        }
    }

    private List<AbstractMap.SimpleEntry<String, String>> getQueryParametersWithKey(List<AbstractMap.SimpleEntry<String, String>> queryParameters) {
        List<AbstractMap.SimpleEntry<String, String>> queryParametersWithKey = new ArrayList<>();
        queryParametersWithKey.add(keyQueryParameter);
        if (queryParameters != null) {
            queryParametersWithKey.addAll(queryParameters);
        }
        return queryParametersWithKey;
    }
}
