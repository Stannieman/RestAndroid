package stannieman.rest.models;

/**
 * Class containing configuration for a BasicAuthRestClientFactory.
 */
public class BasicAuthRestClientFactoryConfig extends ConfigBase {
    private String username;
    private String password;

    /**
     * Gets the username for basic authentication.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for basic authentication.
     * @param username username
     */
    public void setUserName(String username) {
        this.username = username;
    }

    /**
     * Gets the password for basic authentication.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for basic authentication.
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
