package fr.baloomba.wsvolley;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

public abstract class WSStringResponseListener implements WSResponseListener<String> {
    @Override
    public String parseResponse(NetworkResponse networkResponse) {
        String response;
        byte[] data = networkResponse.data;
        try {
            response = new String(data, HttpHeaderParser.parseCharset(networkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            response = new String(data);
        }
        return response;
    }

    @Override
    public void onDownloadProgress(long current, long total) {}

    @Override
    public void onUploadProgress(long current, long total) {}

}
