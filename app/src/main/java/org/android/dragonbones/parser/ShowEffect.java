package org.android.dragonbones.parser;

public class ShowEffect {
    public float alpha = 1.0f;
    public int displayIndex = -1;

    // 复制
    public void set(ShowEffect dst) {
        alpha = dst.alpha;
        displayIndex = dst.displayIndex;
    }

    // 中间采样设置
    public void set(ShowEffect beg, ShowEffect end, float progress) {
        if (progress==0) {
            set(beg);
        } else if (progress==1) {
            set(end);
        } else {
            float rate = 1 - progress;
            alpha = beg.alpha * rate + end.alpha * progress;
            displayIndex = beg.displayIndex;
        }
    }

    public boolean isEqual(ShowEffect dst) {
        return alpha == dst.alpha && displayIndex == dst.displayIndex;
    }
}
