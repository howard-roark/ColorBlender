package com.cs390h.ColorBlender;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by matthewmcguire on 10/17/14.
 * <p/>
 * This class was copied from stackoverflow, code has been slightly modified
 * http://stackoverflow.com/questions/18395064/android-vertical-seek-bar-
 * like-google-play-music-app
 */
public class VerticalSeekBar extends SeekBar {

    protected OnSeekBarChangeListener changeListener;
    protected int x, y, z, w;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected synchronized void onSizeChanged(int w, int h, int oldw,
                                              int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);

        this.x = w;
        this.y = h;
        this.z = oldw;
        this.w = oldh;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);

        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean seekBarTouched;
        if (!isEnabled()) {
            seekBarTouched = false;
        } else {
            seekBarTouched = true;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setSelected(true);
                    setPressed(true);
                    if (changeListener != null)
                        changeListener.onStartTrackingTouch(this);
                    break;
                case MotionEvent.ACTION_UP:
                    setSelected(false);
                    setPressed(false);
                    if (changeListener != null)
                        changeListener.onStopTrackingTouch(this);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int progress =
                            getMax() - (int)
                                    (getMax() * event.getY() / getHeight());

                    if (progress > 100) {
                        progress = 100;
                    } else if (progress < 0) {
                        progress = 0;
                    }

                    setProgress(progress);
                    onSizeChanged(getWidth(), getHeight(), 0, 0);
                    if (changeListener != null)
                        changeListener.onProgressChanged(this, progress, true);
                    break;

                case MotionEvent.ACTION_CANCEL:
                    break;
            }
        }
        return seekBarTouched;
    }

    @Override
    public synchronized void setOnSeekBarChangeListener
            (SeekBar.OnSeekBarChangeListener listener) {
        changeListener = listener;
    }

    @Override
    public synchronized void setProgress(int progress) {
        if (progress >= 0)
            super.setProgress(progress);

        else
            super.setProgress(0);
        onSizeChanged(x, y, z, w);
        if (changeListener != null)
            changeListener.onProgressChanged(this, progress, false);
    }
}