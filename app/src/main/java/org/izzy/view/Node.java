package org.izzy.view;

import org.izzy.draw.DrawContext;

public class Node {
    public float centerX = 0;
    public float centerY = 0;
    public float halfX = 0;
    public float halfY = 0;

    // 父节点
    protected NodeParent parent;

    public void removeFromParent() {
        if (parent!=null) {
            parent.removeChild(this);
        }
    }

    protected void onLayout(DrawContext ctx) {
    }

    public void dispatchLayout(DrawContext ctx) {
    }
}
