package com.shuriken.evanderoid.GameObject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shuriken.evanderoid.Game.GameResourceInfo;

import java.util.Random;

public class Rocks extends ActiveObject {
    private final Random rand;
    private Rock[] mRocks = null;
    private int width, height;
    private int pos_x[] = new int[360];
    private int pos_y[] = new int[360];
    private final Rect src = new Rect();
    protected final Explosion mExplosion = new Explosion();

    public Rocks(final Random rand, int n) {
        mRocks = new Rock[n];
        for (int i = 0; i < n; i++) {
            mRocks[i] = new Rock(0, 0);
        }
        this.rand = rand;
        for (int i = 0; i < 360; i++) {
            pos_x[i] = (int)(Math.cos(Math.PI / 180 * i) * 120000);
            pos_y[i] = (int)(Math.sin(Math.PI / 180 * i) * 120000);
        }
    }

    public void SetScreenSize(int width, int height) {
        this.width  = width;
        this.height = height;
        mExplosion.SetScreenSize(width, height);
    }

    public void Init(int nLevel) {
        final int size = mRocks.length;
        for (int i = 0; i < size; i++) {
            mRocks[i].x = 0;
            mRocks[i].y = 0;
            mRocks[i].nAngle = rand.nextInt(360);
            mRocks[i].bActive = false;
        }
        tm = 0;
    }

    @Override
    public void update() {
        final int size = mRocks.length;
        for (int i = 0; i < size; i++) {
            mRocks[i].update();
        }
        mExplosion.update();
        tm ++;
    }

    public void update(final int x, final int y) {
        if (tm % 400 == 0) {
            shoot(x, y, 45);
        }
        if (tm % 300 == 0) {
            shoot(x, y, 60);
        }
        update();
    }

    @Override
    public void drawSimple(final Canvas canvas, final Paint paint) {
        final int size = mRocks.length;
        for (int i = 0; i < size; i++) {
            mRocks[i].drawSimple(canvas, paint);
        }
    }

