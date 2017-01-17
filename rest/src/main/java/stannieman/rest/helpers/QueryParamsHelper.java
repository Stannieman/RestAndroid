package stannieman.rest.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.List;

/**
 * Class containing helper methods for query parameters.
 */
public abstract class QueryParamsHelper {

    /**
     * Converts a list of query parameters to a query string.
     * @param queryParameters parameters to create query string for
     * @param encoding encoding for URL encoding
     * @return the resulting query string
     * @throws UnsupportedEncodingException when the given encoding is not supported
     */
    public static String getQueryString(List<AbstractMap.SimpleEntry<String, String>> queryParameters, String encoding) throws UnsupportedEncodingException {
        if (queryParameters == null || queryParameters.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder("?");

        for (AbstractMap.SimpleEntry<String, String> param : queryParameters) {
            builder.append(replacePlus(URLEncoder.encode(param.getKey(), encoding)));
            builder.append("=");
            builder.append(replacePlus(URLEncoder.encode(param.getValue(), encoding)));
            builder.append("&");
        }

        // Remove trailing ampersand
        builder.setLength(builder.length() - 1);

        return builder.toString();
    }

    private static String replacePlus(String encodedString) {
        return encodedString.replace("+", "%20");
    }
}
