package org.android.dragonbones.parser;

import org.json.JSONObject;

// @root/Armature/Slot
public class Ka_Slot extends JsonBean {
    public static final String kZ = "z";

    public String parent;
    public int z;
    public int displayIndex;

    public static Ka_Slot fromJson(JSONObject json) {
        Ka_Slot bean = new Ka_Slot();

        _name(json, bean);
        bean.parent = _str(json, kParent, "");
        bean.z = _int(json, kZ, 0);
        bean.displayIndex = _int(json, kDisplayIndex, 0);

        return bean;
    }
}
