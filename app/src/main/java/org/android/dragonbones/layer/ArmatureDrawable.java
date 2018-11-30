package org.android.dragonbones.layer;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ArmatureDrawable extends Drawable implements ValueAnimator.AnimatorUpdateListener {
    private ValueAnimator mFrameAnimator;
    public ArmatureNode mNode;
    public SKContext mDrawContext = new SKContext();
    private Paint mPaint = new Paint();
    private PaintFlagsDrawFilter mAntiAlias = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mNode!=null) {
            mPaint.setColor(0xffff0000);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.setDrawFilter(mAntiAlias);
            Rect bounds = getBounds();
            canvas.drawRect(bounds, mPaint);
            canvas.translate(bounds.width()/2, bounds.height()/2 );
            mDrawContext.resetDrawItems();
            mNode.dispatchLayout(mDrawContext);
            mDrawContext.dispatchDraw(canvas);
//            mNode.dispatchDraw(canvas, mDrawContext);
//            mNode._log("out offset:x="+mDrawContext.x+",y="+mDrawContext.y);
            canvas.translate(-bounds.width()/2, -bounds.height()/2 );
        }
    }

    @Override
    public void setAlpha(int i) {
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
        super.setBounds(bounds);
    }

    public void play(String name, boolean repeat) {
        if (mNode == null) return;
        int maxFrames = mNode.playAnimation(name, repeat);

        if (mFrameAnimator !=null) {
            mFrameAnimator.cancel();
            mFrameAnimator = null;
        }
        mFrameAnimator = ValueAnimator.ofInt(0, maxFrames);
        mFrameAnimator.setDuration(1000*maxFrames/mNode.frameRate);
        mFrameAnimator.setRepeatCount(Integer.MAX_VALUE);
        mFrameAnimator.addUpdateListener(this);

        mFrameAnimator.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        Integer value = (Integer) valueAnimator.getAnimatedValue();
        if (value != mDrawContext.frameIndex) {
            mDrawContext.frameIndex = value;
            mNode.calcAnimations(mDrawContext.frameIndex);
            if (mNode.clearNeedDraw()) {
                invalidateSelf();
            }
        }
    }
}
