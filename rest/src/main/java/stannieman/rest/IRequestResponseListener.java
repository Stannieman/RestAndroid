package stannieman.rest;

import stannieman.commonservices.models.IHasDataAndSuccessState;
import stannieman.rest.models.ErrorResponseDataBase;
import stannieman.rest.models.RestResult;

/**
 * Interface defining a listener for the response of a REST call.
 * @param <SuccessResponseDataType> response data type for a successful request
 * @param <ErrorResponseDataType> response data type for an unsuccessful request
 */
public interface IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> {
    /**
     * Called after an asynchronous REST call is finishes.
     * @param result IHasDataAndSuccessState instance containing a RestResult instance which in turn contains the resulting info and data of the performed REST call
     */
    void onRequestResponse(IHasDataAndSuccessState<RestResult<SuccessResponseDataType, ErrorResponseDataType>> result);
}
