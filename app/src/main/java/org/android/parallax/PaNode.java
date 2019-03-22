package org.android.parallax;

// 基础类 -- View
// 属性：中心位置/大小/缩放/zOrder
public class PaNode {
    public float centerX = 0;
    public float centerY = 0;
    public float halfX = 0;
    public float halfY = 0;
    public float scale = 1;
    public int z = 0;

    public void dispatchDraw() {

    }
}
