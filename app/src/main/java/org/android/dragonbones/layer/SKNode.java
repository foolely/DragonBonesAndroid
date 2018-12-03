package org.android.dragonbones.layer;

import android.animation.ValueAnimator;

import org.android.dragonbones.parser.ShowEffect;
import org.android.dragonbones.parser.Transform;

import java.util.ArrayList;

// 显示节点
// bone 有坐标/偏移/缩放等属性
// slot 没有坐标等属性 有z属性
public class SKNode implements SKParent {
    public String nodeType = "base";
    public boolean isShow = true;
    public int z = 0; // slot节点的属性 其他节点都是0
    public String name;
    protected Transform mTransform; // 平移变换对象 slot节点没有该属性
    protected InnerEffect mEffect; // 显示效果对象 slot节点专有
    protected SKAnimation mAnimation; // 动画对象
    protected static class InnerEffect extends ShowEffect {
        public float alphaSave = -1;
        public void apply(SKContext ctx) {
            alphaSave = ctx.alpha;
            ctx.alpha *= alpha;
        }
        public void restore(SKContext ctx) {
            ctx.alpha = alphaSave;
        }
    }

    // 父节点
    protected SKParent parent;
    // child树结构
    protected ArrayList<SKNode> subNodes = new ArrayList<>();

//    public void _log(String txt) {
//        android.util.Log.e("SKNode", name+"@"+nodeType+":"+txt);
//    }

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

    // 设置变换参数 bone/display
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
    // 设置显示效果 slot
    public void setEffect(ShowEffect dst) {
        if (mEffect == null) {
            mEffect = new InnerEffect();
        }

        if (!mEffect.isEqual(dst)) {
            mEffect.set(dst);
            requestDraw();
        }
    }

//    public void draw(Canvas canvas, SKContext context) {
//
//    }
//    protected void onMeasure(SKContext ctx) {
//    }
//    public void dispatchMeasure(SKContext ctx) {
//
//    }

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
            mEffect.apply(ctx);
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
        if (mEffect !=null) {
            mEffect.restore(ctx);
        }
    }
//    public void dispatchDraw(Canvas canvas, SKContext ctx) {
//        int save = -1;
//        if (mTransform!=null) {
//            save = ctx.apply(mTransform);
//        }
//        // 先画自己
//        draw(canvas, ctx);
//
//        Collections.sort(subNodes, new Comparator<SKNode>() {
//            @Override
//            public int compare(SKNode node0, SKNode node1) {
//                return node0.z - node1.z;
//            }
//        });
//
//        for (SKNode node : subNodes) {
//            if (node.isShow) {
//                node.dispatchDraw(canvas, ctx);
//            }
//        }
//
//        if (save != -1) {
//            canvas.restoreToCount(save);
//        }
//    }

    // 设置节点上的动画
    public void setAnimation(SKAnimation ani) {
        mAnimation = ani;
    }
    // 清除动画
    public void clearAnimations() {
        mAnimation = null;
        for (SKNode item : subNodes) {
            item.clearAnimations();
        }
    }
    // 更新动画参数
    public void calcAnimations(int frameIdx) {
        if (mAnimation != null) {
            mAnimation.update(this, frameIdx);
        }

        for (SKNode item : subNodes) {
            item.calcAnimations(frameIdx);
        }
    }
}
