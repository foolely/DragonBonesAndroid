package org.android.dragonbones.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

// @root/Armature/Skin
// 皮肤 贴图
public class Ka_Skin extends JsonBean {
    public static final String kSlot = "slot";

    public ArrayList<Kas_Slot> slots = new ArrayList<>();

    public static Ka_Skin fromJson(JSONObject json) {
        Ka_Skin bean = new Ka_Skin();

        _name(json, bean);
        JSONArray arr = json!=null?json.optJSONArray(kSlot):null;
        if (arr!=null) {
            for (int i = 0; i < arr.length(); ++i) {
                bean.slots.add(Kas_Slot.fromJson(arr.optJSONObject(i)));
            }
        }

        return bean;
    }
}
