package org.android.dragonbones.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

// @root/Armature/Animation
// 动画
public class Ka_Animation extends JsonBean {
    public static final String kPlayTimes = "playTimes";
    public static final String kEvent = "event";

    public static class Frame {
        public boolean tweenEasing;
        public String event;
        public float duration;
    }
    // todo:
    public static class FFD {

    }

    public int playTimes;
    public float duration;
    public ArrayList<Kaa_Slot> slots = new ArrayList<>();
    public ArrayList<Kaa_Bone> bones = new ArrayList<>();
    public ArrayList<Frame> frames = new ArrayList<>();
    public FFD ffd; // todo:

    public static Ka_Animation fromJson(JSONObject json, Map<String,Transform> boneTransforms, int frameRate) {
        Ka_Animation bean = new Ka_Animation();

        _name(json, bean);
        bean.playTimes = _int(json, kPlayTimes, 0);
        bean.duration = 1.0f / frameRate * _int(json, kDuration, 0);

        JSONArray arr = json.optJSONArray(kSlot);
        if (arr!=null) {
            for (int i = 0; i < arr.length(); ++i) {
                bean.slots.add(Kaa_Slot.fromJson(arr.optJSONObject(i), frameRate));
            }
        }

        arr = json.optJSONArray(kBone);
        if (arr!=null) {
            for (int i = 0; i < arr.length(); ++i) {
                bean.bones.add(Kaa_Bone.fromJson(arr.optJSONObject(i), boneTransforms, frameRate));
            }
        }

        arr = json.optJSONArray(kFrame);
        if (arr!=null) {
            float cDuration = 0;
            float duration = 0;
            boolean tweenEasing = false;
            for (int i = 0; i< arr.length(); ++i) {
                JSONObject item = arr.optJSONObject(i);
                Frame frame = new Frame();
                frame.duration = duration;
                frame.tweenEasing = tweenEasing;
                frame.event = _str(item, kEvent, null);

                cDuration += duration;

                // duration原始时长的计数单位是帧数 ?
                duration = 1.0f/frameRate * _int(item, kDuration, 0);
                tweenEasing = _int(item, kTweenEasing, -1)!=-1;

                bean.frames.add(frame);
            }

            // 最后一帧又回到了第1帧 为了循环么?
            if (bean.frames.size()>0 && cDuration < bean.duration) {
                Frame frame = new Frame();
                frame.tweenEasing = false;
                frame.event = null;
                frame.duration = duration - cDuration;
                bean.frames.add(frame);
            }
        }

        return bean;
    }
}
