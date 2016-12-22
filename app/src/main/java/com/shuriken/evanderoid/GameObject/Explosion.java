package com.shuriken.evanderoid.GameObject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shuriken.evanderoid.Game.GameResourceInfo;

/**
 * Created by shu on 2016/02/21.
 */

public class Explosion extends ActiveObject {
    enum Type {ONE, TWO, THREE}
    Type type = Type.ONE;
    private int x = 0, y = 0, a = 255, t = 0, ch = 0, size;
    private boolean bActive = false;
    private int width, height;

    private int image_IDs[] = new int[32];

    public Explosion() {
        this.x = 0;
        this.y = 0;
        this.size = 256 * M;
        this.t = 0;
        this.bActive = false;

        image_IDs[0] = 0;
        image_IDs[1] = 0;
        image_IDs[2] = 1;
        image_IDs[3] = 1;
        image_IDs[4] = 1;
        image_IDs[5] = 2;
        image_IDs[6] = 2;
        image_IDs[7] = 3;
        image_IDs[8] = 4;
        image_IDs[9] = 3;
        image_IDs[10] = 4;
        image_IDs[11] = 5;
        image_IDs[12] = 5;
        image_IDs[13] = 6;
        image_IDs[14] = 6;
        image_IDs[15] = 6;
        image_IDs[16] = 7;
        image_IDs[17] = 7;
        image_IDs[18] = 7;
        image_IDs[19] = 8;
        image_IDs[20] = 8;
        image_IDs[21] = 9;
        image_IDs[22] = 10;
        image_IDs[23] = 10;
        image_IDs[24] = 11;
        image_IDs[25] = 11;
        image_IDs[26] = 12;
        image_IDs[27] = 12;
        image_IDs[28] = 13;
        image_IDs[29] = 13;
        image_IDs[30] = 14;
        image_IDs[31] = 15;
    }

    public void SetScreenSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void SetPosAndActive(int x, int y, Type type) {
        this.x = x * M;
        this.y = y * M;
        this.size = 256 * M;
        this.t = 0;
        this.type = type;
        this.bActive = true;
    }

    @Override
    public void update() {
        t += 100;
        if (t >= 3200) {
            bActive = false;
        } else {
            ch = image_IDs[t/100];
        }
    }

    @Override
    public void draw(final Canvas canvas, final GameResourceInfo aGameResourceInfo) {
        if (bActive) {
            if (type == Type.ONE) {
                final Rect physRC = getPhysRect();
                final int nWidth = (aGameResourceInfo.bmp_explosion.getWidth() / 4);
                final int nHeight = (aGameResourceInfo.bmp_explosion.getHeight() / 4);
                src.set(((ch % 4) * nWidth), ((ch / 4) * nHeight), ((ch % 4) * nWidth + nWidth), ((ch / 4) * nHeight + nHeight));
                canvas.save();
                aGameResourceInfo.paint.setStyle(Paint.Style.STROKE);
                aGameResourceInfo.paint.setAlpha(255);
                canvas.drawBitmap(aGameResourceInfo.bmp_explosion, src, physRC, aGameResourceInfo.paint);
                //canvas.drawRect(rcF, aGameResourceInfo.paint);
                //canvas.drawOval(rcF, aGameResourceInfo.paint);
                canvas.restore();
            }
            else
            if (type == Type.TWO) {
                final Rect physRC = getPhysRect();
                final int nWidth  = (aGameResourceInfo.bmp_target1.getWidth() / 4);
                final int nHeight = (aGameResourceInfo.bmp_target1.getHeight()/ 4);
                src.set(((ch % 4) * nWidth), ((ch / 4) * nHeight), ((ch % 4) * nWidth + nWidth), ((ch / 4) * nHeight + nHeight));
                canvas.save();
                aGameResourceInfo.paint.setStyle(Paint.Style.STROKE);
                aGameResourceInfo.paint.setAlpha(255);
                canvas.drawBitmap(aGameResourceInfo.bmp_target1, src, physRC, aGameResourceInfo.paint);
                //canvas.drawRect(rcF, aGameResourceInfo.paint);
                //canvas.drawOval(rcF, aGameResourceInfo.paint);
                canvas.restore();
            }
            else
            if (type == Type.THREE) {
                final Rect physRC = getPhysRect();
                final int nWidth  = (aGameResourceInfo.bmp_target1.getWidth() / 4);
                final int nHeight = (aGameResourceInfo.bmp_target1.getHeight()/ 4);
                src.set(((ch % 4) * nWidth), ((ch / 4) * nHeight), ((ch % 4) * nWidth + nWidth), ((ch / 4) * nHeight + nHeight));
                canvas.save();
                aGameResourceInfo.paint.setStyle(Paint.Style.STROKE);
                aGameResourceInfo.paint.setAlpha(255);
                canvas.drawBitmap(aGameResourceInfo.bmp_target2, src, physRC, aGameResourceInfo.paint);
                //canvas.drawRect(rcF, aGameResourceInfo.paint);
                //canvas.drawOval(rcF, aGameResourceInfo.paint);
                canvas.restore();
            }
        }
    }

    @Override
    public final Rect getLogicRect() {
        final int X = x;
        final int Y = y;
        final int WH = size / 2;
        logicRC.left   = X - WH - 1;
        logicRC.top    = Y - WH - 1;
        logicRC.right  = X + WH + 1;
        logicRC.bottom = Y + WH + 1;
        return logicRC;
    }

    @Override
    public final Rect getPhysRect() {
        final int X = x;
        final int Y = y;
        final int WH = size / 2;
        physRC.left   = (X - WH + 1) / M;
        physRC.top    = (Y - WH + 1) / M;
        physRC.right  = (X + WH - 1) / M;
        physRC.bottom = (Y + WH - 1) / M;
        return physRC;
    }
}