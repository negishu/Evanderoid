package com.shuriken.evanderoid.GameObject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shuriken.evanderoid.Game.GameResourceInfo;

import java.util.Random;

public class Invaders extends ActiveObject {
    private final Random rand;
    private Invader[] mInvaders = null;
    private int width, height;

    public Invaders(final Random rand, int n) {
        this.rand = rand;
        mInvaders = new Invader[n];
        for (int i = 0; i < n; i++) {
            mInvaders[i] = new Invader(0, 0, 56, 56);
            mInvaders[i].ch = i;
        }
    }

    public void SetScreenSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void Init(int nLevel) {
        final int size = mInvaders.length;
        for (int i = 0; i < size; i++) {
            mInvaders[i].x = (((i % 10) - 5) * 64 + (width / 2)) * M;
            mInvaders[i].y = ((i / 10) * 128 + 100) * M;
            mInvaders[i].sx = 150;
            mInvaders[i].bActive = true;
        }
    }

    @Override
    public final Rect getLogicRect() {
        return null;
    }

    @Override
    public final Rect getPhysRect() { return null; }

    @Override
    public void update() {
    }

    public void update(Bullets bullets) {
        final int size = mInvaders.length;
        boolean bIsSomeActive = false;
        for (int i = 0; i < size; i++) {
            if (mInvaders[i].bActive) {
                bIsSomeActive = true;
                Rect rc = mInvaders[i].getLogicRect();
                if (rand.nextInt(1000) < 1) {
                    bullets.shoot((int) rc.centerX(), (int) rc.centerY(), 0, +1000);
                }
            }
            mInvaders[i].update();
        }

        if (bIsSomeActive == false) {
            Init(0);
        }
    }

    @Override
    public void drawSimple(final Canvas canvas, final Paint paint) {
    }

    @Override
    public void draw(final Canvas canvas, final GameResourceInfo aGameResourceInfo) {
        final int size = mInvaders.length;
        Rect src = new Rect();
        aGameResourceInfo.paint.setAlpha(255);
        for (int i = 0; i < size; i++) {
            if (mInvaders[i].bActive) {
                final Rect physRC = mInvaders[i].getPhysRect();
                src.set((mInvaders[i].ch % 10) * 118 + 4, (mInvaders[i].ch / 10) * 118, (mInvaders[i].ch % 10) * 118 + 94, (mInvaders[i].ch / 10) * 118 + 90);
                canvas.save();
                canvas.rotate(mInvaders[i].dg - 25 + mInvaders[i].ex_dg, physRC.centerX(), physRC.centerY());
                //aGameResourceInfo.paint.setAlpha(255);
                //aGameResourceInfo.paint.setStrokeWidth(1);
                //aGameResourceInfo.paint.setStyle(Paint.Style.STROKE);
                //canvas.drawRect(dstrf, aGameResourceInfo.paint);
                //canvas.drawOval(dstrf, aGameResourceInfo.paint);
                canvas.drawBitmap(aGameResourceInfo.bmp_ship, src, physRC, aGameResourceInfo.paint);
                canvas.restore();
            }
        }
    }

    public boolean isHit(Bullets bullets) {
        boolean bRet = false;
        final int size = mInvaders.length;
        for (int i = 0; i < size; i++) {
            if (mInvaders[i].bActive) {
                if (bullets.isHitUp(mInvaders[i])) {
                    mInvaders[i].bActive = false;
                    bRet = true;
                }
            }
        }
        return bRet;
    }

    private class Invader extends ActiveObject {
        protected int x;
        protected int y;
        protected int w;
        protected int h;
        protected int sx;
        protected int sy;
        protected int dg, ex_dg;
        protected int ch;
        protected int t;
        protected boolean bActive;

        public Invader(int x, int y, int w, int h) {
            this.x = x * M;
            this.y = y * M;
            this.w = w * M;
            this.h = h * M;
            this.sx = 100;
            this.sy = 0;
            this.dg = 180;
            this.ex_dg = 0;
            this.ch = 0;
            this.t = 0;
            bActive = false;
        }

        @Override
        public final Rect getLogicRect() {
            final int X = x;
            final int Y = y;
            final int HX = w / 2;
            final int HY = h / 2;
            logicRC.left   = X - HX - 1;
            logicRC.top    = Y - HY - 1;
            logicRC.right  = X + HX + 1;
            logicRC.bottom = Y + HY + 1;
            return logicRC;
        }

        @Override
        public final Rect getPhysRect() {
            final int X = x;
            final int Y = y;
            final int HX = w / 2;
            final int HY = h / 2;
            physRC.left   = (X - HX - 1) / M;
            physRC.top    = (Y - HY - 1) / M;
            physRC.right  = (X + HX + 1) / M;
            physRC.bottom = (Y + HY + 1) / M;
            return physRC;
        }

        @Override
        public void update() {
            t++;
            if (bActive) {
                x += sx;
                final Rect rc = getLogicRect();
                if (0 < rc.left && rc.right < width * M) {

                } else {
                    sx = -sx;
                    y += 64 * M;
                }
                if ((t % 100) < 50) {
                    ex_dg++;
                } else {
                    ex_dg--;
                }
            }
        }
    }
}
