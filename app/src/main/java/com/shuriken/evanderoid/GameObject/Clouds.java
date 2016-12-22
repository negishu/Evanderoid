package com.shuriken.evanderoid.GameObject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shuriken.evanderoid.Game.GameResourceInfo;

import java.util.Random;

public class Clouds extends ActiveObject {
    private final Random rand;
    private Cloud[] mClouds = null;
    private int width, height;
    private int pos_x[] = new int[360];
    private int pos_y[] = new int[360];
    private final Rect src = new Rect();
    public Clouds(final Random rand, int n) {
        this.rand = rand;
        mClouds = new Cloud[n];
        for (int i = 0; i < n; i++) {
            mClouds[i] = new Cloud(3, 3);
        }
        for (int i = 0; i < 360; i++) {
            pos_x[i] = (int)(Math.cos(Math.PI / 180 * i) * 200000);
            pos_y[i] = (int)(Math.sin(Math.PI / 180 * i) * 200000);
        }
    }

    public void SetScreenSize(int width, int height) {
        this.width  = width;
        this.height = height;
    }

    public void Init(int nLevel) {
        final int size = mClouds.length;
        for (int i = 0; i < size; i++) {
            mClouds[i].x = 0;
            mClouds[i].y = -20000;
            mClouds[i].nAngle = 0;
            mClouds[i].bActive = false;
        }
        tm = 0;
    }

    @Override
    public void update() {
        update(1);
    }

    public void update(final int b) {
        final int size = mClouds.length;
        for (int i = 0; i < size; i++) {
            mClouds[i].update(b);
        }
        tm ++;
    }

    public void update(final int b, final int x, final int y) {
        if (tm % 100 == 0) {
            shootOne(x - 40000 + rand.nextInt(80000), -1000, 0, 800);
        }
        update(b);
    }

    @Override
    public void drawSimple(final Canvas canvas, final Paint paint) {
        final int size = mClouds.length;
        for (int i = 0; i < size; i++) {
            mClouds[i].drawSimple(canvas, paint);
        }
    }

    @Override
    public void draw(Canvas canvas, final GameResourceInfo aGameResourceInfo) {
        final int size = mClouds.length;
        for (int i = 0; i < size; i++) {
            if (mClouds[i].bActive) {
                final Rect physRC = mClouds[i].getPhysRect();
                canvas.save();
                if (mClouds[i].type == Type.ONE) {
                    canvas.rotate(mClouds[i].nAngle % 360, physRC.centerX(), physRC.centerY());
                    aGameResourceInfo.paint.setAlpha(mClouds[i].nAlpha);
                    canvas.drawBitmap(aGameResourceInfo.bmp_cloud_1, null, physRC, aGameResourceInfo.paint);
                }
                else
                if (mClouds[i].type == Type.TWO) {
                    canvas.rotate(mClouds[i].nAngle % 360, physRC.centerX(), physRC.centerY());
                    aGameResourceInfo.paint.setAlpha(mClouds[i].nAlpha);
                    canvas.drawBitmap(aGameResourceInfo.bmp_cloud_2, null, physRC, aGameResourceInfo.paint);
                }
                else
                if (mClouds[i].type == Type.THREE) {
                    canvas.rotate(mClouds[i].nAngle % 360, physRC.centerX(), physRC.centerY());
                    aGameResourceInfo.paint.setAlpha(mClouds[i].nAlpha);
                    canvas.drawBitmap(aGameResourceInfo.bmp_cloud_3, null, physRC, aGameResourceInfo.paint);
                }
                canvas.restore();
            }
        }
    }

    @Override
    public final Rect getLogicRect() {
        return null;
    }

    @Override
    public final Rect getPhysRect() {
        return null;
    }

    public void shootOne(int x, int y, int sx, int sy) {
        final int size = mClouds.length;
        for (int i = 0; i < size; i++) {
            if (mClouds[i].bActive) continue;
            mClouds[i].type = Type.ONE;
            mClouds[i].shoot(x, y, 64 + rand.nextInt(64), 64 + rand.nextInt(64), sx, sy, 0);
            break;
        }
    }

    enum Type {ONE, TWO, THREE}

    private class Cloud extends ActiveObject {
        protected int x = 0, y = 0, w = 0, h = 0, t = 0, a = 255, speedX, speedY, AccelX, AccelY, nAngle, AccelA, nAlpha;
        protected boolean bActive;
        protected Type type = Type.ONE;

        public Cloud(int x, int y) {
            this.speedX = 0;
            this.speedY = 0;
            this.nAngle = 0;
            this.x = x * M;
            this.y = y * M;
            this.w = 1 * M;
            this.h = 1 * M;
            this.AccelX = 0;
            this.AccelY = 0;
            this.AccelA = 0;
            this.nAlpha = 255;
            this.nAngle = rand.nextInt(360);
            this.bActive = false;
        }

        public void shoot(int x, int y, int w, int h, int sx, int sy, int accelA) {
            this.speedX = sx;
            this.speedY = sy;
            this.w = w * M;
            this.h = h * M;
            this.x = x;
            this.y = y;
            this.AccelX = 0;
            this.AccelY = 0;
            this.AccelA = accelA;
            this.bActive = true;
            this.nAlpha = 255;//rand.nextInt(128) + 64;
            this.nAngle = 0;
            this.t = 0;
        }

        @Override
        public void update() {
            update(1);
        }

        public void update(final int b) {
            if (bActive) {
                //nAngle += AccelA;
                x += speedX;
                y += (speedY * (b > 32 ? Math.min((b / 32), 4) : 1));
                speedX += AccelX;
                speedY += AccelY;
                t++;
                if (t > 5000) {
                    bActive = false;
                    t = 0;
                }
            }
        }

        @Override
        public final Rect getLogicRect() {
            logicRC.left   = x - (w/2);
            logicRC.top    = y - (h/2);
            logicRC.right  = x + (w/2);
            logicRC.bottom = y + (h/2);
            return logicRC;
        }

        @Override
        public final Rect getPhysRect() {
            physRC.left   = (x - (w/2)) / M;
            physRC.top    = (y - (h/2)) / M;
            physRC.right  = (x + (w/2)) / M;
            physRC.bottom = (y + (h/2)) / M;
            return physRC;
        }

        @Override
        public HitState IsInteract(final Object object) {
            return HitState.NONE;
        }
        @Override
        public void drawSimple(final Canvas canvas, final Paint paint) {
            Rect physRC = getPhysRect();
            paint.setAlpha(255);
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(physRC, paint);
            ////canvas.drawOval(physRC, paint);
        }
    }
}
