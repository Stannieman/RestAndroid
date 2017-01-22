package stannieman.rest;

import android.content.Context;

import stannieman.rest.models.BasicAuthRestClientFactoryConfig;

/**
 * Factory that produces BasicAuthRestClients.
 */
public final class BasicAuthRestClientFactory extends RestClientFactoryBase implements IBasicAuthRestClientFactory {
    private String username;
    private String password;

    /**
     * Constructor to create a factory with the given config.
     * @param context application context
     * @param config configuration
     */
    public BasicAuthRestClientFactory(Context context, BasicAuthRestClientFactoryConfig config) {
        super(context);
        loadConfig(config);
    }

    @Override
    public IRestClient getRestClient(String endpointPath) {
        configLock.lock();
        IRestClient client = new BasicAuthRestClient(objectMapper, requestQueue, scheme, host, port, apiBasePath, endpointPath, timeout, username, password);
        configLock.unlock();

        return client;
    }

    @Override
    public void loadConfig(BasicAuthRestClientFactoryConfig config) {
        configLock.lock();

        super.loadConfig(config);
        username = config.getUsername();
        password = config.getPassword();

        configLock.unlock();
    }
}
