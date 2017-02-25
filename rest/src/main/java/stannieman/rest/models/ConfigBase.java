package stannieman.rest.models;

import com.android.volley.RequestQueue;
import com.fasterxml.jackson.databind.ObjectMapper;

import stannieman.rest.Scheme;

public abstract class ConfigBase {
    private ObjectMapper objectMapper;
    private RequestQueue requestQueue;
    private Scheme scheme;
    private String host;
    private int port;
    private String apiBasePath;
    private long timeout;

    /**
     * Gets the object mapper used for mapping objects to and from JSON.
     * @return object mapper
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Sets the object mapper used for mapping objects to and from JSON.
     * If this property is null the REST client factory will create a new mapper.
     * It is recommended to have only one object mapper in your application,
     * so if you have more than one REST client factory you should
     * create an object mapper yourself and specify it here.
     * @param objectMapper object mapper
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Gets the request queue used for doing network requests.
     * @return request queue
     */
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * Sets the request queue used for doing network requests.
     * If this property is null the REST client factory will create a new request queue.
     * It is recommended to have only one request queue in your application,
     * so if you have more than one REST client factory you should
     * create a request queue yourself and specify it here.
     * @param requestQueue request queue
     */
    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    /**
     * Gets the URI scheme.
     * @return URI scheme
     */
    public Scheme getScheme() {
        return scheme;
    }

    /**
     * Sets the URI scheme.
     * @param scheme URI scheme
     */
    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    /**
     * Gets the remote host.
     * @return remote host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the remote host.
     * @param host remote host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets the port on which the API is exposed.
     * @return port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the port on which the API is exposed.
     * @param port port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets the base path of the API.
     * @return base path of API
     */
    public String getApiBasePath() {
        return apiBasePath;
    }

    /**
     * Sets the base path of the API.
     * @param apiBasePath base path of API
     */
    public void setApiBasePath(String apiBasePath) {
        this.apiBasePath = apiBasePath;
    }

    /**
     * Gets the network timeout.
     * @return network timeout
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Sets the network timeout.
     * @param timeout network timeout
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
