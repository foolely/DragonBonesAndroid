package org.android.dragonbones.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.izzy.draw.DrawContext;
import org.izzy.draw.DrawItem;

// 绘制图片的node 目前唯一的显示单元
public class DisplayNode extends SKNode implements DrawItem {
    protected Paint mPaint = new Paint();
    public Bitmap mImage;
    private Matrix mMatrix = new Matrix();
    private int zOrder = 0;
    public DisplayNode() {
        mPaint.setAntiAlias(true);
    }
    @Override
    public int zOrder() {
        return zOrder;
    }

    @Override
    public Matrix matrix() {
        return mMatrix;
    }

    // 获取当前的布局位置
    @Override
    protected void onLayout(DrawContext ctx) {
        SKContext sk = (SKContext) ctx.extra;
        mMatrix.set(ctx.matrix);
        zOrder = sk.z + z;
        mPaint.setAlpha((int)(255*ctx.alpha));
        ctx.addDrawItem(this);
    }

    // 绘制吧
    @Override
    public void draw(Canvas canvas, DrawContext ctx) {
        if (ctx.hasFlag("isShowName")) {
            mPaint.setColor(0xff0000ff);
            canvas.drawText(name, 0, 0, mPaint);
        }
        // 位置中心对应图片中心
        if (mImage!=null) {
            float left = mImage.getWidth() * -0.5f;
            float top = mImage.getHeight() * -0.5f;
            canvas.drawBitmap(mImage, left, top, mPaint);
        }
    }
}
