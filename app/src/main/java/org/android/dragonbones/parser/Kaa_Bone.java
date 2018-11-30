package org.android.dragonbones.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

// @root/Armature/Animation/Bone
// 动画里的bone
public class Kaa_Bone extends JsonBean {

//    public static class Frame extends Kaa_Frame {
//        public Transform transform;
//        public float duration; // 解析到这里的意义不是这一个frame的时长 而是显示起点 上一帧的时长
//    }

    public ArrayList<Kaa_Frame> frames = new ArrayList<>();

    public static Kaa_Bone fromJson(JSONObject json, Map<String,Transform> boneTransforms, int frameRate) {
        Kaa_Bone bean = new Kaa_Bone();

        _name(json, bean);

        JSONArray arr = (json!=null)?json.optJSONArray(kFrame):null;
        if (arr!=null&&arr.length()>0) {
//            float duration = 0;
//            boolean tweenEasing = false;
//            int frameCount = 0;
            Kaa_Frame frame = null;
            Transform base = boneTransforms.get(bean.name);
            for (int i = 0; i< arr.length(); ++i) {
                JSONObject item = arr.optJSONObject(i);

                frame = new Kaa_Frame();
                frame.transform(Transform.fromJson(_obj(item, kTransform), base));
                frame.frameCount = _int(item, kDuration, 0);
                frame.tweenEasing = _int(item, kTweenEasing, -1)!=-1;
//                frame.duration = duration;
//                frame.tweenEasing = tweenEasing;
//                frame.frameCount = frameCount;

                // duration原始时长的计数单位是帧数 ?
//                duration = 1.0f/frameRate * _int(item, kDuration, 0);
//                tweenEasing = _int(item, kTweenEasing, -1)!=-1;
//                frameCount = _int(item, kDuration, 0);

                bean.frames.add(frame);
            }

//            if (frameCount>0) {
//                frame = bean.frames.get(0);
//                Frame last = new Frame();
//                last.tweenEasing = false;
//                last.transform = frame.transform;
//                last.frameCount = frameCount;
//                bean.frames.add(last);
//            }
            // 最后一帧又回到了第1帧 为了循环么?
//            if (duration!=0) {
//                Frame frame = new Frame();
//                Frame first = bean.frames.get(0);
//                frame.transform = first.transform;
//                frame.tweenEasing = false;
//                frame.duration = duration;
//                bean.frames.add(frame);
//            }
        }

        return bean;
    }
}
