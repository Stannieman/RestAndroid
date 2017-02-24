package stannieman.rest;

import stannieman.rest.models.SimpleRestClientFactoryConfig;

public interface ISimpleRestClientFactory extends IRestClientFactory {
    void loadConfig(SimpleRestClientFactoryConfig config);
}
