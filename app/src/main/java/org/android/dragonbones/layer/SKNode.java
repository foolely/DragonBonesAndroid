package org.android.dragonbones.layer;

import org.android.dragonbones.parser.ShowEffect;
import org.android.dragonbones.parser.Transform;
import org.izzy.draw.DrawContext;
import org.izzy.view.Node;
import org.izzy.view.NodeGroup;

// 显示节点
// bone 有坐标/偏移/缩放等属性
// slot 没有坐标等属性 有z属性
public class SKNode extends NodeGroup {
    public String nodeType = "base";
    public boolean isShow = true;
    public int z = 0; // slot节点的属性 其他节点都是0
    public String name;
    protected Transform mTransform; // 平移变换对象 slot节点没有该属性
    protected InnerEffect mEffect; // 显示效果对象 slot节点专有
    protected SKAnimation mAnimation; // 动画对象
    protected static class InnerEffect extends ShowEffect {
        public float alphaSave = -1;
        public void apply(DrawContext ctx) {
            alphaSave = ctx.alpha;
            ctx.alpha *= alpha;
        }
        public void restore(DrawContext ctx) {
            ctx.alpha = alphaSave;
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

    public void dispatchLayout(DrawContext ctx) {
        // 应用动画
        int save = -1;
        SKContext sk = (SKContext) ctx.extra;
        if (mTransform!=null) {
            save = sk.apply(ctx,mTransform);
        }

        if (mEffect!=null) {
            // todo:需要处理alpha
            // 处理显示哪个帧
            int disIdx = mEffect.displayIndex;
            for (int i = 0; i < subNodes.size(); ++i) {
                SKNode node = (SKNode) subNodes.get(i);
                node.isShow = disIdx == i;
            }
            mEffect.apply(ctx);
        }
        // 先画自己
        onLayout(ctx);

        if (subNodes.size()>0) {
            sk.z += z;
            for (Node item : subNodes) {
                SKNode node = (SKNode) item;
                if (node.isShow) {
                    node.dispatchLayout(ctx);
                }
            }
            sk.z -= z;
        }

        if (save != -1) {
            ctx.restoreToCount(save);
        }
        if (mEffect !=null) {
            mEffect.restore(ctx);
        }
    }

    // 设置节点上的动画
    public void setAnimation(SKAnimation ani) {
        mAnimation = ani;
    }
    // 清除动画
    public void clearAnimations() {
        mAnimation = null;
        for (Node item : subNodes) {
            SKNode node = (SKNode) item;
            node.clearAnimations();
        }
    }
    // 更新动画参数
    public void calcAnimations(int frameIdx) {
        if (mAnimation != null) {
            mAnimation.update(this, frameIdx);
        }

        for (Node item : subNodes) {
            SKNode node = (SKNode) item;
            node.calcAnimations(frameIdx);
        }
    }
}
