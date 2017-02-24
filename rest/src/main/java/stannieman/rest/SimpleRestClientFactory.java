package stannieman.rest;

import android.content.Context;

import stannieman.rest.models.SimpleRestClientFactoryConfig;

/**
 * Factory that produces SimpleRestClients.
 */
public final class SimpleRestClientFactory extends RestClientFactoryBase implements ISimpleRestClientFactory {

    /**
     * Constructor to create a factory.
     * @param context application context
     */
    public SimpleRestClientFactory(Context context) {
        super(context);
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
