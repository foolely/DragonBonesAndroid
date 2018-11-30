package org.android.dragonbones.layer;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ArmatureDrawable extends Drawable {
    public ArmatureNode mNode;
    public SKNode.DrawContext mDrawContext = new SKNode.DrawContext();
    private Paint mPaint = new Paint();
    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mNode!=null) {
            mPaint.setColor(0xffff0000);
            mPaint.setStyle(Paint.Style.STROKE);
            Rect bounds = getBounds();
            canvas.drawRect(bounds, mPaint);
            canvas.translate(bounds.width()/2, bounds.height()/2 );
            mNode.dispatchDraw(canvas, mDrawContext);
            mNode._log("out offset:x="+mDrawContext.x+",y="+mDrawContext.y);
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
}
