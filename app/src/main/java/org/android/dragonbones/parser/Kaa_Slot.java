package org.android.dragonbones.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

// @root/Armature/Animation/Slot
// 动画节点
public class Kaa_Slot extends JsonBean {
    public static final String kAM = "aM";

    // @root/Armature/Animation/Slot/Frame
    // 帧图
//    public static class Frame extends Kaa_Frame {
//        public float alpha;
//        public int displayIndex;
//        public float duration; // 解析到这里的意义不是这一个frame的时长 而是显示起点 上一帧的时长
//    }

    public ArrayList<Kaa_Frame> frames = new ArrayList<>();

    public static Kaa_Slot fromJson(JSONObject json, int frameRate) {
        Kaa_Slot bean = new Kaa_Slot();

        _name(json, bean);
        JSONArray arr = (json!=null)?json.optJSONArray(kFrame):null;
        if (arr!=null) {
//            int frameCount = 0;
//            float duration = 0;
//            boolean tweenEasing = false;
            for (int i = 0; i< arr.length(); ++i) {
                JSONObject item = arr.optJSONObject(i);
                Kaa_Frame frame = new Kaa_Frame();
//                frame.duration = duration;
//                frame.frameCount = frameCount;
//                frame.tweenEasing = tweenEasing;
                frame.frameCount = _int(item, kDuration, 0);
                frame.tweenEasing = _int(item, kTweenEasing, -1)!=-1;
                ShowEffect effect = frame.effect();
                effect.alpha = _int(item, kAM, 100) / 100f;
                effect.displayIndex = _int(item, kDisplayIndex, 0);

                // duration原始时长的计数单位是帧数 ?
//                duration = 1.0f/frameRate * _int(item, kDuration, 0);
//                frameCount = _int(item, kDuration, 0);
//                tweenEasing = _int(item, kTweenEasing, -1)!=-1;

                bean.frames.add(frame);
            }

            // 最后一帧又回到了第1帧 为了循环么?
//            if (duration!=0) {
//            if (frameCount>0) {
//                Frame frame = new Frame();
//                Frame first = bean.frames.get(0);
//                frame.tweenEasing = false;
//                frame.alpha = first.alpha;
//                frame.displayIndex = first.displayIndex;
//                frame.duration = duration;
//                frame.frameCount = frameCount;
//                bean.frames.add(frame);
//            }
        }

        return bean;
    }
}
