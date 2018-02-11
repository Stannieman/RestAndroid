package stannieman.rest;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import stannieman.rest.models.ConfigBase;

public abstract class RestClientFactoryBase implements IRestClientFactory {
    protected Scheme scheme;
    protected String host;
    protected int port;
    protected String apiBasePath;
    protected long timeout;
    protected ObjectMapper objectMapper;
    protected RequestQueue requestQueue;
    protected RetryPolicy retryPolicy;

    protected Lock configLock = new ReentrantLock();

    private final Context context;

    protected RestClientFactoryBase(Context context) {
        this.context = context;
    }

    protected void loadConfig(ConfigBase config) {
        scheme = config.getScheme();
        host = config.getHost();
        port = config.getPort();
        apiBasePath = config.getApiBasePath();
        timeout = config.getTimeout();
        retryPolicy = new DefaultRetryPolicy(Integer.MAX_VALUE, 0, 0);

        ObjectMapper newObjectMapper = config.getObjectMapper();
        if (newObjectMapper != null) {
            objectMapper = newObjectMapper;
        }
        else if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        RequestQueue newRequestQueue = config.getRequestQueue();
        if (newRequestQueue != null) {
            requestQueue = newRequestQueue;
        }
        else if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }
}
