package stannieman.rest.models;

/**
 * Class containing configuration for a KeyAuthRestClientFactory.
 */
public class KeyAuthRestClientFactoryConfig extends ConfigBase {
    private String keyParameterName;
    private String key;

    /**
     * Gets the name of the API key parameter.
     * @return name of the API key parameter
     */
    public String getKeyParameterName() {
        return keyParameterName;
    }

    /**
     * Sets the name of the API key parameter
     * @param keyParameterName name of the API key parameter
     */
    public void setKeyParameterName(String keyParameterName) {
        this.keyParameterName = keyParameterName;
    }

    /**
     * Gets the API key
     * @return API key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the API key
     * @param key API key
     */
    public void setKey(String key) {
        this.key = key;
    }
}
