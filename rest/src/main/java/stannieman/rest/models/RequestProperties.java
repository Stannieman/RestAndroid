package stannieman.rest.models;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/**
 * Class to hold info about how a particular REST call should
 * be executed and data that's required for the performing the call.
 * @param <SuccessResponseDataType> response data type for a successful call
 * @param <ErrorResponseDataType> response data type for an unsuccessful call
 */
public final class RequestProperties<SuccessResponseDataType extends SuccessResponseDataBase, ErrorResponseDataType extends ErrorResponseDataBase> {
    private Class<SuccessResponseDataType> successResponseDataType;
    private Class<ErrorResponseDataType> errorResponseDataType;
    private String subPath;
    private List<String> subPathParameters;
    private List<AbstractMap.SimpleEntry<String, String>> queryParameters;
    private Map<String, String> headers;
    private Integer[] successStatusCodes;
    private Object body;

    /**
     * Gets the response data type for a successful call.
     * @return response data type
     */
    public Class<SuccessResponseDataType> getSuccessResponseDataType() {
        return successResponseDataType;
    }

    /**
     * Sets the response data type for a successful call.
     * @param successResponseDataType response data type
     */
    public void setSuccessResponseDataType(Class<SuccessResponseDataType> successResponseDataType) {
        this.successResponseDataType = successResponseDataType;
    }

    /**
     * Gets the response data type for an unsuccessful call.
     * @return response data type
     */
    public Class<ErrorResponseDataType> getErrorResponseDataType() {
        return errorResponseDataType;
    }

    /**
     * Sets the response data type for an unsuccessful call.
     * @param errorResponseDataType response data type
     */
    public void setErrorResponseDataType(Class<ErrorResponseDataType> errorResponseDataType) {
        this.errorResponseDataType = errorResponseDataType;
    }

    /**
     * Gets the sub path for the call.
     * @return sub path
     */
    public String getSubPath() {
        return subPath;
    }

    /**
     * Sets the sub path for the call.
     * This path can be in printf-style format for using parameters.
     * @param subPath sub path
     */
    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    /**
     * Gets the parameters for the sub path.
     * @return sub path parameters
     */
    public List<String> getSubPathParameters() {
        return subPathParameters;
    }

    /**
     * Sets the parameters for the sub path.
     * These parameters are inserted into the sub path if it is in printf-style format.
     * @param subPathParameters
     */
    public void setSubPathParameters(List<String> subPathParameters) {
        this.subPathParameters = subPathParameters;
    }

    /**
     * Gets the query parameters as a list of key values.
     * @return query parameters
     */
    public List<AbstractMap.SimpleEntry<String, String>> getQueryParameters() {
        return queryParameters;
    }

    /**
     * Sets the query parameters.
     * @param queryParameters query parameters
     */
    public void setQueryParameters(List<AbstractMap.SimpleEntry<String, String>> queryParameters) {
        this.queryParameters = queryParameters;
    }

    /**
     * Gets the headers for the request as a key value map.
     * @return request headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets the headers for the request.
     * @param headers request headers
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Gets the status codes for a successful request.
     * @return status codes for a successful request
     */
    public Integer[] getSuccessStatusCodes() {
        return successStatusCodes;
    }

    /**
     * Sets the status codes for a successful request.
     * If the status code of a request exists in this array the request is treated as successful.
     * @param successStatusCodes status codes for a successful request
     */
    public void setSuccessStatusCodes(Integer[] successStatusCodes) {
        this.successStatusCodes = successStatusCodes;
    }

    /**
     * Gets the object representing the body for the request.
     * @return body for the request
     */
    public Object getBody() {
        return body;
    }

    /**
     * Sets the object representing the body for the request.
     * This object will be parsed to JSON.
     * @param body body for the request
     */
    public void setBody(Object body) {
        this.body = body;
    }
}
