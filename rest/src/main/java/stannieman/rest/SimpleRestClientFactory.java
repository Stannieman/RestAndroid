package stannieman.rest;

import android.content.Context;

import stannieman.rest.models.SimpleRestClientFactoryConfig;

/**
 * Factory that produces SimpleRestClients.
 */
public final class SimpleRestClientFactory extends RestClientFactoryBase implements ISimpleRestClientFactory {

    /**
     * Constructor to create a factory with the given config.
     * @param context application context
     * @param config configuration
     */
    public SimpleRestClientFactory(Context context, SimpleRestClientFactoryConfig config) {
        super(context);
        loadConfig(config);
    }

    @Override
    public IRestClient getRestClient(String endpointPath) {
        configLock.lock();
        IRestClient client = new SimpleRestClient(objectMapper, requestQueue, scheme, host, port, apiBasePath, endpointPath, timeout);
        configLock.unlock();

        return client;
    }

    @Override
    public void loadConfig(SimpleRestClientFactoryConfig config) {
        configLock.lock();
        super.loadConfig(config);
        configLock.unlock();
    }
}
