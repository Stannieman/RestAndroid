package stannieman.rest;

/**
 * An enum of the supported URI schemes.
 */
public enum Scheme {
    HTTP("http"),
    HTTPS("https");

    private final String name;

    Scheme(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}