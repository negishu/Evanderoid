package com.shuriken.evanderoid.GameObject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shuriken.evanderoid.Game.GameResourceInfo;

import java.util.Random;

public class Targets extends ActiveObject {
    private final Random rand;
    private int ch = 0, g_ch = 0;
    private Target[] mTargets = null;
    private int width, height;
    private int pos_x[] = new int[360];
    private int pos_y[] = new int[360];
    protected final Explosion mExplosion = new Explosion();
    public Targets(final Random rand, int n) {
        this.rand = rand;
        mTargets = new Target[n];
        for (int i = 0; i < n; i++) {
            mTargets[i] = new Target(3, 3);
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
        final int size = mTargets.length;
        for (int i = 0; i < size; i++) {
            mTargets[i].x = 0;
            mTargets[i].y = -20000;
            mTargets[i].nAngle = rand.nextInt(360);
            mTargets[i].bActive = false;
        }
        tm = 0;
    }

    @Override
    public void update() {
        final int size = mTargets.length;
        for (int i = 0; i < size; i++) {
            mTargets[i].update();
        }
        tm ++; g_ch++; ch = (g_ch/5) % 16;
        mExplosion.update();
    }

    public void update(final int x, final int y) {
        if (tm % 100 == 0) {
            if (rand.nextInt(100) < 50) {
                shootOne(x - 200 + rand.nextInt(400), -10000, 0, 0);
            }
        }
        update();
    }

    @Override
    public void drawSimple(final Canvas canvas, final Paint paint) {
        final int size = mTargets.length;
        for (int i = 0; i < size; i++) {
            mTargets[i].drawSimple(canvas, paint);
        }
    }

    @Override
    public void draw(Canvas canvas, final GameResourceInfo aGameResourceInfo) {
        final int size = mTargets.length;
        for (int i = 0; i < size; i++) {
            if (mTargets[i].bActive) {
                final Rect physRC = mTargets[i].getPhysRect();
                canvas.save();
                if (mTargets[i].type == Type.ONE) {
                    final int nWidth  = (aGameResourceInfo.bmp_target1.getWidth() / 4);
                    final int nHeight = (aGameResourceInfo.bmp_target1.getHeight()/ 4);
                    src.set(((ch % 4) * nWidth), ((ch / 4) * nHeight), ((ch % 4) * nWidth + nWidth), ((ch / 4) * nHeight + nHeight));
                    canvas.rotate(mTargets[i].nAngle % 360, physRC.centerX(), physRC.centerY());
                    aGameResourceInfo.paint.setAlpha(mTargets[i].nAlpha);
                    canvas.drawBitmap(aGameResourceInfo.bmp_target1, src, physRC, aGameResourceInfo.paint);
                    //mTargets[i].drawSimple(canvas, aGameResourceInfo.paint);
                }
                else
                if (mTargets[i].type == Type.TWO) {
                    final int nWidth  = (aGameResourceInfo.bmp_target2.getWidth() / 4);
                    final int nHeight = (aGameResourceInfo.bmp_target2.getHeight()/ 4);
                    src.set(((ch % 4) * nWidth), ((ch / 4) * nHeight), ((ch % 4) * nWidth + nWidth), ((ch / 4) * nHeight + nHeight));
                    canvas.rotate(mTargets[i].nAngle % 360, physRC.centerX(), physRC.centerY());
                    aGameResourceInfo.paint.setAlpha(mTargets[i].nAlpha);
                    canvas.drawBitmap(aGameResourceInfo.bmp_target2, src, physRC, aGameResourceInfo.paint);
                    //mTargets[i].drawSimple(canvas, aGameResourceInfo.paint);
                }
                else
                if (mTargets[i].type == Type.THREE) {
                    final int nWidth  = (aGameResourceInfo.bmp_target3.getWidth() / 4);
                    final int nHeight = (aGameResourceInfo.bmp_target3.getHeight()/ 4);
                    src.set(((ch % 4) * nWidth), ((ch / 4) * nHeight), ((ch % 4) * nWidth + nWidth), ((ch / 4) * nHeight + nHeight));
                    canvas.rotate(mTargets[i].nAngle % 360, physRC.centerX(), physRC.centerY());
                    aGameResourceInfo.paint.setAlpha(mTargets[i].nAlpha);
                    canvas.drawBitmap(aGameResourceInfo.bmp_target3, src, physRC, aGameResourceInfo.paint);
                    //mTargets[i].drawSimple(canvas, aGameResourceInfo.paint);
                }
                canvas.restore();
            }
        }
        mExplosion.draw(canvas, aGameResourceInfo);
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
        final int size = mTargets.length;
        for (int i = 0; i < size; i++) {
            if (mTargets[i].bActive) continue;
            if (rand.nextInt(100) < 50) {
                mTargets[i].type = Type.TWO;
                sy = 500;
                mTargets[i].shoot(x, y, 128 + rand.nextInt(64), 128 + rand.nextInt(64), sx, sy, 3 - rand.nextInt(6));
            }
            else {
                mTargets[i].type = Type.ONE;
                sy = 800;
                mTargets[i].shoot(x, y, 128 + rand.nextInt(64), 128 + rand.nextInt(64), sx, sy, 6 - rand.nextInt(12));
            }
            break;
        }
    }

    public Type isHit(ActiveObject activeObject) {
        final int size = mTargets.length;
        for (int i = 0; i < size; i++) {
            if (mTargets[i].bActive) {
                if (mTargets[i].IsInteract(activeObject)!= HitState.NONE) {
                    return mTargets[i].type;
                }
            }
        }
        return Type.NONE;
    }

    public enum Type {NONE, ONE, TWO, THREE}

    private class Target extends ActiveObject {
        protected int x = 0, y = 0, w = 0, h = 0, t = 0, a = 255, speedX, speedY, AccelX, AccelY, nAngle, AccelA, nAlpha;
        protected boolean bActive;
        protected Type type = Type.ONE;

        public Target(int x, int y) {
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
            this.nAlpha = 255;
            this.nAngle = rand.nextInt(360);
            this.t = 0;
        }

        @Override
        public void update() {
            if (bActive) {
                nAngle += AccelA;
                x += speedX;
                y += speedY;
                speedX += AccelX;
                speedY += AccelY;
                t++;
                if (t > 10000) {
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
            HitState hs = super.IsInteract(object);
            if (hs != HitState.NONE) {
                mExplosion.SetPosAndActive(x / M, y / M, type == Type.ONE ?  Explosion.Type.TWO : Explosion.Type.THREE);
                bActive = false;
            }
            return hs;
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
