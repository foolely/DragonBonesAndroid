package org.android.dragonbones.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

// @root/Armature/Animation/Bone
// 动画里的bone
public class Kaa_Bone extends JsonBean {

    public static class Frame {
        public boolean tweenEasing;
        public Transform transform;
        public float duration; // 解析到这里的意义不是这一个frame的时长 而是显示起点 上一帧的时长
    }

    public ArrayList<Frame> frames = new ArrayList<>();

    public static Kaa_Bone fromJson(JSONObject json, Map<String,Transform> boneTransforms, int frameRate) {
        Kaa_Bone bean = new Kaa_Bone();

        _name(json, bean);

        JSONArray arr = (json!=null)?json.optJSONArray(kFrame):null;
        if (arr!=null) {
            float duration = 0;
            boolean tweenEasing = false;
            for (int i = 0; i< arr.length(); ++i) {
                JSONObject item = arr.optJSONObject(i);

                Frame frame = new Frame();
                frame.transform = Transform.fromJson(_obj(item, kTransform), boneTransforms.get(bean.name));
                frame.duration = duration;
                frame.tweenEasing = tweenEasing;

                // duration原始时长的计数单位是帧数 ?
                duration = 1.0f/frameRate * _int(item, kDuration, 0);
                tweenEasing = _int(item, kTweenEasing, -1)!=-1;

                bean.frames.add(frame);
            }

            // 最后一帧又回到了第1帧 为了循环么?
            if (duration!=0) {
                Frame frame = new Frame();
                Frame first = bean.frames.get(0);
                frame.transform = first.transform;
                frame.tweenEasing = false;
                frame.duration = duration;
                bean.frames.add(frame);
            }
        }

        return bean;
    }
}
