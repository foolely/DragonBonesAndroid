package org.android.dragonbones.layer;

import android.graphics.Bitmap;

public interface ImageCache {
    public Bitmap load(String path);
}
