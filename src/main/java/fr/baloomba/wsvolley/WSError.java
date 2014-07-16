package fr.baloomba.wsvolley;

import com.android.volley.VolleyError;

public class WSError extends VolleyError {

    // <editor-fold desc="GLOBAL VARIABLES">

    public static final int DEFAULT = -1;

    // </editor-fold>

    // <editor-fold desc="VARIABLES">

    private int mType;

    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">

    public WSError() {
        super();
        mType = DEFAULT;
    }

    public WSError(int type) {
        super();
        mType = type;
    }

    public WSError(com.android.volley.NetworkResponse response) {
        super(response);
        mType = DEFAULT;
    }

    public WSError(com.android.volley.NetworkResponse response, int type) {
        super(response);
        mType = type;
    }

    public WSError(java.lang.String exceptionMessage) {
        super(exceptionMessage);
        mType = DEFAULT;
    }

    public WSError(java.lang.String exceptionMessage, int type) {
        super(exceptionMessage);
        mType = type;
    }

    public WSError(java.lang.String exceptionMessage, java.lang.Throwable reason) {
        super(exceptionMessage, reason);
        mType = DEFAULT;
    }

    public WSError(java.lang.String exceptionMessage, java.lang.Throwable reason, int type) {
        super(exceptionMessage, reason);
        mType = type;
    }

    public WSError(java.lang.Throwable cause) {
        super(cause);
        mType = DEFAULT;
    }

    public WSError(java.lang.Throwable cause, int type) {
        super(cause);
        mType = type;
    }

    // </editor-fold>

    // <editor-fold desc="GETTERS">

    public int getType() {
        return mType;
    }

    // </editor-fold>

}
