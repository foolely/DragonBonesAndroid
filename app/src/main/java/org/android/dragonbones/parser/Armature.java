package org.android.dragonbones.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

// @root/Armature
public class Armature extends JsonBean {
    public static final String kSkin = "skin";
    public static final String kAnimation = "animation";
    public static final String kActions = "theDefaultActions";

    public String type;
    public int frameRate;
    public ArrayList<Ka_Bone> bones = new ArrayList<>();
    public ArrayList<Ka_Skin> skins = new ArrayList<>();
    public ArrayList<Ka_Slot> slots = new ArrayList<>();
    public ArrayList<Ka_Animation> animations = new ArrayList<>();
    public ArrayList<Ka_Action> actions = new ArrayList<>();
    public Ka_IK ik; // todo:

    public float x,y,width,height; // aabb节点

    public static Armature fromJson(JSONObject json) {
        Armature bean = new Armature();

        bean.name = _str(json, kName, "");
        bean.type = _str(json, kType, "");
        bean.frameRate = _int(json, kFrameRate, 24);

        HashMap<String, Transform> boneTransforms = new HashMap<>();

        JSONArray arr = json.optJSONArray(kBone);
        if (arr != null) {
            for (int i = 0; i < arr.length(); ++i) {
                Ka_Bone bone = Ka_Bone.fromJson(arr.optJSONObject(i));
                boneTransforms.put(bone.name, bone.transform);
                bean.bones.add(bone);
            }
        }

        arr = json.optJSONArray(kSkin);
        if (arr != null) {
            for (int i = 0; i < arr.length(); ++i) {
                bean.skins.add(Ka_Skin.fromJson(arr.optJSONObject(i)));
            }
        }

        arr = json.optJSONArray(kSlot);
        if (arr != null) {
            for (int i = 0; i < arr.length(); ++i) {
                bean.slots.add(Ka_Slot.fromJson(arr.optJSONObject(i)));
            }
        }

        arr = json.optJSONArray(kAnimation);
        if (arr != null) {
            for (int i = 0; i < arr.length(); ++i) {
                bean.animations.add(Ka_Animation.fromJson(arr.optJSONObject(i), boneTransforms, bean.frameRate));
            }
        }

        arr = json.optJSONArray(kActions);
        if (arr != null) {
            for (int i = 0; i < arr.length(); ++i) {
                bean.actions.add(Ka_Action.fromJson(arr.optJSONObject(i)));
            }
        }

        // 获取aabb节点的信息
        JSONObject aabb = _obj(json, "aabb");
        if (aabb != null) {
            bean.x = _float(aabb, "x", -1);
            bean.y = _float(aabb, "y", -1);
            bean.width = _float(aabb, "width", -1);
            bean.height = _float(aabb, "height", -1);
            if (bean.width > 0 && bean.height > 0) {
                // 纠偏 xy不一定在正中心
//                float half = bean.width * 0.5f + bean.x;
//                bean.width += half>0?half*2:-half*2;
//                half = bean.height * 0.5f + bean.y;
//                bean.height += half>0?half*2:-half*2;
//                bean.width = bean.x/bean.width;
//                bean.height = bean.y/bean.height;
            }
        }

        return bean;
    }
}
