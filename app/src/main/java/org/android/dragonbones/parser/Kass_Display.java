package org.android.dragonbones.parser;

import org.json.JSONObject;

// @root/Armature/Skin/Slot/Display
public class Kass_Display extends JsonBean {

    public static final int tImage = 0;
    public static final int tArmature = 1;
    public static final int tMesh = 2;
    public static final String[] kTypes = {"image", "armature", "mesh"};

    public int type = tImage;
    public Transform transform;

    public static Kass_Display fromJson(JSONObject json) {
        Kass_Display bean = new Kass_Display();

        _name(json, bean);
        String type = _str(json, kType, kTypes[0]);
        for (int i = 0; i < kTypes.length; ++i) {
            if (kTypes[i].equals(type)) {
                bean.type = i;
                break;
            }
        }
        bean.transform = Transform.fromJson(_obj(json, kTransform), null);

        return bean;
    }
}
