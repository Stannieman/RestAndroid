package stannieman.rest;

import stannieman.rest.models.BasicAuthRestClientFactoryConfig;

public interface IBasicAuthRestClientFactory {
    void loadConfig(BasicAuthRestClientFactoryConfig config);
}
