package org.izzy.draw;

import android.graphics.Canvas;
import android.graphics.Matrix;

// 支持绘制的节点
public interface DrawItem {
    public void draw(Canvas canvas, DrawContext ctx);
    public int zOrder();
    public Matrix matrix();
}
