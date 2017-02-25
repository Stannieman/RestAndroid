package stannieman.rest;

/**
 * Interface representing a factory for REST clients.
 */
public interface IRestClientFactory {
    /**
     * Creates a rest client for the given endpoint.
     * @param endpointPath relative path of the endpoint the client will perform requests on
     * @return a REST client
     */
    IRestClient getRestClient(String endpointPath);
}
