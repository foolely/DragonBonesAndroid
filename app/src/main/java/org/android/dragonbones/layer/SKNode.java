package org.android.dragonbones.layer;

import android.animation.ValueAnimator;
import android.graphics.Canvas;

import org.android.dragonbones.parser.ShowEffect;
import org.android.dragonbones.parser.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// 显示节点
// bone 有坐标/偏移/缩放等属性
// slot 没有坐标等属性 有z属性
public class SKNode implements SKParent {
    public String nodeType = "base";
    public boolean isShow = true;
    public int z = 0; // slot节点的属性
    public String name;
    private Transform mTransform; // slot节点没有该属性
    private ShowEffect mEffect; // slot节点专有
//    public float x = 0,y = 0;
//    public float scaleX = 1;
//    public float scaleY = 1;
//    public float skX = 0,skY = 0; // 倾斜

    public SKParent parent;
    protected SKAnimation mAnimation;
    // child树结构
    public ArrayList<SKNode> subNodes = new ArrayList<>();

    public void _log(String txt) {
        android.util.Log.e("SKNode", name+"@"+nodeType+":"+txt);
    }

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

    // 设置变换参数
    public void setTransform(Transform dst) {
        if (mTransform==null) {
            mTransform = new Transform();
        }
        if (!mTransform.isEqual(dst)) {
            mTransform.set(dst);
            requestDraw();
        }
    }
    @Override
    public void requestDraw() {
        if (parent!=null) {
            parent.requestDraw();
        }
    }
    // 设置显示效果
    public void setEffect(ShowEffect dst) {
        if (mEffect == null) {
            mEffect = new ShowEffect();
        }

        if (!mEffect.isEqual(dst)) {
            mEffect.set(dst);
        }
    }

    public void draw(Canvas canvas, SKContext context) {

    }

    protected void onLayout(SKContext ctx) {
    }
    public void dispatchLayout(SKContext ctx) {
        // 应用动画
        int save = -1;
        if (mTransform!=null) {
            save = ctx.apply(mTransform);
        }

        if (mEffect!=null) {
            // todo:需要处理alpha
            // 处理显示哪个帧
            int disIdx = mEffect.displayIndex;
            for (int i = 0; i < subNodes.size(); ++i) {
                SKNode node = subNodes.get(i);
                node.isShow = disIdx == i;
            }
        }
        // 先画自己
        onLayout(ctx);

        if (subNodes.size()>0) {
            ctx.z += z;
            for (SKNode node : subNodes) {
                if (node.isShow) {
                    node.dispatchLayout(ctx);
                }
            }
            ctx.z -= z;
        }

        if (save != -1) {
            ctx.restoreToCount(save);
        }
    }
    public void dispatchDraw(Canvas canvas, SKContext ctx) {
        int save = -1;
        if (mTransform!=null) {
            save = ctx.apply(mTransform);
        }
        // 先画自己
        draw(canvas, ctx);

        Collections.sort(subNodes, new Comparator<SKNode>() {
            @Override
            public int compare(SKNode node0, SKNode node1) {
                return node0.z - node1.z;
            }
        });

        for (SKNode node : subNodes) {
            if (node.isShow) {
                node.dispatchDraw(canvas, ctx);
            }
        }

        if (save != -1) {
            canvas.restoreToCount(save);
        }
    }

    public void setAnimation(SKAnimation ani) {
        mAnimation = ani;
    }
    // 清楚动画
    public void clearAnimations() {
        mAnimation = null;
        for (SKNode item : subNodes) {
            item.clearAnimations();
        }
    }
    public void calcAnimations(int frameIdx) {
        if (mAnimation != null) {
            mAnimation.update(this, frameIdx);
        }

        for (SKNode item : subNodes) {
            item.calcAnimations(frameIdx);
        }
    }
}
