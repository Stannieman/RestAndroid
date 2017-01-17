package stannieman.rest;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Factory that produces SimpleRestClients.
 */
public final class SimpleRestClientFactory implements IRestClientFactory {
    private final ObjectMapper objectMapper;
    private final RequestQueue requestQueue;
    private final Scheme scheme;
    private final String host;
    private final int port;
    private final String apiBasePath;
    private final long timeout;

    /**
     * Constructor to create a factory with an existing object mapper and request queue.
     * It is recommended to have only one object mapper and request queue in your application,
     * so if you have more than one REST client factory you should to create them yourself
     * and use this constructor to use the existing instances.
     * @param objectMapper object mapper to map objects from and to JSON
     * @param requestQueue Volley RequestQueue for performing requests
     * @param scheme URI schema
     * @param host REST server host
     * @param port REST server port
     * @param apiBasePath base path of the REST api
     * @param timeout timeout for the requests
     */
    public SimpleRestClientFactory(ObjectMapper objectMapper, RequestQueue requestQueue, Scheme scheme, String host, int port, String apiBasePath, long timeout) {
        this.objectMapper = objectMapper;
        this.requestQueue = requestQueue;
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.apiBasePath = apiBasePath;
        this.timeout = timeout;
    }

    /**
     * Constructor to create a factory without an existing object mapper and request queue.
     * A new object mapper and request queue will be created for performing the REST calls.
     * @param scheme URI schema
     * @param host REST server host
     * @param port REST server port
     * @param apiBasePath base path of the REST api
     * @param timeout timeout for the requests
     */
    public SimpleRestClientFactory(Context context, Scheme scheme, String host, int port, String apiBasePath, long timeout) {
        this(new ObjectMapper(), Volley.newRequestQueue(context), scheme, host, port, apiBasePath, timeout);
    }

    @Override
    public IRestClient getRestClient(String endpointPath) {
        return new SimpleRestClient(objectMapper, requestQueue, scheme, host, port, apiBasePath, endpointPath, timeout);
    }
}