    @Override
    public void draw(Canvas canvas, final GameResourceInfo aGameResourceInfo) {
        aGameResourceInfo.paint.setAlpha(255);
        final int size = mRocks.length;
        for (int i = 0; i < size; i++) {
            if (mRocks[i].bActive) {
                final Rect physRC = mRocks[i].getPhysRect();
                canvas.save();
                if (mRocks[i].type == Type.ROCK) {
                    //mRocks[i].drawsimple(canvas, aGameResourceInfo.paint);
                    final int nWidth  = (aGameResourceInfo.bmp_rocks.getWidth());
                    final int nHeight = (aGameResourceInfo.bmp_rocks.getHeight());
                    src.set(0, 0, nWidth, nHeight);
                    //canvas.rotate(mRocks[i].nAngle % 360, physRC.centerX(), physRC.centerY());
                    canvas.drawBitmap(aGameResourceInfo.bmp_rocks, src, physRC, aGameResourceInfo.paint);
                }
                else
                if (mRocks[i].type == Type.FIRE) {
                    //mRocks[i].drawsimple(canvas, aGameResourceInfo.paint);
                    final int nWidth  = (aGameResourceInfo.bmp_fires.getWidth() / 8);
                    final int nHeight = (aGameResourceInfo.bmp_fires.getHeight()/ 4);
                    src.set(nWidth * ((mRocks[i].t) % 8), 0, nWidth * ((mRocks[i].t) % 8) + nWidth, nHeight);
                    canvas.drawBitmap(aGameResourceInfo.bmp_fires, src, physRC, aGameResourceInfo.paint);
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
        final int size = mRocks.length;
        for (int i = 0; i < size; i++) {
            if (mRocks[i].bActive) continue;
            //mRocks[i].type = Type.FIRE;
            //mRocks[i].shoot(x, y-10000, 192, 256, sx, sy, rand.nextInt(5)+1);
            break;
        }
    }

    public void shoot(int x, int y, int dg) {
        final int size = mRocks.length;
        int m = 1;
        if (rand.nextInt(100) < 50) {
            for (int a = 200, n = 0, i = 0; i < size; i++) {
                if (mRocks[i].bActive) continue;
                int xx = (x - pos_x[n]);
                int yy = (y - pos_y[n]);
                int sx = (x - xx) / (300 - (a));
                int sy = (y - yy) / (300 - (a));
                if (m > 0) {
                    if (rand.nextInt(100) < 50) {
                        m--;
                    }
                    else {
                        mRocks[i].type = Type.ROCK;
                        mRocks[i].shoot(xx, yy, 20, 20, sx, sy, 8);
                    }
                }
                else {
                    mRocks[i].type = Type.ROCK;
                    mRocks[i].shoot(xx, yy, 20, 20, sx, sy, 8);
                }
                n += dg;
                if (n >= 360) {
                    break;
                }
            }
        }
        else {
            for (int a = 200, n = 0, i = 0; i < size; i++) {
                if (mRocks[i].bActive) continue;
                int xx = (x - pos_x[n]);
                int yy = (y - pos_y[n]);
                int sx = (x - xx) / (300 - (a));
                int sy = (y - yy) / (300 - (a));
                if (m > 0) {
                    if (rand.nextInt(100) < 50) {
                        m--;
                    }
                    else {
                        mRocks[i].type = Type.ROCK;
                        mRocks[i].shoot(xx, yy, 20, 20, sx, sy, 8);
                    }
                }
                else {
                    mRocks[i].type = Type.ROCK;
                    mRocks[i].shoot(xx, yy, 20, 20, sx, sy, 8);
                }
                n += dg;
                if (n >= 360) {
                    break;
                }
            }
        }
    }

    public boolean isHit(ActiveObject activeObject) {
        boolean bRet = false;
        final int size = mRocks.length;
        for (int i = 0; i < size; i++) {
            if (mRocks[i].bActive) {
                if (mRocks[i].IsInteract(activeObject)!= HitState.NONE) {
                    mRocks[i].bActive = false;
                    bRet = true;
                }
            }
        }
        return bRet;
    }

    enum Type {FIRE, ROCK}

    private class Rock extends ActiveObject {
        protected int x = 0, y = 0, w = 0, h = 0, t = 0, a = 255, speedX, speedY, AccelX, AccelY, nAngle, AccelA;
        protected boolean bActive;
        protected Type type = Type.ROCK;

        public Rock(int x, int y) {
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
            this.bActive = false;
        }

        public void shoot(int x, int y, int w, int h, int sx, int sy, int accelA) {
            this.speedX = sx;
            this.speedY = sy;
            this.x = x;
            this.y = y;
            this.w = w * M;
            this.h = h * M;
            this.AccelX = 0;
            this.AccelY = 0;
            this.AccelA = accelA;
            this.bActive = true;
            this.t = 0;
        }

        @Override
        public void update() {
            if (bActive) {
                t++;
                if (type == Type.FIRE) {
                    if (t > 800) {
                        bActive = false;
                        t = 0;
                    }
                    /*
                    if (0 < t && t < 200) {

                    } else {
                        x += speedX;
                        y += speedY;
                        speedX += AccelX;
                        speedY += AccelY;
                    }
                    */

                    x += speedX;
                    y += speedY;
                    speedX += AccelX;
                    speedY += AccelY;

                }
                else {
                    if (t > 500) {
                        bActive = false;
                        t = 0;
                    }
                    x += speedX;
                    y += speedY;
                    speedX += AccelX;
                    speedY += AccelY;
/*
                    if (0 < t && t < 100) {

                    } else if (200 < t && t < 300) {

                    } else {
                        x += speedX;
                        y += speedY;
                        speedX += AccelX;
                        speedY += AccelY;
                    }
*/
                }
                nAngle += AccelA;
            }
        }

        @Override
        public final Rect getLogicRect() {
            logicRC.left   = x - (w/3);
            logicRC.top    = y - (h/3);
            logicRC.right  = x + (w/3);
            logicRC.bottom = y + (h/3);
            if (type == Type.FIRE) {
                logicRC.left   = x - (w/8);
                logicRC.right  = x + (w/8);
                logicRC.top = y;
            }
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
                mExplosion.SetPosAndActive(x / M, y / M, Explosion.Type.ONE);
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
        }
    }
}
