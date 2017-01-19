package stannieman.rest;

import stannieman.commonservices.models.ServiceResult;
import stannieman.rest.models.ErrorResponseDataBase;
import stannieman.rest.models.SuccessResponseDataBase;
import stannieman.rest.models.RestResult;

/**
 * Interface defining a listener for the response of a REST call.
 * @param <SuccessResponseDataType> response data type for a successful request
 * @param <ErrorResponseDataType> response data type for an unsuccessful request
 */
public interface IRequestResponseListener<SuccessResponseDataType extends SuccessResponseDataBase, ErrorResponseDataType extends ErrorResponseDataBase> {
    /**
     * Called after an asynchronous REST call is finishes.
     * @param result ServiceResult instance containing a RestResult instance which in turn contains the resulting info and data of the performed REST call
     */
    void onRequestResponse(ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> result);
}
