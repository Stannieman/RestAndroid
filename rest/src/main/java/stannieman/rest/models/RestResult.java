package stannieman.rest.models;

/**
 * Class that represents the result of a REST call.
 * @param <SuccessResponseDataType> type of the object returned by the end point when the request was successful
 * @param <ErrorResponseDataType> type of the object returned by the end point when the request was not successful
 */
public final class RestResult<SuccessResponseDataType extends SuccessResponseDataBase, ErrorResponseDataType extends ErrorResponseDataBase> {

    //region isSuccess

    private boolean isSuccess;

    /**
     * Returns whether the REST call was successful.
     * @return whether the REST call was successful
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    //endregion

    //region statusCode

    private int statusCode;

    /**
     * Return the HTTP status code for the REST call.
     * @return HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    //endregion

    //region successData

    private SuccessResponseDataType successData;

    /**
     * Returns the data returned by the called end point for a successful request.
     * @return data returned by the end point on a successful request or null if there is no return data or an error occurred
     */
    public SuccessResponseDataType getSuccessData() {
        return successData;
    }

    //endregion

    //region errorData

    private ErrorResponseDataType errorData;

    /**
     * If an error occurred it is possible that the end point returns an object containing
     * info about the error. This method returns that object.
     * @return the error object returned by the end point in case of an unsuccessful request or null if no error occurred or no error object was returned
     */
    public ErrorResponseDataType getErrorData() {
        return errorData;
    }

    //endregion

    /**
     * Constructor to set the HTTP status code.
     * Initializes the result as successful with the given HTTP status code.
     * The data and error objects are left null.
     * @param statusCode HTTP status code
     */
    public RestResult(int statusCode) {
        this.isSuccess = true;
        this.statusCode = statusCode;
    }

    /**
     * Constructor to set the isSuccess state and HTTP status code.
     * The data and error objects are left null.
     * @param isSuccess whether the REST call was successful
     * @param statusCode HTTP status code
     */
    public RestResult(boolean isSuccess, int statusCode) {
        this.isSuccess = isSuccess;
        this.statusCode = statusCode;
    }

    /**
     * Constructor that initializes the result as successful
     * with the given HTTP status code and the data returned by the end point.
     * @param statusCode HTTP status code
     * @param successData data returned by the end point for a successful request
     */
    public RestResult(int statusCode, SuccessResponseDataType successData) {
        this.isSuccess = true;
        this.statusCode = statusCode;
        this.successData = successData;
    }

    /**
     * Constructor that initializes the result as unsuccessful
     * with the given HTTP status code and the error data returned by the end point.
     * @param statusCode HTTP status code
     * @param errorData error data returned by the end point for an unsuccessful request
     */
    public RestResult(int statusCode, ErrorResponseDataType errorData) {
        this.isSuccess = false;
        this.statusCode = statusCode;
        this.errorData = errorData;
    }
}