package com.baloomba.wsvolley.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.baloomba.wsvolley.helper.BitmapLruCache;

import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;

public class WSManager {

    // <editor-fold desc="VARIABLES">

    private static WSManager sInstance;

    private PersistentCookieStore mCookieStore;

//    private DefaultHttpClient mHttpClient;
    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    // </editor-fold>

    // <editor-fold desc="INSTANCE">

    public static WSManager getInstance() {
        return sInstance;
    }

    // </editor-fold>

    // <editor-fold desc="GETTERS">

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    // </editor-fold>

    // <editor-fold desc="COOKIES METHODS">

    public void saveCookies() {
//        List<Cookie> cookies = mHttpClient.getCookieStore().getCookies();
//        mCookieStore.clear();
//        for (Cookie cookie : cookies) {
//            mCookieStore.addCookie(cookie);
//        }
    }

    // </editor-fold>

    // <editor-fold desc="METHODS">

    public void init(Context appContext) {
        sInstance = this;

        CookieHandler.setDefault(new CookieManager( null, CookiePolicy.ACCEPT_ALL ));

//        mHttpClient = new DefaultHttpClient();

//        mRequestQueue = Volley.newRequestQueue(appContext, new HttpClientStack(mHttpClient));
        mRequestQueue = Volley.newRequestQueue(appContext);

        mCookieStore = new PersistentCookieStore(appContext);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache());
    }

    public void cancel(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public void cancelAll() {
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    public void send(WSRequest request) {
//        mHttpClient.setCookieStore(mCookieStore);
        mRequestQueue.add(request);
    }

    // </editor-fold>

}
