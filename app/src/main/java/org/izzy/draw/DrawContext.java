package org.izzy.draw;

import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DrawContext {
    public float alpha = 1.0f;
    // 变换参数及保存栈
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

    // 平移
    public void translate(float x, float y) {
        matrix.preTranslate(x, y);
    }
    // 缩放
    public void scale(float scaleX, float scaleY) {
        matrix.preScale(scaleX, scaleY);
    }
    // 旋转
    public void rotate(float v) {
        matrix.preRotate(v);
    }

    private ArrayList<DrawItem> mItems = new ArrayList<>();
    // 清除绘制item
    public void resetDrawItems() {
        mItems.clear();
    }
    // 添加绘制item
    public void addDrawItem(DrawItem item) {
        mItems.add(item);
    }
    // 分发绘制item
    public void dispatchDraw(Canvas canvas) {
        if (mItems.size()==0) return;

        // 调整zOrder
        Collections.sort(mItems, new Comparator<DrawItem>() {
            @Override
            public int compare(DrawItem node0, DrawItem node1) {
                return node0.zOrder() - node1.zOrder();
            }
        });

        for (DrawItem item : mItems) {
            int save = canvas.save();
            canvas.concat(item.matrix());
            item.draw(canvas, this);
            canvas.restoreToCount(save);
        }
    }

    // 标记/数据集合
    private HashMap<String,Object> userData = new HashMap<>();
    // 修改标记
    public void setFlag(String name,boolean on) {
        if (on) {
            userData.put(name,name);
        } else {
            userData.remove(name);
        }
    }
    // 获取标记
    public boolean hasFlag(String name) {
        return userData.containsKey(name);
    }
    // 设置数据
    public void setUserData(String name,Object data) {
        if (data==null) {
            userData.remove(name);
        } else {
            userData.put(name,data);
        }
    }
    // 获取数据
    public Object getUserData(String name) {
        return userData.get(name);
    }
    public void clearUserData() {
        userData.clear();
    }
    public Object extra; // 额外数据
}
