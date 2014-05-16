package com.baloomba.wsvolley.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

public class WSRequest<T> extends Request<T> {

    // <editor-fold desc="VARIABLES">

    private WSResponseListener<T> mListener;
    private Map<String, String> mHeaders;
    private Map<String, String> mParams;
    private Priority mPriority;

    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">

    @SuppressWarnings("unchecked")
    protected WSRequest(final Builder builder) {
        super(builder.mMethod, builder.mUrl, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (builder.mListener != null)
                    builder.mListener.onErrorResponse(error);
            }
        });

        setRetryPolicy(builder.mRetryPolicy);
        setTag(builder.mTag);
        setShouldCache(builder.mShouldCache);

        mListener = builder.mListener;
        mHeaders = builder.mHeaders;
        mParams = builder.mParams;
        mPriority = builder.mPriority;

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

    // </editor-fold>

    // <editor-fold desc="BUILDER CLASS">

    public static class Builder {

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

        public Builder(int method, String url, String tag) {
            mMethod = checkMethod(method);
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

        public Builder setPriority(Priority priority) {
            mPriority = priority;
            return this;
        }

        public Builder setParams(Map<String, String> params) {
            if (params != null)
                mParams.putAll(params);
            return this;
        }

        public Builder addParam(String key, String value) {
            mParams.put(key, value);
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            if (headers != null)
                mHeaders.putAll(headers);
            return this;
        }

        public Builder addHeader(String key, String value) {
            mHeaders.put(key, value);
            return this;
        }

        public Builder setTimeout(int timeout) {
            mTimeout = timeout;
            return this;
        }

        public Builder setNbRetries(int nbRetries) {
            mNbRetries = nbRetries;
            return this;
        }

        public Builder setShouldCache(boolean shouldCache) {
            mShouldCache = shouldCache;
            return this;
        }

        public Builder setListener(WSResponseListener responseListener) {
            mListener = responseListener;
            return this;
        }

        // </editor-fold>

        // <editor-fold desc="METHODS">

        public WSRequest build() {
            mRetryPolicy = new DefaultRetryPolicy(mTimeout, mNbRetries,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            return new WSRequest(this);
        }

        public int checkMethod(int method) {
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
                    return method;
                default:
                    throw new IllegalAccessError("Unknown method type");
            }
        }

        // </editor-fold>

    }

    // </editor-fold>

}
