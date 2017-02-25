package stannieman.rest;

import stannieman.rest.models.KeyAuthRestClientFactoryConfig;

public interface IKeyAuthRestClientFactory extends IRestClientFactory {
    void loadConfig(KeyAuthRestClientFactoryConfig config);
}
