package org.android.dragonbones.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

// @root/Armature/Skin/Slot
public class Kas_Slot extends JsonBean {
    public static final String kDisplay = "display";

    public ArrayList<Kass_Display> displays = new ArrayList<>();

    public static Kas_Slot fromJson(JSONObject json) {
        Kas_Slot bean = new Kas_Slot();

        _name(json, bean);

        JSONArray arr = json.optJSONArray(kDisplay);
        for (int i = 0; i < arr.length(); ++i) {
            bean.displays.add(Kass_Display.fromJson(arr.optJSONObject(i)));
        }

        return bean;
    }
}
