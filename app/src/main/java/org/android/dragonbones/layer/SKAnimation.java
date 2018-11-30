package org.android.dragonbones.layer;

import org.android.dragonbones.parser.Kaa_Frame;
import org.android.dragonbones.parser.ShowEffect;
import org.android.dragonbones.parser.Transform;

import java.util.ArrayList;

// 动画组合对象
// 思路：使用一个value变化器 控制一个序列动画item的流转
// 动画item有自己的终点状态 起点状态需要等前面的动画完成后才能确定
public abstract class SKAnimation {
    // 更新节点信息 返回是否有下一帧数据
    public boolean update(SKNode node, int frameIdx) {
        boolean bNext = isRepeat || frameIdx < totalFrames;
        frameIdx = searchFrame(frames, frameIdx, dst);

        if (frameIdx < 0) return false;

        if (!dst[0].tweenEasing) {
            setNode(node, dst[0], null, 0);
        } else {
            int frameCount = dst[0].frameCount;
            if (frameCount == 0) {
                frameCount = 1;
            }
            setNode(node, dst[0], dst[1], 1.0f*frameIdx/frameCount);
        }

        return bNext;
    }

    protected abstract void setNode(SKNode node, Kaa_Frame beg, Kaa_Frame end, float progress);

    public String name;
    protected boolean isRepeat = false;
    protected ArrayList<Kaa_Frame> frames = new ArrayList<>();
    protected Kaa_Frame[] dst = new Kaa_Frame[2];

    public void play(boolean repeat) {
        isRepeat = repeat;
    }

    protected int totalFrames = 0;
    protected static int sumFrames(ArrayList<Kaa_Frame> frames) {
        int count = 0;
        for (Kaa_Frame frame : frames) {
            count += frame.frameCount;
        }
        return count;
    }
    // todo: 需要优化frameCount=0的情况
    public int searchFrame(ArrayList<Kaa_Frame> frames, int frameIdx, Kaa_Frame[] outFrames) {
        if (totalFrames<1) return -1;
        if (totalFrames<2) {
            outFrames[0] = frames.get(0);
            outFrames[1] = outFrames[0];
            return 0; // 只有一帧
        }

        frameIdx = frameIdx%totalFrames;
        for (int i = 0; i < frames.size(); ++i) {
            Kaa_Frame frame = frames.get(i);
            if (frameIdx < frame.frameCount) {
                outFrames[0] = frame;
                i = (i+1) % frames.size();
                if (i == frames.size()) i = 0;
                outFrames[1] = frames.get(i);
                return frameIdx;
            } else {
                frameIdx -= frame.frameCount;
            }
        }
        return -1;
    }
    // bone有位移/缩放等变换信息
    public static class Bone extends SKAnimation {
        private Transform transform = new Transform();
        @Override
        protected void setNode(SKNode node, Kaa_Frame beg, Kaa_Frame end, float progress) {
            transform.set(beg.transform(), end!=null?end.transform():null,progress);
            node.setTransform(transform);
        }
    }

    // slot有显示/隐藏 淡入淡出信息
    public static class Slot extends SKAnimation {
        private ShowEffect effect = new ShowEffect();
        @Override
        protected void setNode(SKNode node, Kaa_Frame beg, Kaa_Frame end, float progress) {
            effect.set(beg.effect(), end!=null?end.effect():null, progress);
            node.setEffect(effect);
        }
    }

    protected void init(String n, ArrayList<Kaa_Frame> frs) {
        name = n;
        frames = frs;
        totalFrames = SKAnimation.sumFrames(frames);
    }
    public static SKAnimation createBoneAni(String name, ArrayList<Kaa_Frame> frames) {
        Bone ani = new Bone();
        ani.init(name, frames);
        return ani;
    }
    public static SKAnimation createSlotAni(String name, ArrayList<Kaa_Frame> frames) {
        Slot ani = new Slot();
        ani.init(name, frames);
        return ani;
    }
}
