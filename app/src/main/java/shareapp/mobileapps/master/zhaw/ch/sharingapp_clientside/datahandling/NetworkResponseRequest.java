package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

public class NetworkResponseRequest extends Request<NetworkResponse> {
    /** Lock to guard mListener as it is cleared on cancel() and read on delivery. */
    private final Object mLock = new Object();
    /** Default charset for JSON request. */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private final String mRequestBody;



    // @GuardedBy("mLock")
    private Response.Listener<NetworkResponse> mListener;

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the Network response
     * @param errorListener Error listener, or null to ignore errors
     */
    public NetworkResponseRequest(int method, String url, String requestBody, Response.Listener<NetworkResponse> listener,
                         Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mRequestBody = requestBody;
    }

    /**
     * Creates a new GET request.
     *
     * @param url URL to fetch the NetworkResponse at
     * @param listener Listener to receive the NetworkResponse
     * @param errorListener Error listener, or null to ignore errors
     */
    public NetworkResponseRequest(String url, String requestBody, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, requestBody, listener, errorListener);
    }

    @Override
    public void cancel() {
        super.cancel();
        synchronized (mLock) {
            mListener = null;
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        Response.Listener<NetworkResponse> listener;
        synchronized (mLock) {
            listener = mListener;
        }
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    public static String parseResponseToString(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return parsed;
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }
}
