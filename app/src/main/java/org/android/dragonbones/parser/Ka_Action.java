package org.android.dragonbones.parser;

import org.json.JSONObject;

// @root/Armature/Action
// 动作
public class Ka_Action {
    public static final String kGotoAndPlay = "gotoAndPlay";
    public String gotoAndPlay;

    public static Ka_Action fromJson(JSONObject json) {
        Ka_Action bean = new Ka_Action();

        bean.gotoAndPlay = JsonBean._str(json, kGotoAndPlay, null);

        return bean;
    }
}
