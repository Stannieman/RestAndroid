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
     * Constructor to create a factory with the given config.
     * @param context application context
     * @param config configuration
     */
    public KeyAuthRestClientFactory(Context context, KeyAuthRestClientFactoryConfig config) {
        super(context);
        loadConfig(config);
    }

    @Override
    public IRestClient getRestClient(String endpointPath) {
        configLock.lock();
        IRestClient client = new KeyAuthRestClient(objectMapper, requestQueue, scheme, host, port, apiBasePath, endpointPath, timeout, keyParameterName, key);
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
