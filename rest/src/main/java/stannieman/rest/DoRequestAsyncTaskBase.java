package stannieman.rest;

import android.os.AsyncTask;

import stannieman.commonservices.models.IHasDataAndSuccessState;
import stannieman.rest.models.ErrorResponseDataBase;
import stannieman.rest.models.RequestProperties;
import stannieman.rest.models.RestResult;

abstract class DoRequestAsyncTaskBase<SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> extends AsyncTask<Void, Void, IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>>> {
    final int method;
    private final IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener;
    final RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties;

    DoRequestAsyncTaskBase(int method, RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener) {
        this.method = method;
        this.requestResponseListener = requestResponseListener;
        this.requestProperties = requestProperties;
    }

    @Override
    protected void onPostExecute(IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> result) {
        requestResponseListener.onRequestResponse(result);
    }
}
