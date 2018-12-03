package org.android.dragonbones.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;

// 支持绘制的节点
public interface SKDraw {
    public void draw(Canvas canvas, SKContext ctx);
    public int zOrder();
    public Matrix matrix();
}
