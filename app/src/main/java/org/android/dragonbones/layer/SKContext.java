package org.android.dragonbones.layer;

import org.android.dragonbones.parser.Transform;
import org.izzy.draw.DrawContext;

// extra context
public class SKContext {
    public int z; // 当前z轴高度
    public int frameIndex = 0; // 当前帧序列

    // 应用变换参数
    public int apply(DrawContext ctx, Transform t) {
        int save = -1;
        if (t.x!=0||t.y!=0) {
            save = ctx.save();
            ctx.translate(t.x,t.y);
        }
        if (t.scX!=1||t.scY!=1) {
            if (save==-1) {
                save = ctx.save();
            }
            ctx.scale(t.scX, t.scY);
        }
        if (t.skX != 0 || t.skY != 0) {
            if (save==-1) {
                save = ctx.save();
            }
            ctx.rotate((t.skX+t.skY)*0.5f);
        }
        return save;
    }
}
