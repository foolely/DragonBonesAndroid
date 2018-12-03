package org.android.dragonbones.layer;

import android.graphics.Bitmap;

// 图片获取及缓存
public interface ImageCache {
    public Bitmap load(String path);
}
