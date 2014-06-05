package com.baloomba.wsvolley;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

public interface WSResponseListener<T> {
    public T parseResponse(NetworkResponse networkResponse);
    public void onResponse(T response);
    public void onErrorResponse(VolleyError error);
}
