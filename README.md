# DragonBonesAndroid
DragonBones runtime for Android

DragonBones的android原生代码运行库 
---- 2018-12-03 ----
支持v4.5版本json文件解析
已实现
1.bone变换动画+slot入出动画
2.z轴顺序
3.大小粗略预估，给scale<1适当缩小,避免其他帧(非第1帧)出屏幕
4.支持不给name加载默认动画(有效的)

缺：
1.声音部分的播放
2.大小还没找到有效方式精确获取，"aabb"节点给的大小只能涵盖第1帧

示例

import org.android.dragonbones.layer.ArmatureDrawable;

    private void readSke(String path) {
        mAd.setScale(0.8f);
        mAd.enableSimpleImageCache("/mnt/sdcard/ske", true);
        // mAd.loadAnimation("/mnt/sdcard/ske.json", "Armatureblue");
        mAd.loadAnimation(path, null);

        View nodeView = findViewById(R.id.nodeView);
        nodeView.setBackgroundDrawable(mAd);
        // mAd.play("default", false);
        mAd.play(null, false);
    }

