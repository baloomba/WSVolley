package com.baloomba.wsvolley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.OutputStreamProgress;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WSMultipartRequest extends WSRequest<String> {

    // <editor-fold desc="VARIABLES">

    private static final String TAG = WSMultipartRequest.class.getSimpleName();
    private HttpEntity mEntity;

    // </editor-fold>

    // <editor-fold desc="">

    protected WSMultipartRequest(Init<?> builder) {
        super(builder);
        mEntity = builder.mEntity;
    }

    // </editor-fold>

    // <editor-fold desc="REQUEST OVERRIDDEN METHODS">

    @Override
    public String getBodyContentType() {
        return mEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        OutputStreamProgress outputStreamProgress =
                new OutputStreamProgress(new ByteArrayOutputStream(), this,
                        mEntity.getContentLength());
        try {
            mEntity.writeTo(outputStreamProgress);
        } catch (IOException e) {
            Log.e(TAG, "IOException writing to ByteArrayOutputStream");
        }
        return ((ByteArrayOutputStream)outputStreamProgress.getOutputStream()).toByteArray();
    }

    // </editor-fold>

    // <editor-fold desc="INIT BUILDER CLASS">

    public static abstract class Init<Te extends Init<Te>> extends WSRequest.Init<Te> {

        // <editor-fold desc="VARIABLES">

        private HttpEntity mEntity;
        private MultipartEntityBuilder mMultipartEntityBuilder;

        // </editor-fold>

        // <editor-fold desc="CONSTRUCTORS">

        public Init(int method, String url, String tag) {
            super(method, url, tag);
            mMultipartEntityBuilder = MultipartEntityBuilder.create();
        }

        // </editor-fold>

        // <editor-fold desc="SETTERS">

        public Te addParam(String key, String value) {
            mMultipartEntityBuilder.addPart(key, new StringBody(value,
                    ContentType.create("text/plain", MIME.UTF8_CHARSET)));
            return self();
        }

        public Te addMultipartEntity(String key, byte[] data, String mimeType, String fileName) {
            mMultipartEntityBuilder.addPart(key, new ByteArrayBody(data,
                    ContentType.create(mimeType), fileName));
            return self();
        }

        // </editor-fold>

        // <editor-fold desc="METHODS">

        public WSMultipartRequest build() {
            setRetryPolicy();
            mEntity = mMultipartEntityBuilder.build();
            return new WSMultipartRequest(this);
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

}
