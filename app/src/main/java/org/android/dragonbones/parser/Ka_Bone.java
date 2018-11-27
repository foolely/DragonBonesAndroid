package org.android.dragonbones.parser;

import org.json.JSONObject;

// @root/Armature/Bone
// 骨架容器
public class Ka_Bone extends JsonBean {
    public Transform transform;
    public String parent;

    public static Ka_Bone fromJson(JSONObject json) {
        Ka_Bone bean = new Ka_Bone();

        _name(json, bean);
        bean.transform = Transform.fromJson(_obj(json, kTransform), null);
        bean.parent = _str(json, kParent, null);

        return bean;
    }
}
