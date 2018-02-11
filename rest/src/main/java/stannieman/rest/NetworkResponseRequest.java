package stannieman.rest;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

class NetworkResponseRequest extends Request<NetworkResponse> {
    private static final String BODY_CONTENT_TYPE = "application/json; charset=UTF-8";

    private final Response.Listener<NetworkResponse> responseListener;
    private final Map<String, String> headers;
    private final String body;
    private final String encoding;

    public NetworkResponseRequest(int method, String url, Map<String, String> headers, String body, RetryPolicy retryPolicy, Response.Listener<NetworkResponse> responseListener, Response.ErrorListener errorListener, String encoding) {
        super(method, url, errorListener);
        this.responseListener = responseListener;
        this.headers = headers;
        this.body = body;
        this.encoding = encoding;
        this.setRetryPolicy(retryPolicy);
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        responseListener.onResponse(response);
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers != null ? headers : new HashMap<String, String>();
    }

    @Override
    public byte[] getBody() {
        try {
            return body != null ? body.getBytes(encoding) : new byte[0];
        }
        catch (UnsupportedEncodingException e) {
            return new byte[0];
        }
    }

    @Override
    public String getBodyContentType() {
        return BODY_CONTENT_TYPE;
    }

    public static String parseToString(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return parsed;
    }
}
