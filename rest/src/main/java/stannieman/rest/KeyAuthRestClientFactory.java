package stannieman.rest;

import android.content.Context;

import stannieman.rest.models.KeyAuthRestClientFactoryConfig;

/**
 * Factory that produces KeyAuthRestClients.
 */
public final class KeyAuthRestClientFactory extends RestClientFactoryBase implements IKeyAuthRestClientFactory {
    private String keyParameterName;
    private String key;

    /**
     * Constructor to create a factory.
     * @param context application context
     */
    public KeyAuthRestClientFactory(Context context) {
        super(context);
    }

    @Override
    public IRestClient getRestClient(String endpointPath) {
        configLock.lock();
        IRestClient client = new KeyAuthRestClient(objectMapper, requestQueue, scheme, host, port, apiBasePath, endpointPath, timeout, retryPolicy, keyParameterName, key);
        configLock.unlock();

        return client;
    }

    @Override
    public void loadConfig(KeyAuthRestClientFactoryConfig config) {
        configLock.lock();

        super.loadConfig(config);
        keyParameterName = config.getKeyParameterName();
        key = config.getKey();

        configLock.unlock();
    }
}
