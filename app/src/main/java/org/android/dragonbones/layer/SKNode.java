package org.android.dragonbones.layer;

import android.animation.ValueAnimator;

import org.android.dragonbones.parser.Transform;

import java.util.ArrayList;

// 显示节点
public class SKNode implements SKParent {
    public String name;
    public float x,y;
    public float scaleX = 1;
    public float scaleY = 1;
    public float skX,skY; // 倾斜

    public SKParent parent;
    // child树结构
    public ArrayList<SKNode> subNodes = new ArrayList<>();

    @Override
    public void addChild(SKNode child) {
        child.parent = this;
        subNodes.add(child);
    }

    @Override
    public void removeChild(SKNode child) {
        subNodes.remove(child);
        child.parent = null;
        ValueAnimator v;
    }

    public void removeFromParent() {
        if (parent!=null) {
            parent.removeChild(this);
        }
    }

    // 设置参数
    public void setTransform(Transform dst) {
    }
}
