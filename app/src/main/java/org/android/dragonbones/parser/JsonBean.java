package org.android.dragonbones.parser;

import org.json.JSONObject;

public class JsonBean {
    public static final String kName = "name";
    public static final String kTransform = "transform";
    public static final String kFrame = "frame";
    public static final String kDuration = "duration";
    public static final String kTweenEasing = "tweenEasing";
    public static final String kParent = "parent";
    public static final String kDisplayIndex = "displayIndex";
    public static final String kType = "type";
    public static final String kSlot = "slot";
    public static final String kBone = "bone";
    public static final String kFrameRate = "frameRate";

    public static int _int(JSONObject json, String key, int def) {
        return json!=null?json.optInt(key, def):def;
    }

    public static float _float(JSONObject json, String key, float def) {
        return json!=null?(float) json.optDouble(key, def):def;
    }

    public static double _double(JSONObject json, String key, double def) {
        return json!=null?json.optDouble(key, def):def;
    }

    public static String _str(JSONObject json, String key, String def) {
        return json!=null?json.optString(key, def):def;
    }

    public static JSONObject _obj(JSONObject json, String key) {
        return json!=null?json.optJSONObject(key):null;
    }

    public String name;
    public static void _name(JSONObject json, JsonBean bean) {
        bean.name = _str(json, kName, "");
    }

}
