package org.android.dragonbones.layer;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.android.dragonbones.parser.Skeleton;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;

public class ArmatureDrawable extends Drawable implements ValueAnimator.AnimatorUpdateListener {
    private ImageCache mImageCache;
    private ValueAnimator mFrameAnimator;
    private ArmatureNode mNode;
    private SKContext mDrawContext = new SKContext();
    private float mScale = 1.0f;
    private PaintFlagsDrawFilter mAntiAlias = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mNode!=null) {
            canvas.setDrawFilter(mAntiAlias);
            Rect bounds = getBounds();
            // 元素中心放置于绘制区域的中心
//            canvas.translate(bounds.width()/2, bounds.height()/2 );
            float x = 0, y = 0;
            if (mNode.width>0 && mNode.height>0 && bounds.width()>0 && bounds.height()>0) {
                x = bounds.width()*mNode.x;
                y = bounds.height()*mNode.y;
                canvas.translate(x, y);

                float sx = bounds.width()/mNode.width;
                float sy = bounds.height()/mNode.height;
                if (sx > sy) sx = sy;
                sx *= mScale;
                canvas.scale(sx, sx);
//                canvas.scale(sx, sy);
            } else {
                x = bounds.width() * 0.5f;
                y = bounds.height() * 0.5f;
                canvas.translate(x, y);
                canvas.scale(mScale, mScale);
            }
            mDrawContext.resetDrawItems();
            mNode.dispatchLayout(mDrawContext);
            mDrawContext.dispatchDraw(canvas);
            canvas.translate(-x, -y);
//            canvas.translate(-bounds.width()/2, -bounds.height()/2 );
        }
    }

    @Override
    public void setAlpha(int i) {
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    // 读取文本文件
    public static String readText(String path,int maxLen) {
        FileInputStream in = null;
        String r = null;
        do {
            try {
                File f = new File(path);
                if (!f.exists()) break;
                if (maxLen<=0) maxLen = (int)f.length();
                in = new FileInputStream(f);
                byte[] bin = new byte[maxLen];
                int ret = in.read(bin);
                r = new String(bin, 0, ret);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (false);
        if (in!=null) {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return r;
    }

    public void setScale(float scale) {
        mScale = scale;
    }
    // 设置图片缓存对象
    public void setImageCache(ImageCache ic) {
        mImageCache = ic;
    }
    // 初始化简单缓存对象
    public void enableSimpleImageCache(String dir, boolean clean) {
        if (mImageCache !=null && mImageCache instanceof SimpleImageCache) {
            if (clean) {
                ((SimpleImageCache) mImageCache).clear();
            }
            ((SimpleImageCache) mImageCache).setDir(dir);
        } else {
            SimpleImageCache ic = new SimpleImageCache();
            ic.setDir(dir);
            setImageCache(ic);
        }
    }
    // 从内存加载动画数据
    public boolean loadAnimationFromString(String data, String name) {
        try {
            JSONObject json = new JSONObject(data);
            return loadAnimationFromJson(json,name);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
    // 从文件加载动画数据
    public boolean loadAnimation(String path, String name) {
        return loadAnimationFromString(readText(path, 0), name);
    }
    // 从json加载动画
    public boolean loadAnimationFromJson(JSONObject json, String name) {
        try {
            Skeleton ske = Skeleton.fromJson(json);
            if (name == null) {
                name = ske.getDefaultName();
            }
            ArmatureNode an = ArmatureNode.fromParser(ske, name, null, mImageCache);
            mNode = an;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // 播放指定动画动画
    public void play(String name, boolean repeat) {
        if (mNode == null) return;
        int maxFrames = mNode.playAnimation(name, repeat);

        if (mFrameAnimator !=null) {
            mFrameAnimator.cancel();
            mFrameAnimator = null;
        }
        mFrameAnimator = ValueAnimator.ofInt(0, maxFrames);
        mFrameAnimator.setDuration(1000*maxFrames/mNode.frameRate);
        mFrameAnimator.setRepeatCount(repeat?Integer.MAX_VALUE:0); // 0=1遍 1=2遍
        mFrameAnimator.addUpdateListener(this);

        mFrameAnimator.start();
    }
    // 停止当前动画
    public void stop() {
        if (mFrameAnimator != null) {
            mFrameAnimator.cancel();
            mFrameAnimator = null;
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        Integer value = (Integer) valueAnimator.getAnimatedValue();
        if (value != mDrawContext.frameIndex) {
//            mNode._log("onAnimationUpdate:"+value);
            mDrawContext.frameIndex = value;
            mNode.calcAnimations(mDrawContext.frameIndex);
            if (mNode.clearNeedDraw()) {
                invalidateSelf();
            } else {
//                mNode._log("no draw:"+value);
            }
        }
    }
}
