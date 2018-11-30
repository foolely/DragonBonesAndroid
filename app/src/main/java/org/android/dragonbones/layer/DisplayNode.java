package org.android.dragonbones.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.android.dragonbones.parser.Kass_Display;

public class DisplayNode extends SKNode {
    protected Paint mPaint = new Paint();
    private Bitmap mImage;
    private Matrix mMatrix = new Matrix();
    public static DisplayNode fromBean(Kass_Display display, ImageCache cache) {
//                         let components = display.name.components(separatedBy: "/")
//                        let atlasName = components[0]
//                        let textureName = components[1].addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed)!
//                        let atlas = SKTextureAtlas(named: atlasName)
//                        let texture = atlas.textureNamed(textureName)
//
//                        let spriteNode = EDDisplayNode(transform: display.transform, texture: texture)
        DisplayNode node = new DisplayNode();
        node.nodeType = "image";
        node.name = display.name;
        node.mImage = cache.load(display.name);
        node.setTransform(display.transform);
        return node;
    }

    @Override
    public void draw(Canvas canvas, DrawContext context) {
        super.draw(canvas, context);
        mPaint.setColor(0xff0000ff);
        canvas.drawText(name, x+20, y+20, mPaint);
        if (mImage!=null) {
            float left = mImage.getWidth() * -0.5f;
            float top = mImage.getHeight() * -0.5f;
            canvas.drawBitmap(mImage, left, top, mPaint);
//            mMatrix.setTranslate(centerX, centerY);
//            mMatrix.setScale(scaleX, scaleY);
//            mMatrix.setRotate((skX+skY)*0.5f, x, y);
//            canvas.drawBitmap(mImage, mMatrix, mPaint);
        }
//        canvas.drawBitmap();
    }
}
