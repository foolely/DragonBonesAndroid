package org.android.dragonbones.parser;

// @root/Armature/Animation/Slot/Frame
// @root/Armature/Animation/Bone
public class Kaa_Frame {
    // 原始文件意思 是否平滑过渡到下一帧 最后一帧的下一帧是first
    public boolean tweenEasing = false; // xxx false=过x帧跳到该帧 true=满满的过渡到该帧
    public int frameCount = 0; // 使用帧数在android上更好用
    public Object info = null; // 帧数据 变换对象/显示效果

    public Transform transform() {
        if (info==null) {
            info = new Transform();
        }
        return (Transform) info;
    }

    public void transform(Transform transform) {
        info = transform;
    }

    public ShowEffect effect() {
        if (info == null) {
            info = new ShowEffect();
        }
        return (ShowEffect) info;
    }
}
