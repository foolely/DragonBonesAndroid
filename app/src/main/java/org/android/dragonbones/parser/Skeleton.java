package org.android.dragonbones.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

// @root
// 文件最外层
public class Skeleton extends JsonBean {
    public static final String kVersion = "version";
    public static final String kIsGlobal = "isGlobal";
    public static final String kArmature = "armature";

    public int frameRate;
    public String version;
    public int isGlobal;
    public ArrayList<Armature> armatures = new ArrayList<>();

    public static Skeleton fromJson(JSONObject json) {
        Skeleton bean = new Skeleton();

        _name(json, bean);
        bean.version = _str(json, kVersion, "");
        bean.frameRate = _int(json, kFrameRate, 24);
        bean.isGlobal = _int(json, kIsGlobal, 0);

        JSONArray arr = json.optJSONArray(kArmature);
        if (arr != null) {
            for (int i = 0; i < arr.length(); ++i) {
                Armature armature = Armature.fromJson(arr.optJSONObject(i));
                bean.armatures.add(armature);
            }
        }
        return bean;
    }
}
