package org.android.dragonbones.layer;

import android.animation.ValueAnimator;

import org.android.dragonbones.parser.Armature;
import org.android.dragonbones.parser.Ka_Animation;
import org.android.dragonbones.parser.Ka_Bone;
import org.android.dragonbones.parser.Ka_Skin;
import org.android.dragonbones.parser.Ka_Slot;
import org.android.dragonbones.parser.Kaa_Bone;
import org.android.dragonbones.parser.Kaa_Slot;
import org.android.dragonbones.parser.Kas_Slot;
import org.android.dragonbones.parser.Kass_Display;
import org.android.dragonbones.parser.Skeleton;
import org.android.dragonbones.parser.Transform;

import java.util.ArrayList;
import java.util.HashMap;

public class ArmatureNode extends SKNode {
    private HashMap<String, HashMap<String, SKAnimation>> boneAnimations = new HashMap<>();
    private HashMap<String, HashMap<String, SKAnimation>> slotAnimations = new HashMap<>();
    private HashMap<String, SKAnimation> frameAnimations = new HashMap<>();
    private HashMap<String, SKNode> bones = new HashMap<>();
    private HashMap<String, SKNode> slots = new HashMap<>();
    private ArrayList<ArmatureNode> subArmatureNodes = new ArrayList<>();

    private ValueAnimator mFrameAnimator;

    public static ArmatureNode fromParser(Skeleton parser, String name, Transform base, ImageCache cache) {
        Armature armature = parser.armature(name);
        if (armature==null) return null;

        ArmatureNode root = new ArmatureNode();
        root.nodeType = "armature";
        root.name = name;
//        root.mFrameAnimator = ValueAnimator.
//        armature.frameRate;
        if (base!=null) {
            root.setTransform(base);
        }

        // 分支节点 构造树结构
        for (Ka_Bone bone : armature.bones) {
            SKNode node = createBoneNode(bone);
            SKNode parent = root.bones.get(bone.parent);
            if (parent == null) parent = root;
            parent.addChild(node);
            root.bones.put(bone.name, node);
        }

        // 叶子节点 往树上加叶子
        for (Ka_Slot slot : armature.slots) {
            SKNode node = createSlotNode(slot);
            SKNode parent = root.bones.get(slot.parent);
            parent.addChild(node);
            root.slots.put(slot.name, node);
        }

        // 贴图
        for (Ka_Skin skin : armature.skins) {
            for (Kas_Slot slot : skin.slots) {
                SKNode node = root.slots.get(slot.name);
                for (Kass_Display display : slot.displays) {
                    switch (display.type) {
                        case Kass_Display.tImage:
                            node.addChild(DisplayNode.fromBean(display, cache));
                            break;
                        case Kass_Display.tArmature:
                        case Kass_Display.tMesh: { // 2个都是子骨架 写在一起
                            ArmatureNode subItem = fromParser(parser, display.name, display.transform, cache);
                            node.addChild(subItem);
                            root.subArmatureNodes.add(subItem);
                            break; }
                    }

                }
            }
        }

        // 动画
        for (Ka_Animation animation : armature.animations) {
            HashMap<String, SKAnimation> boneAni = new HashMap<>();
            HashMap<String, SKAnimation> slotAni = new HashMap<>();

            // bone动画序列 有位移/缩放/错切 跳帧
            for (Kaa_Bone bone : animation.bones) {
                SKNode node = root.bones.get(bone.name);
                Kaa_Bone.Frame firstFrame = bone.frames.get(0);
                node.setTransform(firstFrame.transform);
            }

            // slot动画序列 只有显示/隐藏(即时) 淡入/淡出
            for (Kaa_Slot slot : animation.slots) {
                SKNode node = root.slots.get(slot.name);
                Kaa_Slot.Frame firstFrame = slot.frames.get(0);
//                node.z = slot
            }

            // frame 播放声音
            for (Ka_Animation.Frame frame : animation.frames) {

            }
        }

        return root;
    }
    // bone节点 可以嵌套 可以加slot节点
    public static SKNode createBoneNode(Ka_Bone bean) {
        SKNode node = new SKNode();
        node.nodeType = "bone";
        node.name = bean.name;
        node.setTransform(bean.transform);
        return node;
    }
    public static SKNode createSlotNode(Ka_Slot slot) {
        SKNode node = new SKNode();
        node.nodeType = "slot";
        node.name = slot.name;
        node.z = slot.z; // /100
        return node;
    }
}

