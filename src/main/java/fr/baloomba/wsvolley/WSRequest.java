package fr.baloomba.wsvolley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ProgressListener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

public class WSRequest<T> extends Request<T> implements ProgressListener {

    // <editor-fold desc="VARIABLES">

    protected WSResponseListener<T> mListener;
    protected Map<String, String> mHeaders;
    protected Map<String, String> mParams;
    protected Priority mPriority;
    protected Object mTag;

    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">

    @SuppressWarnings("unchecked")
    protected WSRequest(final Init<?> builder) {
        super(builder.mMethod, builder.mUrl, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (builder.mListener != null)
                    builder.mListener.onErrorResponse(error);
            }
        });

        mTag = builder.mTag;

        setRetryPolicy(builder.mRetryPolicy);
        setTag(mTag);
        setShouldCache(builder.mShouldCache);

        mListener = builder.mListener;
        mHeaders = builder.mHeaders;
        mParams = builder.mParams;
        mPriority = builder.mPriority;
    }

    // </editor-fold>

    // <editor-fold desc="GETTERS">

    public Object getTag() {
        return mTag;
    }

    // </editor-fold>

    // <editor-fold desc="REQUEST OVERRIDDEN METHODS">

    @Override
    protected void deliverResponse(T response) {
        if(mListener != null)
            mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return mParams != null ? mParams : super.getParams();
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success((mListener != null ? mListener.parseResponse(networkResponse) :
                null), HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    // </editor-fold>

    // <editor-fold desc="PROGRESS LISTENER IMPLEMENTATION METHODS">

    @Override
    public void onDownloadProgress(long current, long total) {
        if (mListener != null) {
            mListener.onDownloadProgress(current, total);
        }
    }

    @Override
    public void onUploadProgress(long current, long total) {
        if (mListener != null) {
            mListener.onUploadProgress(current, total);
        }
    }

    // </editor-fold>

    // <editor-fold desc="INIT BUILDER CLASS">

    public static abstract class Init<Te extends Init<Te>> {

        // <editor-fold desc="VARIABLES">

        private int mMethod;
        private String mUrl;
        private Object mTag;

        private WSResponseListener mListener;
        private Map<String, String> mHeaders;
        private Map<String, String> mParams;
        private Priority mPriority;
        private int mNbRetries;
        private int mTimeout;
        private RetryPolicy mRetryPolicy;
        private Boolean mShouldCache;

        // </editor-fold>

        // <editor-fold desc="CONSTRUCTORS">

        public Init(int method, String url, String tag) {
            mMethod = checkMethodExist(method);
            if (url != null)
                mUrl = url;
            else
                throw new IllegalArgumentException("Url can't be null");
            mTag = tag != null ? tag : "";
            mHeaders = new HashMap<String, String>();
            mParams = new HashMap<String, String>();
            mPriority = WSPriority.NORMAL;
            mNbRetries = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;
            mTimeout = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
            mRetryPolicy = new DefaultRetryPolicy();
            mShouldCache = true;
        }

        // </editor-fold>

        // <editor-fold desc="SETTERS">

        public Te setPriority(Priority priority) {
            mPriority = priority;
            return self();
        }

        public Te setParams(Map<String, String> params) {
            if (params != null)
                mParams.putAll(params);
            return self();
        }

        public Te addParam(String key, String value) {
            mParams.put(key, value);
            return self();
        }

        public Te setHeaders(Map<String, String> headers) {
            if (headers != null)
                mHeaders.putAll(headers);
            return self();
        }

        public Te addHeader(String key, String value) {
            mHeaders.put(key, value);
            return self();
        }

        public Te setTimeout(int timeout) {
            mTimeout = timeout;
            return self();
        }

        public Te setNbRetries(int nbRetries) {
            mNbRetries = nbRetries;
            return self();
        }

        public Te setShouldCache(boolean shouldCache) {
            mShouldCache = shouldCache;
            return self();
        }

        public Te setListener(WSResponseListener responseListener) {
            mListener = responseListener;
            return self();
        }

        // </editor-fold>

        // <editor-fold desc="METHODS">

        protected abstract Te self();

        public WSRequest build() {
            setRetryPolicy();
            return new WSRequest(this);
        }

        protected void setRetryPolicy() {
            mRetryPolicy = new DefaultRetryPolicy(mTimeout, mNbRetries,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        }

        // </editor-fold>

    }

    // </editor-fold>

    // <editor-fold desc="BUILDER CLASS">

    public static class Builder extends Init<Builder> {

        // <editor-fold desc="CONSTRUCTORS">

        public Builder(int method, String url, String tag) {
            super(method, url, tag);
        }

        // </editor-fold>

        // <editor-fold desc="METHODS">

        @Override
        protected Builder self() {
            return this;
        }

        // </editor-fold>

    }

    // </editor-fold>

    // <editor-fold desc="METHODS">

    private static int checkMethodExist(int method) {
        switch (method) {
            case WSMethod.DEPRECATED_GET_OR_POST:
            case WSMethod.GET:
            case WSMethod.POST:
            case WSMethod.PUT:
            case WSMethod.DELETE:
            case WSMethod.HEAD:
            case WSMethod.OPTIONS:
            case WSMethod.TRACE:
            case WSMethod.PATCH:
            case WSMethod.POST_MULTI_PART:
                return method;
            default:
                throw new IllegalAccessError("Unknown method type");
        }
    }

    // </editor-fold>

}
