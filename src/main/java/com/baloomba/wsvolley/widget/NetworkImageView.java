package com.baloomba.wsvolley.widget;

import android.content.Context;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;

import android.graphics.Bitmap;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;

import android.util.AttributeSet;

import com.baloomba.wsvolley.R;
import com.baloomba.wsvolley.network.WSManager;

public class NetworkImageView extends com.android.volley.toolbox.NetworkImageView {

    // <editor-fold desc="VARIABLES">

    private static final String TAG = NetworkImageView.class.getSimpleName();

    private static final String NAME_SPACE = "http://schemas.android.com/apk/res-auto";

    private static final String RATIO = "ratio";
    private static final String FADE_IN_TIME = "fade_in_time";
    private static final String CORNER_RADIUS = "corner_radius";
    private static final String BORDER_WIDTH = "border_width";

    public static final int DEFAULT_FADE_IN_TIME = 0;
    public static final float DEFAUTL_RATIO = 0F;
    public static final int DEFAULT_RADIUS = 0;
    public static final int DEFAULT_BORDER_WIDTH = 0;

    private int mFadeInTime = DEFAULT_FADE_IN_TIME;
    private Float mRatio = DEFAUTL_RATIO;
    private static final ScaleType[] sScaleTypeArray = {
            ScaleType.MATRIX,
            ScaleType.FIT_XY,
            ScaleType.FIT_START,
            ScaleType.FIT_CENTER,
            ScaleType.FIT_END,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE
    };
    private int mCornerRadius = DEFAULT_RADIUS;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private ColorStateList mBorderColor = ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
    private boolean mRoundBackground = false;
    private boolean mOval = false;
    private Drawable mDrawable;
    private Drawable mBackgroundDrawable;
    private ScaleType mScaleType;

    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">

    public NetworkImageView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public NetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    // </editor-fold>

    // <editor-fold desc="GETTERS">

    @SuppressWarnings("unused")
    public int getCornerRadius() {
        return mCornerRadius;
    }

    @SuppressWarnings("unused")
    public int getBorderWidth() {
        return mBorderWidth;
    }

    @SuppressWarnings("unused")
    public int getBorderColor() {
        return mBorderColor.getDefaultColor();
    }

    @SuppressWarnings("unused")
    public ColorStateList getBorderColors() {
        return mBorderColor;
    }

    @SuppressWarnings("unused")
    public boolean isOval() {
        return mOval;
    }

    @SuppressWarnings("unused")
    public boolean isRoundBackground() {
        return mRoundBackground;
    }

    // </editor-fold>

    // <editor-fold desc="SETTERS">

    @SuppressWarnings("unused")
    public void setCornerRadius(int radius) {
        if (mCornerRadius == radius) {
            return;
        }

        mCornerRadius = radius;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs();
    }

