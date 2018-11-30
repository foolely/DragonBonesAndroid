package org.android.dragonbones.layer;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Matrix;

import org.android.dragonbones.parser.Transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

// 显示节点
// bone 有坐标/偏移/缩放等属性
// slot 没有坐标等属性 有z属性
public class SKNode implements SKParent {
    public static class DrawContext {
        public Matrix matrix = new Matrix();
        private ArrayList<Matrix> stack = new ArrayList<>();
        private int stackCount = 0;
        // 保存
        public int save() {
            Matrix backup = null;
            if (stack.size()>stackCount) {
                backup = stack.get(stackCount);
            } else {
                backup = new Matrix();
                stack.add(backup);
            }
            backup.set(matrix);
            return stackCount++;
        }
        // 还原
        public int restore() {
            if (stackCount>0) {
                matrix.set(stack.get(--stackCount));
            }
            return stackCount;
        }
        // 还原到计数
        public int restoreToCount(int save) {
            if (save<0) return stackCount;
            if (save<stackCount) {
                matrix.set(stack.get(save));
                stackCount = save;
            }
            return stackCount;
        }
        public float x,y;
        public float progress;
        public long curTick;
        public int totalFrames = 1;
        public int frameIndex = 0; // 当前帧序列
        public int needNextFrames; // 需要绘制下一帧
    }
    public String nodeType = "base";
    public boolean isShow = true;
    public int z = 0;
    public String name;
    public float x = 0,y = 0;
    public float scaleX = 1;
    public float scaleY = 1;
    public float skX = 0,skY = 0; // 倾斜

    public SKParent parent;
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

    // 设置参数
    public void setTransform(Transform dst) {
        x = dst.x;
        y = dst.y;
        scaleX = dst.scX;
        scaleY = dst.scY;
        skX = dst.skX;
        skY = dst.skY;
    }

    public void draw(Canvas canvas, DrawContext context) {

    }
    public void dispatchDraw(Canvas canvas, DrawContext context) {
        int save = -1;
        if (x!=0||y!=0) {
            save = canvas.save();
            canvas.translate(x,y);
//            save = context.save();
//            context.matrix.preTranslate(x, y);
        }
        if (scaleX!=1||scaleY!=1) {
            if (save==-1) {
                save = canvas.save();
            }
            canvas.scale(scaleX, scaleY);
        }
        if (skX != 0 || skY != 0) {
            if (save==-1) {
                save = canvas.save();
            }
            canvas.rotate((skX+skY)*0.5f);
        }
        context.x += x;
        context.y += y;
        _log("x="+context.x+",y="+context.y);
        // 先画自己
        draw(canvas, context);

        Collections.sort(subNodes, new Comparator<SKNode>() {
            @Override
            public int compare(SKNode node0, SKNode node1) {
                return node1.z - node0.z;
            }
        });

        for (SKNode node : subNodes) {
            if (node.isShow) {
                node.dispatchDraw(canvas, context);
            }
        }

        context.x -= x;
        context.y -= y;
        if (save != -1) {
            canvas.restoreToCount(save);
        }
    }
}
