/*
 * Copyright (C) 2014 Kalin Maldzhanski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.djodjo.jjson.atools.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;



import java.util.LinkedList;

public class MultiSlider extends View {

    int mMinWidth;
    int mMaxWidth;
    int mMinHeight;
    int mMaxHeight;

    /**
     * global Min and Max
     */
    private int mScaleMin;
    private int mScaleMax;


    private Drawable mProgressDrawable;

    Bitmap mSampleTile;
    //used in constructor to prevent invalidating before ready state
    private boolean mNoInvalidate;
    private long mUiThreadId;

    private boolean mInDrawing;
    private boolean mAttached;
    private boolean mRefreshIsPosted;

    boolean mMirrorForRtl = false;

    //list of all the loaded thumbs
    private LinkedList<Thumb> mThumbs;

    /**
     * On touch, this offset plus the scaled value from the position of the
     * touch will form the progress value. Usually 0.
     */
    float mTouchProgressOffset;

    /**
     * Whether this is user seekable.
     */
    boolean mIsUserSeekable = true;

    /**
     * On key presses (right or left), the amount to increment/decrement the
     * progress.
     */
    private int mKeyProgressIncrement = 1;

    private static final int NO_ALPHA = 0xFF;
    private float mDisabledAlpha = 0.5f;

    private int mScaledTouchSlop;
    private float mTouchDownX;
    private boolean mIsDragging;

    public class Thumb {
        //abs min value for this thumb
        private int min;
        //abs max value for this thumb
        private int max;
        //current value of this thumb
        private int value;
        //thumb drawable, can be shared
        private Drawable thumb;
        //thumb range drawable, can also be shared
        //this is the line from the beginning or the previous thumb if any until the this one.
        private Drawable range;
        private int thumbOffset;

        public Drawable getRange() {
            return range;
        }

        public Thumb setRange(Drawable range) {
            this.range = range;
            return this;
        }

        public Thumb() {
        }

        public int getMin() {
            return min;
        }

        public Thumb setMin(int min) {
            this.min = min;
            return this;
        }

        public int getMax() {
            return max;
        }

        public Thumb setMax(int max) {
            this.max = max;
            return this;
        }

        public int getValue() {
            return value;
        }

        public Thumb setValue(int value) {
            this.value = value;
            return this;
        }

        public Drawable getThumb() {
            return thumb;
        }

        public Thumb setThumb(Drawable mThumb) {
            this.thumb = mThumb;
            return this;
        }

        public int getThumbOffset() {
            return thumbOffset;
        }

        public Thumb setThumbOffset(int mThumbOffset) {
            this.thumbOffset = mThumbOffset;
            return this;
        }



    }

    public MultiSlider(Context context) {
        this(context, null);
    }

    public MultiSlider(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.multiSliderStyle);
    }

    public MultiSlider(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public MultiSlider(Context context, AttributeSet attrs, int defStyle, int styleRes) {
        super(context, attrs, defStyle);

        mUiThreadId = Thread.currentThread().getId();
        initMultiSlider();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiSlider, defStyle, styleRes);
        mNoInvalidate = true;

        Drawable drawable = a.getDrawable(R.styleable.MultiSlider_progressDrawable);
        if (drawable != null) {
            drawable = tileify(drawable, false);
            // Calling this method can set mMaxHeight, make sure the corresponding
            // XML attribute for mMaxHeight is read after calling this method
            setProgressDrawable(drawable);
        }


        mMinWidth = a.getDimensionPixelSize(R.styleable.MultiSlider_minWidth, mMinWidth);
        mMaxWidth = a.getDimensionPixelSize(R.styleable.MultiSlider_maxWidth, mMaxWidth);
        mMinHeight = a.getDimensionPixelSize(R.styleable.MultiSlider_minHeight, mMinHeight);
        mMaxHeight = a.getDimensionPixelSize(R.styleable.MultiSlider_maxHeight, mMaxHeight);

        setMax(a.getInt(R.styleable.MultiSlider_max, mScaleMax));

        setProgress(a.getInt(R.styleable.ProgressBar_progress, mProgress));

        mNoInvalidate = false;


        mMirrorForRtl = a.getBoolean(R.styleable.MultiSlider_mirrorForRtl, mMirrorForRtl);



        // --> now place thumbs

        Drawable thumbDrawable = a.getDrawable(R.styleable.MultiSlider_thumb);

        setThumbs(thumbDrawable); // will guess thumbOffset if thumb != null...
        // ...but allow layout to override this
        int thumbOffset = a.getDimensionPixelOffset(R.styleable.MultiSlider_thumbOffset, getThumbOffset());
        setThumbOffset(thumbOffset);

        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        a.recycle();
    }

    /**
     * Converts a drawable to a tiled version of itself. It will recursively
     * traverse layer and state list drawables.
     */
    private Drawable tileify(Drawable drawable, boolean clip) {

        if (drawable instanceof LayerDrawable) {
            LayerDrawable background = (LayerDrawable) drawable;
            final int N = background.getNumberOfLayers();
            Drawable[] outDrawables = new Drawable[N];

            for (int i = 0; i < N; i++) {
                int id = background.getId(i);
                outDrawables[i] = tileify(background.getDrawable(i),
                        (id == android.R.id.progress || id == android.R.id.secondaryProgress));
            }

            LayerDrawable newBg = new LayerDrawable(outDrawables);

            for (int i = 0; i < N; i++) {
                newBg.setId(i, background.getId(i));
            }

            return newBg;

        } else if (drawable instanceof StateListDrawable) {
            StateListDrawable in = (StateListDrawable) drawable;
            StateListDrawable out = new StateListDrawable();
//            int numStates = in.getStateCount();
//            for (int i = 0; i < numStates; i++) {
//                out.addState(in.getStateSet(i), tileify(in.getStateDrawable(i), clip));
//            }
            return out;

        } else if (drawable instanceof BitmapDrawable) {
            final Bitmap tileBitmap = ((BitmapDrawable) drawable).getBitmap();
            if (mSampleTile == null) {
                mSampleTile = tileBitmap;
            }

            final ShapeDrawable shapeDrawable = new ShapeDrawable(getDrawableShape());

            final BitmapShader bitmapShader = new BitmapShader(tileBitmap,
                    Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            shapeDrawable.getPaint().setShader(bitmapShader);

            return (clip) ? new ClipDrawable(shapeDrawable, Gravity.LEFT,
                    ClipDrawable.HORIZONTAL) : shapeDrawable;
        }

        return drawable;
    }

    Shape getDrawableShape() {
        final float[] roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
        return new RoundRectShape(roundedCorners, null, null);
    }


    /**
     * <p>Get the drawable used to draw the progress bar in
     * progress mode.</p>
     *
     * @return a {@link android.graphics.drawable.Drawable} instance
     *
     * @see #setProgressDrawable(android.graphics.drawable.Drawable)
     */
    public Drawable getProgressDrawable() {
        return mProgressDrawable;
    }

    /**
     * <p>Define the drawable used to draw the progress bar in
     * progress mode.</p>
     *
     * @param d the new drawable
     *
     * @see #getProgressDrawable()
     */
    public void setProgressDrawable(Drawable d) {
        boolean needUpdate;
        if (mProgressDrawable != null && d != mProgressDrawable) {
            mProgressDrawable.setCallback(null);
            needUpdate = true;
        } else {
            needUpdate = false;
        }

        if (d != null) {
            d.setCallback(this);
//            if (canResolveLayoutDirection()) {
//                d.setLayoutDirection(getLayoutDirection());
//            }

            // Make sure the ProgressBar is always tall enough
            int drawableHeight = d.getMinimumHeight();
            if (mMaxHeight < drawableHeight) {
                mMaxHeight = drawableHeight;
                requestLayout();
            }
        }
        mProgressDrawable = d;

        if (needUpdate) {
            updateDrawableBounds(getWidth(), getHeight());
            updateDrawableState();
            doRefreshValue(R.id.progress, mProgress, false, false);
        }
    }

    /**
     * Refreshes the value for the specific thumb
     * @param thumb the thumb which value is going to be changed
     * @param value the new value
     * @param fromUser if the request is coming form the user or the client
     */
    private synchronized void refreshValue(Thumb thumb, int value, boolean fromUser) {
        if(thumb == null || thumb.getThumb()==null) return;

        if (mUiThreadId == Thread.currentThread().getId()) {
            doRefreshValue(id, value, fromUser, true);
        } else {
            if (mRefreshProgressRunnable == null) {
                mRefreshProgressRunnable = new RefreshProgressRunnable();
            }

            final RefreshData rd = RefreshData.obtain(id, progress, fromUser);
            mRefreshData.add(rd);
            if (mAttached && !mRefreshIsPosted) {
                post(mRefreshProgressRunnable);
                mRefreshIsPosted = true;
            }
        }
    }

    private synchronized void refreshValue(int thumb, int value, boolean fromUser) {
        refreshValue(mThumbs.get(thumb), value, fromUser);
    }


    private synchronized void doRefreshValue(Thumb thumb, int value, boolean fromUser,
                                             boolean callBackToApp) {
        //float scale = mScaleMax > 0 ? (float) value / (float) mScaleMax : 0;
        if()
        thumb.setValue(value);
        final Drawable d = thumb.getRange();
        if (d != null) {
            Drawable progressDrawable = null;

            final int level = (int) (scale * MAX_LEVEL);
            (progressDrawable != null ? progressDrawable : d).setLevel(level);
        } else {
            invalidate();
        }

        if (callBackToApp) {
            onValueRefresh(scale, fromUser);
        }
    }

    private void updateDrawableBounds(int w, int h) {
        // onDraw will translate the canvas so we draw starting at 0,0.
        // Subtract out padding for the purposes of the calculations below.
        w -= getPaddingRight() + getPaddingLeft();
        h -= getPaddingTop() + getPaddingBottom();

        int right = w;
        int bottom = h;
        int top = 0;
        int left = 0;

        if (mProgressDrawable != null) {
            mProgressDrawable.setBounds(0, 0, right, bottom);
        }
    }

    private void updateDrawableState() {
        int[] state = getDrawableState();

        if (mProgressDrawable != null && mProgressDrawable.isStateful()) {
            mProgressDrawable.setState(state);
        }
    }

    private void initMultiSlider() {
        mScaleMin = 0;
        mScaleMax = 100;
        mMinWidth = 24;
        mMaxWidth = 48;
        mMinHeight = 24;
        mMaxHeight = 48;
        mThumbs =  new LinkedList<Thumb>();
        mThumbs.add(new Thumb().setMin(mScaleMin).setMax(mScaleMax).setValue(mScaleMin));
        mThumbs.add(new Thumb().setMin(mScaleMin).setMax(mScaleMax).setValue(mScaleMax));
    }

    /**
     * Sets the thumb that will be drawn at the end of the progress meter within the SeekBar.
     * <p>
     * If the thumb is a valid drawable (i.e. not null), half its width will be
     * used as the new thumb offset (@see #setThumbOffset(int)).
     *
     * @param thumb Drawable representing the thumb
     */
    public void setThumbs(Drawable thumb) {
        boolean needUpdate;
        // This way, calling setThumbs again with the same bitmap will result in
        // it recalcuating thumbOffset (if for example it the bounds of the
        // drawable changed)
        for(Thumb mThumb:mThumbs) {
            if (mThumb.getThumb() != null && thumb != mThumb.getThumb()) {
                mThumb.getThumb().setCallback(null);
                needUpdate = true;
            } else {
                needUpdate = false;
            }
            if (thumb != null) {
                thumb.setCallback(this);

                // Assuming the thumb drawable is symmetric, set the thumb offset
                // such that the thumb will hang halfway off either edge of the
                // progress bar.
                mThumb.setThumbOffset(thumb.getIntrinsicWidth() / 2);

                // If we're updating get the new states
                if (needUpdate &&
                        (thumb.getIntrinsicWidth() != mThumb.getThumb().getIntrinsicWidth()
                                || thumb.getIntrinsicHeight() != mThumb.getThumb().getIntrinsicHeight())) {
                    requestLayout();
                }
            }
            mThumb.setThumb(thumb);
            invalidate();
            if (needUpdate) {
                updateThumbPos(getWidth(), getHeight());
                if (thumb != null && thumb.isStateful()) {
                    // Note that if the states are different this won't work.
                    // For now, let's consider that an app bug.
                    int[] state = getDrawableState();
                    thumb.setState(state);
                }
            }
        }
    }

    /**
     * Return the drawable used to represent the scroll thumb - the component that
     * the user can drag back and forth indicating the current value by its position.
     *
     * @return The thumb at position pos
     */
    public Thumb getThumb(int pos) {
        return mThumbs.get(pos);
    }

    /**
     * Sets the amount of progress changed via the arrow keys.
     *
     * @param increment The amount to increment or decrement when the user
     *            presses the arrow keys.
     */
    public void setKeyProgressIncrement(int increment) {
        mKeyProgressIncrement = increment < 0 ? -increment : increment;
    }

    /**
     * Returns the amount of progress changed via the arrow keys.
     * <p>
     * By default, this will be a value that is derived from the max progress.
     *
     * @return The amount to increment or decrement when the user presses the
     *         arrow keys. This will be positive.
     */
    public int getKeyProgressIncrement() {
        return mKeyProgressIncrement;
    }

    @Override
    public synchronized void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (max != mScaleMax) {
            mScaleMax = max;
            postInvalidate();

            //check for max in all thumbs
            if (mProgress > max) {
                mProgress = max;
            }

            refreshProgress(R.id.progress, mProgress, false);
        }

        if ((mKeyProgressIncrement == 0) || (getMax() / mKeyProgressIncrement > 20)) {
            // It will take the user too long to change this via keys, change it
            // to something more reasonable
            setKeyProgressIncrement(Math.max(1, Math.round((float) getMax() / 20)));
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        for(Thumb thumb:mThumbs) {
            if(thumb.getThumb()!=null && who == thumb.getThumb()) return true;
        }
        return who == mProgressDrawable || super.verifyDrawable(who);
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        for(Thumb thumb:mThumbs) {
            if (thumb.getThumb() != null) thumb.getThumb().jumpToCurrentState();
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable != null) {
            progressDrawable.setAlpha(isEnabled() ? NO_ALPHA : (int) (NO_ALPHA * mDisabledAlpha));
        }
        for(Thumb thumb:mThumbs) {
            if (thumb.getThumb() != null && thumb.getThumb().isStateful()) {
                int[] state = getDrawableState();
                thumb.getThumb().setState(state);
            }
        }
    }

    void onValueRefresh(float scale, boolean fromUser) {
        Drawable thumb = mThumb;
        if (thumb != null) {
            setThumbPos(getWidth(), thumb, scale, Integer.MIN_VALUE);
            /*
             * Since we draw translated, the drawable's bounds that it signals
             * for invalidation won't be the actual bounds we want invalidated,
             * so just invalidate this whole view.
             */
            invalidate();
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateThumbPos(w, h);
    }

    private void updateThumbPos(int w, int h) {
        Drawable d = getProgressDrawable();
        Drawable thumb = mThumb;
        int thumbHeight = thumb == null ? 0 : thumb.getIntrinsicHeight();
        // The max height does not incorporate padding, whereas the height
        // parameter does
        int trackHeight = h - getPaddingTop() - getPaddingBottom();

        int max = getMax();
        float scale = max > 0 ? (float) getProgress() / (float) max : 0;

        if (thumbHeight > trackHeight) {
            if (thumb != null) {
                setThumbPos(w, thumb, scale, 0);
            }
            int gapForCenteringTrack = (thumbHeight - trackHeight) / 2;
            if (d != null) {
                // Canvas will be translated by the padding, so 0,0 is where we start drawing
                d.setBounds(0, gapForCenteringTrack,
                        w - getPaddingRight() - getPaddingLeft(), h - getPaddingBottom() - gapForCenteringTrack
                                - getPaddingTop());
            }
        } else {
            if (d != null) {
                // Canvas will be translated by the padding, so 0,0 is where we start drawing
                d.setBounds(0, 0, w - getPaddingRight() - getPaddingLeft(), h - getPaddingBottom()
                        - getPaddingTop());
            }
            int gap = (trackHeight - thumbHeight) / 2;
            if (thumb != null) {
                setThumbPos(w, thumb, scale, gap);
            }
        }
    }

    /**
     * @param gap If set to {@link Integer#MIN_VALUE}, this will be ignored and
     */
    private void setThumbPos(int w, Drawable thumb, float scale, int gap) {
        int available = w - getPaddingLeft() - getPaddingRight();
        int thumbWidth = thumb.getIntrinsicWidth();
        int thumbHeight = thumb.getIntrinsicHeight();
        available -= thumbWidth;

        // The extra space for the thumb to move on the track
        available += mThumbOffset * 2;

        int thumbPos = (int) (scale * available + 0.5f);

        int topBound, bottomBound;
        if (gap == Integer.MIN_VALUE) {
            Rect oldBounds = thumb.getBounds();
            topBound = oldBounds.top;
            bottomBound = oldBounds.bottom;
        } else {
            topBound = gap;
            bottomBound = gap + thumbHeight;
        }

        // Canvas will be translated, so 0,0 is where we start drawing
        final int left = (isLayoutRtl()) ? available - thumbPos : thumbPos;
        thumb.setBounds(left, topBound, left + thumbWidth, bottomBound);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(Thumb thumb:mThumbs) {
            if (thumb.getThumb() != null) {
                canvas.save();
                // Translate the padding. For the x, we need to allow the thumb to
                // draw in its extra space
                canvas.translate(getPaddingLeft() - thumb.getThumbOffset(), getPaddingTop());
                thumb.getThumb().draw(canvas);
                canvas.restore();
            }
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dw =  getMeasuredWidthAndState();
        int dh= 0;
        //get already measured height and add larger thumb into account
        int mh = getMeasuredHeightAndState();
        if(mThumb != null) {
            int maxThumbHeight = mThumb.getIntrinsicHeight() + getPaddingTop() + getPaddingBottom();

            dh = Math.max(maxThumbHeight, mh);

            setMeasuredDimension(resolveSizeAndState(dw, widthMeasureSpec, 0),
                    resolveSizeAndState(dh, heightMeasureSpec, 0));
        }
    }
    public boolean isInScrollingContainer() {
        ViewParent p = getParent();
        while (p != null && p instanceof ViewGroup) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) {
                return true;
            }
            p = p.getParent();
        }
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsUserSeekable || !isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInScrollingContainer()) {
                    mTouchDownX = event.getX();
                } else {
                    setPressed(true);
                    if (mThumb != null) {
                        invalidate(mThumb.getBounds()); // This may be within the padding region
                    }
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    attemptClaimDrag();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsDragging) {
                    trackTouchEvent(event);
                } else {
                    final float x = event.getX();
                    if (Math.abs(x - mTouchDownX) > mScaledTouchSlop) {
                        setPressed(true);
                        if (mThumb != null) {
                            invalidate(mThumb.getBounds()); // This may be within the padding region
                        }
                        onStartTrackingTouch();
                        trackTouchEvent(event);
                        attemptClaimDrag();
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    // Touch up when we never crossed the touch slop threshold should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                if (mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate(); // see above explanation
                break;
        }
        return true;
    }

    private void trackTouchEvent(MotionEvent event) {
        final int width = getWidth();
        final int available = width - getPaddingLeft() - getPaddingRight();
        int x = (int)event.getX();
        float scale;
        float progress = 0;
        if (isLayoutRtl()) {
            if (x > width - getPaddingRight()) {
                scale = 0.0f;
            } else if (x < getPaddingLeft()) {
                scale = 1.0f;
            } else {
                scale = (float)(available - x + getPaddingLeft()) / (float)available;
                progress = mTouchProgressOffset;
            }
        } else {
            if (x < getPaddingLeft()) {
                scale = 0.0f;
            } else if (x > width - getPaddingRight()) {
                scale = 1.0f;
            } else {
                scale = (float)(x - getPaddingLeft()) / (float)available;
                progress = mTouchProgressOffset;
            }
        }
        final int max = getMax();
        progress += scale * max;

        //setProgress((int) progress, true);
        setProgress((int) progress);
        onValueRefresh(scale, true);
    }

    /**
     * Tries to claim the user's drag motion, and requests disallowing any
     * ancestors from stealing events in the drag.
     */
    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    /**
     * This is called when the user has started touching this widget.
     */
    void onStartTrackingTouch() {
        mIsDragging = true;
    }

    /**
     * This is called when the user either releases his touch or the touch is
     * canceled.
     */
    void onStopTrackingTouch() {
        mIsDragging = false;
    }

    /**
     * Called when the user changes the seekbar's progress by using a key event.
     */
    void onKeyChange() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isEnabled()) {
            int progress = getProgress();
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (progress <= 0) break;
                    //setProgress(progress - mKeyProgressIncrement, true);
                    onKeyChange();
                    return true;

                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (progress >= getMax()) break;
                    //setProgress(progress + mKeyProgressIncrement, true);
                    onKeyChange();
                    return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(MultiSlider.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(MultiSlider.class.getName());

        if (isEnabled()) {
            final int progress = getProgress();
            if (progress > 0) {
                info.addAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
            }
            if (progress < getMax()) {
                info.addAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
        }
    }

    @Override
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        if(Build.VERSION.SDK_INT>=16) {
            if (super.performAccessibilityAction(action, arguments)) {
                return true;
            }
        }
        if (!isEnabled()) {
            return false;
        }
        final int progress = getProgress();
        final int increment = Math.max(1, Math.round((float) getMax() / 5));
        switch (action) {
            case AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD: {
                if (progress <= 0) {
                    return false;
                }
                //setProgress(progress - increment, true);
                onKeyChange();
                return true;
            }
            case AccessibilityNodeInfo.ACTION_SCROLL_FORWARD: {
                if (progress >= getMax()) {
                    return false;
                }
                //setProgress(progress + increment, true);
                onKeyChange();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        if(Build.VERSION.SDK_INT>=17){
            super.onRtlPropertiesChanged(layoutDirection);
        }

        int max = getMax();
        float scale = max > 0 ? (float) getProgress() / (float) max : 0;

        Drawable thumb = mThumb;
        if (thumb != null) {
            setThumbPos(getWidth(), thumb, scale, Integer.MIN_VALUE);
            /*
             * Since we draw translated, the drawable's bounds that it signals
             * for invalidation won't be the actual bounds we want invalidated,
             * so just invalidate this whole view.
             */
            invalidate();
        }
    }

    public boolean isLayoutRtl() {
        if(Build.VERSION.SDK_INT>=17){
            return (getLayoutDirection() == LAYOUT_DIRECTION_RTL);
        }

        return false;
    }
}
