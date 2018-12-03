package org.android.dragonbones.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;

import org.android.dragonbones.parser.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SKContext {
    public Matrix matrix = new Matrix();
    public int z; // 当前z轴高度
    public float alpha = 1.0f;
    public int frameIndex = 0; // 当前帧序列
    public boolean isShowName = false;

    private ArrayList<SKDraw> mItems = new ArrayList<>();

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

    public void translate(float x, float y) {
        matrix.preTranslate(x, y);
    }

    public void scale(float scaleX, float scaleY) {
        matrix.preScale(scaleX, scaleY);
    }

    public void rotate(float v) {
        matrix.preRotate(v);
    }

    public void resetDrawItems() {
        mItems.clear();
    }
    public void dispatchDraw(Canvas canvas) {
        if (mItems.size()==0) return;

        // 调整zOrder
        Collections.sort(mItems, new Comparator<SKDraw>() {
            @Override
            public int compare(SKDraw node0, SKDraw node1) {
                return node0.zOrder() - node1.zOrder();
            }
        });

        for (SKDraw item : mItems) {
            int save = canvas.save();
            canvas.concat(item.matrix());
            item.draw(canvas, this);
            canvas.restoreToCount(save);
        }
    }
    public void addDrawItem(SKDraw item) {
        mItems.add(item);
    }

    // 应用变换
    public int apply(Transform t) {
        int save = -1;
        if (t.x!=0||t.y!=0) {
            save = save();
            translate(t.x,t.y);
        }
        if (t.scX!=1||t.scY!=1) {
            if (save==-1) {
                save = save();
            }
            scale(t.scX, t.scY);
        }
        if (t.skX != 0 || t.skY != 0) {
            if (save==-1) {
                save = save();
            }
            rotate((t.skX+t.skY)*0.5f);
        }
        return save;
    }
}
