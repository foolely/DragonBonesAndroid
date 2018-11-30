package org.android.dragonbones.parser;

import org.json.JSONObject;

// @root/Armature/Transform
// @root/Armature/Bone/Transform
// @root/Armature/Animation/Bone/Frame/Transform
// @root/Armature/Skin/Slot/Display/Transform
// 变换矩阵的参数
// sk原始数值是角度 0-90-180-360
public class Transform {
    public static final String kX = "x";
    public static final String kY = "y";
    public static final String kScX = "scX";
    public static final String kScY = "scY";
    public static final String kSkX = "skX";
    public static final String kSkY = "skY";

    public float x = 0;
    public float y = 0;
    public float scX = 1;
    public float scY = 1;
    public float skX = 0;
    public float skY = 0;
//    let zRotation: CGFloat 旋转的弧度 以PI为单位
//    zRotation = -CGFloat(((skX + skY) / 2 ) * .pi) / 180

    public static Transform fromJson(JSONObject json, Transform base) {
        Transform bean = new Transform();
        bean.x = JsonBean._float(json, "x", 0);
        bean.y = JsonBean._float(json, "y", 0);

        bean.scX = JsonBean._float(json, "scX", 1);
        bean.scY = JsonBean._float(json, "scY", 1);

        bean.skX = JsonBean._float(json, "skX", 0);
        bean.skY = JsonBean._float(json, "skY", 0);

        // 根据基准矩阵偏移
        if (base!=null) {
            bean.x += base.x;
            bean.y += base.y;
            bean.skX += base.skX;
            bean.skY += base.skY;
            bean.scX *= base.scX;
            bean.scY *= base.scY;
        }
        return bean;
    }

    // 复制
    public void set(Transform dst) {
        x = dst.x;
        y = dst.y;
        scX = dst.scX;
        scY = dst.scY;
        skX = dst.skX;
        skY = dst.skY;
    }

    // 中间采样设置
    public void set(Transform beg, Transform end, float progress) {
        if (progress==0) {
            set(beg);
        } else if (progress==1) {
            set(end);
        } else {
            float rate = 1 - progress;
            x = beg.x * rate + end.x * progress;
            y = beg.y * rate + end.y * progress;
            scX = beg.scX * rate + end.scX * progress;
            scY = beg.scY * rate + end.scY * progress;
            skX = beg.skX * rate + end.skX * progress;
            skY = beg.skY * rate + end.skY * progress;
        }
    }

    public boolean isEqual(Transform dst) {
        return x == dst.x && y == dst.y &&
                scX == dst.scX && scY == dst.scY &&
                skX == dst.skX && skY == dst.skY;
    }

//    init(json: JSON, defaultTransform: Transform) {
//        let offsetSkX = json["skX"].double ?? 0
//        let offsetSkY = json["skY"].double ?? 0
//        let offsetX = CGFloat(json["x"].double ?? 0)
//        let offsetY = -CGFloat(json["y"].double ?? 0)
//        let offsetScX = CGFloat(json["scX"].double ?? 1)
//        let offsetScY = CGFloat(json["scY"].double ?? 1)
//        let offsetZRotation = -CGFloat(((offsetSkX + offsetSkY) / 2 ) * .pi) / 180
//
//        scX = defaultTransform.scX * offsetScX
//        scY = defaultTransform.scY * offsetScY
//        zRotation = defaultTransform.zRotation + offsetZRotation
//        position = CGPoint(x: defaultTransform.position.x + offsetX, y: defaultTransform.position.y + offsetY)
//    }

}
