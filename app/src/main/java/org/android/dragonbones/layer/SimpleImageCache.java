package org.android.dragonbones.layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.util.HashMap;

public class SimpleImageCache implements ImageCache {
    private String mRootDir;
    private HashMap<String, Bitmap> mCache = new HashMap<>();
    public void clear() {
        mRootDir = null;
        mCache.clear();
    }
    public void setDir(String dir) {
        if (dir==null) {
            clear();
            return;
        }
        if (dir.equals(mRootDir)) {
            return;
        }
        clear();
        mRootDir = dir;
    }
    @Override
    public Bitmap load(String path) {
        Bitmap bitmap = mCache.get(path);
        if (bitmap!=null) {
            return bitmap;
        }
        if (!path.endsWith(".png")&&!path.endsWith(".jpg")) {
            path += ".png";
        }
        bitmap = BitmapFactory.decodeFile(new File(mRootDir, path).getAbsolutePath());
        if (bitmap!=null) {
            mCache.put(path, bitmap);
            return bitmap;
        }
        return null;
    }
}
