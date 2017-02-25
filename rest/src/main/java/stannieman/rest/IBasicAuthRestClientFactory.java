package stannieman.rest;

import stannieman.rest.models.BasicAuthRestClientFactoryConfig;

public interface IBasicAuthRestClientFactory extends IRestClientFactory {
    void loadConfig(BasicAuthRestClientFactoryConfig config);
}
