package com.shuriken.evanderoid.GameObject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shuriken.evanderoid.Game.GameResourceInfo;

import java.util.Random;

public class Bullets extends ActiveObject {
    private final Random rand;
    private Bullet[] mBullets = null;
    private int width, height;

    public Bullets(final Random rand, int n) {
        mBullets = new Bullet[n];
        for (int i = 0; i < n; i++) {
            mBullets[i] = new Bullet(0, 0, 0, 0);
        }
        this.rand = rand;
    }

    public void SetScreenSize(int width, int height) {
        this.width = width;
        this.height = height;
        final int size = mBullets.length;
        for (int i = 0; i < size; i++) {
            mBullets[i].mExplosion.SetScreenSize(width, height);
        }
    }

    public void Init(int nLevel) {
        final int size = mBullets.length;
        for (int i = 0; i < size; i++) {
            mBullets[i].x = 0;
            mBullets[i].y = 0;
            mBullets[i].nAngle = rand.nextInt(360);
            mBullets[i].bActive = false;
        }
    }

    @Override
    public void update() {
        final int size = mBullets.length;
        for (int i = 0; i < size; i++) {
            mBullets[i].update();
        }
    }

    @Override
    public void drawSimple(final Canvas canvas, final Paint paint) {
        final int size = mBullets.length;
        for (int i = 0; i < size; i++) {
            mBullets[i].drawSimple(canvas, paint);
        }
    }

    @Override
    public void draw(final Canvas canvas, final GameResourceInfo aGameResourceInfo) {
        final int size = mBullets.length;
        for (int i = 0; i < size; i++) {
            if (mBullets[i].bActive) {
                final Rect physRC = mBullets[i].getPhysRect();
                Rect src = new Rect();
                src.set(0, 0, 332, 332);
                canvas.save();
                canvas.rotate(mBullets[i].nAngle % 360, physRC.centerX(), physRC.centerY());
                aGameResourceInfo.paint.setAlpha(255);
                canvas.drawBitmap(aGameResourceInfo.bmp_effects, src, physRC, aGameResourceInfo.paint);
                canvas.restore();
            }
            mBullets[i].mExplosion.draw(canvas, aGameResourceInfo);
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

    public void shoot(int x, int y, int sx, int sy) {
        final int size = mBullets.length;
        for (int i = 0; i < size; i++) {
            if (mBullets[i].bActive) continue;
            mBullets[i].shoot(x, y, sx, sy);
            break;
        }
    }

    public boolean isHitUp(ActiveObject o) {
        final int size = mBullets.length;
        for (int i = 0; i < size; i++) {
            if (!(mBullets[i].bActive)) continue;
            if (mBullets[i].speedY < 0 && mBullets[i].IsInteract(o) != HitState.NONE) {
                mBullets[i].bActive = false;
                return true;
            }
        }
        return false;
    }

    public boolean isHitDown(ActiveObject o) {
        final int size = mBullets.length;
        for (int i = 0; i < size; i++) {
            if (!(mBullets[i].bActive)) continue;
            if (mBullets[i].speedY > 0 && mBullets[i].IsInteract(o) != HitState.NONE) {
                mBullets[i].bActive = false;
                return true;
            }
        }
        return false;
    }

    private class Bullet extends ActiveObject {
        private static final int SIZE = 24;
        private final Explosion mExplosion = new Explosion();
        private int x = 0, y = 0, a = 255, size, speedX, speedY, AccelX, AccelY, nAngle;
        private boolean bActive;

        public Bullet(int x, int y, int sx, int sy) {
            this.speedX = sx;
            this.speedY = sy;
            this.size = SIZE * M;
            this.x = x * M;
            this.y = y * M;
            this.AccelX = 0;
            this.AccelY = 0;
            this.bActive = false;
            this.nAngle = 0;
        }

        public void shoot(int x, int y, int sx, int sy) {
            this.speedX = sx;
            this.speedY = sy;
            this.x = x;
            this.y = y;
            this.AccelX = 0;
            this.AccelY = 0;
            this.bActive = true;
        }

        @Override
        public void update() {
            if (bActive) {
                this.nAngle++;
                x += speedX;
                y += speedY;
                speedX += AccelX;
                speedY += AccelY;
                if (y < (-100 * M)) bActive = false;
                if (y > (height + 100) * M) bActive = false;
            }
            mExplosion.update();
        }

        @Override
        public final Rect getLogicRect() {
            final int X = x;
            final int Y = y;
            final int WH = size / 2;
            logicRC.left = (int) X - WH - 1;
            logicRC.top = (int) Y - WH - 1;
            logicRC.right = (int) X + WH + 1;
            logicRC.bottom = (int) Y + WH + 1;
            return logicRC;
        }

        @Override
        public final Rect getPhysRect() {
            final int X = x;
            final int Y = y;
            final int WH = size / 2;
            physRC.left   = (X - WH - 1) / M;
            physRC.top    = (Y - WH - 1) / M;
            physRC.right  = (X + WH + 1) / M;
            physRC.bottom = (Y + WH + 1) / M;
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
    }
}