    @SuppressWarnings("unused")
    public void setBorderWidth(int width) {
        if (mBorderWidth == width) {
            return;
        }

        mBorderWidth = width;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs();
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setBorderColor(int color) {
        setBorderColors(ColorStateList.valueOf(color));
    }

    public void setBorderColors(ColorStateList colors) {
        if (mBorderColor.equals(colors)) {
            return;
        }

        mBorderColor = (colors != null) ? colors : ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs();
        if (mBorderWidth > 0) {
            invalidate();
        }
    }

    @SuppressWarnings("unused")
    public void setOval(boolean oval) {
        mOval = oval;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs();
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setRoundBackground(boolean roundBackground) {
        if (mRoundBackground == roundBackground) {
            return;
        }

        mRoundBackground = roundBackground;
        updateBackgroundDrawableAttrs();
        invalidate();
    }

    // </editor-fold>

    // <editor-fold desc="MEMBER METHODS">

    public void init(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NetworkImageView,
                    defStyle, 0);
            mRatio = attrs.getAttributeFloatValue(NAME_SPACE, RATIO, DEFAUTL_RATIO);
            mFadeInTime = attrs.getAttributeIntValue(NAME_SPACE, FADE_IN_TIME, DEFAULT_FADE_IN_TIME);

            int index = a != null ?
                    a.getInt(R.styleable.NetworkImageView_android_scaleType, -1) : -1;
            if (index >= 0)
                setScaleType(sScaleTypeArray[index]);

            mCornerRadius = a != null ?
                    a.getDimensionPixelSize(R.styleable.NetworkImageView_corner_radius,
                            DEFAULT_RADIUS)
                    : DEFAULT_RADIUS;
            if (mCornerRadius < 0)
                mCornerRadius = DEFAULT_RADIUS;

            mBorderWidth = a != null ?
                    a.getDimensionPixelSize(R.styleable.NetworkImageView_border_width,
                            DEFAULT_BORDER_WIDTH)
                    : DEFAULT_BORDER_WIDTH;
            if (mBorderWidth < 0)
                mBorderWidth = DEFAULT_BORDER_WIDTH;

            mBorderColor = a != null ? a.getColorStateList(
                    R.styleable.NetworkImageView_border_color) :
                    ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
            if (mBorderColor == null)
                mBorderColor = ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);

            mRoundBackground = a != null &&
                    a.getBoolean(R.styleable.NetworkImageView_round_background, false);

            mOval = (a != null) && a.getBoolean(R.styleable.NetworkImageView_is_oval, false);

            updateDrawableAttrs();
            updateBackgroundDrawableAttrs();
            if (a != null)
                a.recycle();
        }
    }

    private void updateDrawableAttrs() {
        updateAttrs(mDrawable, false);
    }

    private void updateBackgroundDrawableAttrs() {
        updateAttrs(mBackgroundDrawable, true);
    }

    private void updateAttrs(Drawable drawable, boolean background) {
        if (drawable == null) {
            return;
        }
        if (drawable instanceof RoundedDrawable) {
            ((RoundedDrawable) drawable).setScaleType(mScaleType)
                    .setCornerRadius(!mRoundBackground && background ? 0 : mCornerRadius)
                    .setBorderWidth(!mRoundBackground && background ? 0 : mBorderWidth)
                    .setBorderColors(mBorderColor)
                    .setOval(mOval);
        } else if (drawable instanceof LayerDrawable) {
            // loop through layers to and set drawable attrs
            LayerDrawable ld = ((LayerDrawable) drawable);
            int layers = ld.getNumberOfLayers();
            for (int i = 0; i < layers; i++) {
                updateAttrs(ld.getDrawable(i), background);
            }
        }
    }

    public void setImageUrl(String url) {
        super.setImageUrl(url, WSManager.getInstance().getImageLoader());
    }

    // </editor-fold>

    // <editor-fold desc="OVERRIDDEN VOLLEY NETWORK IMAGE VIEW METHODS">

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRatio.compareTo(0f) != 0) {
            int width = getMeasuredWidth();
            int height = (int)(mRatio * getMeasuredWidth());
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    /**
     * Return the current scale type in use by this ImageView.
     *
     * @attr ref android.R.styleable#ImageView_scaleType
     * @see android.widget.ImageView.ScaleType
     */
    @Override
    public ScaleType getScaleType() {
        return mScaleType;
    }

    /**
     * Controls how the image should be resized or moved to match the size
     * of this ImageView.
     *
     * @param scaleType The desired scaling mode.
     * @attr ref android.R.styleable#ImageView_scaleType
     */
    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            throw new NullPointerException();
        }
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            switch (scaleType) {
                case CENTER:
                case CENTER_CROP:
                case CENTER_INSIDE:
                case FIT_CENTER:
                case FIT_START:
                case FIT_END:
                case FIT_XY:
                    super.setScaleType(ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scaleType);
                    break;
            }
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs();
            invalidate();
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm != null) {
            mDrawable = new RoundedDrawable(bm);
            updateDrawableAttrs();
        } else
            mDrawable = null;
        if (mFadeInTime > 0 && mDrawable != null) {
            TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                    new ColorDrawable(android.R.color.transparent),
                    mDrawable
            });
            super.setImageDrawable(td);
            td.startTransition(mFadeInTime);
        } else
            super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (drawable != null) {
            mDrawable = RoundedDrawable.fromDrawable(drawable);
            updateDrawableAttrs();
        } else
            mDrawable = null;
        if (mFadeInTime > 0 && mDrawable != null) {
            TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                    new ColorDrawable(android.R.color.transparent),
                    mDrawable
            });
            super.setImageDrawable(td);
            td.startTransition(mFadeInTime);
        } else
            super.setImageDrawable(mDrawable);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public void setBackgroundDrawable(Drawable background) {
        mBackgroundDrawable = RoundedDrawable.fromDrawable(background);
        updateBackgroundDrawableAttrs();
        super.setBackgroundDrawable(mBackgroundDrawable);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setImageDrawable(getDrawable());
    }

    // </editor-fold>

}
