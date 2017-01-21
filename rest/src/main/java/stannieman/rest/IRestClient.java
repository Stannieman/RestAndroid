package stannieman.rest;

import stannieman.commonservices.models.ServiceResult;
import stannieman.rest.models.ErrorResponseDataBase;
import stannieman.rest.models.RequestProperties;
import stannieman.rest.models.RestResult;

/**
 * Interface defining a REST client.
 */
public interface IRestClient {
    /**
     * Does a synchronous get request.
     * @param requestProperties info and data required for performing the REST call.
     * @param <SuccessResponseDataType> response data type for a successful call
     * @param <ErrorResponseDataType> response data type for an unsuccessful call
     * @return ServiceResult instance containing a RestResult instance which in turn contains the resulting info and data of the performed REST call
     */
    <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> get(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties);

    /**
     * Does a synchronous post request.
     * @param requestProperties info and data required for performing the REST call.
     * @param <SuccessResponseDataType> response data type for a successful call
     * @param <ErrorResponseDataType> response data type for an unsuccessful call
     * @return ServiceResult instance containing a RestResult instance which in turn contains the resulting info and data of the performed REST call
     */
    <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> post(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties);

    /**
     * Does a synchronous put request.
     * @param requestProperties info and data required for performing the REST call.
     * @param <SuccessResponseDataType> response data type for a successful call
     * @param <ErrorResponseDataType> response data type for an unsuccessful call
     * @return ServiceResult instance containing a RestResult instance which in turn contains the resulting info and data of the performed REST call
     */
    <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> put(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties);

    /**
     * Does a synchronous patch request.
     * @param requestProperties info and data required for performing the REST call.
     * @param <SuccessResponseDataType> response data type for a successful call
     * @param <ErrorResponseDataType> response data type for an unsuccessful call
     * @return ServiceResult instance containing a RestResult instance which in turn contains the resulting info and data of the performed REST call
     */
    <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> ServiceResult<RestResult<SuccessResponseDataType, ErrorResponseDataType>> patch(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties);

    /**
     * Does an asynchronous get request.
     * @param requestProperties info and data required for performing the REST call.
     * @param requestResponseListener listener of which the onRequestResponse(ServiceResult<RestResult<ResponseDataType, ErrorDataType>> result) method is called after the call is performed.
     * @param <SuccessResponseDataType> response data type for a successful call
     * @param <ErrorResponseDataType> response data type for an unsuccessful call
     * @return ServiceResult instance containing a RestResult instance which in turn contains the resulting info and data of the performed REST call
     */
    <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void getAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener);

    /**
     * Does an asynchronous post request.
     * @param requestProperties info and data required for performing the REST call.
     * @param requestResponseListener listener of which the onRequestResponse(ServiceResult<RestResult<ResponseDataType, ErrorDataType>> result) method is called after the call is performed.
     * @param <SuccessResponseDataType> response data type for a successful call
     * @param <ErrorResponseDataType> response data type for an unsuccessful call
     * @return ServiceResult instance containing a RestResult instance which in turn contains the resulting info and data of the performed REST call
     */
    <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void postAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener);

    /**
     * Does an asynchronous put request.
     * @param requestProperties info and data required for performing the REST call.
     * @param requestResponseListener listener of which the onRequestResponse(ServiceResult<RestResult<ResponseDataType, ErrorDataType>> result) method is called after the call is performed.
     * @param <SuccessResponseDataType> response data type for a successful call
     * @param <ErrorResponseDataType> response data type for an unsuccessful call
     * @return ServiceResult instance containing a RestResult instance which in turn contains the resulting info and data of the performed REST call
     */
    <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void putAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener);

    /**
     * Does an asynchronous patch request.
     * @param requestProperties info and data required for performing the REST call.
     * @param requestResponseListener listener of which the onRequestResponse(ServiceResult<RestResult<ResponseDataType, ErrorDataType>> result) method is called after the call is performed.
     * @param <SuccessResponseDataType> response data type for a successful call
     * @param <ErrorResponseDataType> response data type for an unsuccessful call
     * @return ServiceResult instance containing a RestResult instance which in turn contains the resulting info and data of the performed REST call
     */
    <SuccessResponseDataType, ErrorResponseDataType extends ErrorResponseDataBase> void patchAsync(RequestProperties<SuccessResponseDataType, ErrorResponseDataType> requestProperties, IRequestResponseListener<SuccessResponseDataType, ErrorResponseDataType> requestResponseListener);
}
