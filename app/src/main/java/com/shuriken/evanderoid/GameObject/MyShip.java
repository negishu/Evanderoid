package com.shuriken.evanderoid.GameObject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shuriken.evanderoid.Game.GameResourceInfo;

import java.util.Random;

public class MyShip extends ActiveObject {
    private final Random rand;
    private int x = 0, y = 0, w = 128 * M, h = 128 * M, sx = 0, sy = 0, tx = 0, ty = 0;
    private int width, height, nSize, nAngle, nAlpha;

    public MyShip(final Random rand) {
        this.rand = rand;
        Init();
    }

    public void Init() {
        nSize  = 0;
        nAngle = 0;
        nAlpha = 255;
        x = (width / 2) * M;
        y = ((height * 7) / 11) * M;
        tx = x;
        ty = y;
    }

    public void SetScreenSize(int width, int height) {
        this.width = width;
        this.height = height;
        Init();
    }

    public int getTurnDegrees() {
        return nAngle + 90;
    }

    @Override
    public void update() {
        x += sx;
        y += sy;
        final Rect r = getLogicRect();
        if (r.left < 0) {
            sx = -sx;
            x += sx;
        }
        if (r.right > width * M) {
            sx = -sx;
            x += sx;
        }
        if (r.top < 0) {
            sy = -sy;
            y += sy;
        }
        if (r.bottom > (height - 20) * M) {
            sy = -sy;
            y += sy;
        }
        sx = (tx - x) / 2;
        sy = (ty - y) / 2;
        nAngle = sx / 64;
        nAngle %= 360;
        if (nSize > 0) {
            nSize /= 15;
            nSize *= 14;
            nAngle = 0;
        }
    }

    @Override
    public void drawSimple(final Canvas canvas, final Paint paint) {
        final Rect physRC = getPhysRect();
        paint.setAlpha(255);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(physRC, paint);
        //canvas.drawOval(physRC, paint);
    }

    @Override
    public void draw(final Canvas canvas, final GameResourceInfo aGameResourceInfo) {
        final Rect physRC = getPhysRect();
        {
            Rect src = new Rect();
            src.set(0, 0, 252, 252);
            if (nSize > 0) {
                nAlpha = 90;
            } else {
                nAlpha = 255;
            }
            aGameResourceInfo.paint.setAlpha(nAlpha);
            canvas.save();
            canvas.rotate(-nAngle, physRC.centerX(), physRC.centerY());
            //drawsimple(canvas, aGameResourceInfo.paint);
            canvas.drawBitmap(aGameResourceInfo.bmp_ship, null, physRC, aGameResourceInfo.paint);
            canvas.restore();
        }
    }

    @Override
    public final Rect getLogicRect() {
        final int X = x;
        final int Y = y;
        final int W = w / 2;
        final int H = h / 2;
        logicRC.left   = X - W + (M*12);
        logicRC.top    = Y - H + (M*12);
        logicRC.right  = X + W - (M*12);
        logicRC.bottom = Y + H - (M*12);
        return logicRC;
    }

    @Override
    public final Rect getPhysRect() {
        final int X = x;
        final int Y = y;
        final int W = w / 2;
        final int H = h / 2;
        physRC.left   = (X - W - nSize) / M;
        physRC.top    = (Y - H - nSize) / M;
        physRC.right  = (X + W + nSize) / M;
        physRC.bottom = (Y + H + nSize) / M;
        return physRC;
    }

    public void MoveTo(int x, int y) {
        x *= ActiveObject.M;
        y *= ActiveObject.M;
        tx = x;
        ty = y;
        sx = (tx - this.x);
        sy = (ty - this.y);
    }

    public boolean isHitRock(Rocks rocks) {
        if (nSize <= 0) {
            if (rocks.isHit(this)) {
                nSize = 60000;
                return true;
            }
        }
        return false;
    }

    public boolean isHitBullet(Bullets bullets) {
        if (nSize <= 0) {
            if (bullets.isHitDown(this)) {
                nSize = 60000;
                return true;
            }
        }
        return false;
    }

    public boolean isReady() {
        if (nSize <= 0) {
            return true;
        }
        return false;
    }
}
