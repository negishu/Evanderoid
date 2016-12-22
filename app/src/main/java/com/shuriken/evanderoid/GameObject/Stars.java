package com.shuriken.evanderoid.GameObject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shuriken.evanderoid.Game.GameResourceInfo;

import java.util.Random;

public class Stars extends ActiveObject {
    private final Random rand;
    private Star[] mStars = null;
    private int width, height;

    public Stars(final Random rand, int n) {
        mStars = new Star[n];
        for (int i = 0; i < n; i++) {
            mStars[i] = new Star(0, 0, 0, 0, 0);
        }
        this.rand = rand;
    }

    public void SetScreenSize(int width, int height) {
        this.width = width;
        this.height = height;
        final int size = mStars.length;
        for (int i = 0; i < size; i++) {
            int x = (rand.nextInt(width * M));
            int y = (rand.nextInt(height * M));
            int s = (rand.nextInt(5) + 10) * M;
            int r = (rand.nextInt(4) + 2);
            int a = (rand.nextInt(172) + 64);
            mStars[i] = new Star(x, y, s, r, a);
        }
    }

    public void update() {
        update(1);
    }
    public void update(int b) {
        final int size = mStars.length;
        for (int i = 0; i < size; i++) {
            mStars[i].update(b);
            if (mStars[i].y > height * M) {
                int x = (rand.nextInt(width * M));
                int y = -100;
                int s = (rand.nextInt(5) + 10) * M;
                int r = (rand.nextInt(4) + 2);
                int a = (rand.nextInt(172) + 64);
                mStars[i].x = x;
                mStars[i].y = y;
                mStars[i].s = s;
                mStars[i].r = r;
                mStars[i].a = a;
            }
        }
    }

    @Override
    public void draw(final Canvas canvas, final GameResourceInfo aGameResourceInfo) {
        aGameResourceInfo.paint.setColor(Color.WHITE);
        aGameResourceInfo.paint.setStyle(Paint.Style.FILL);
        final int size = mStars.length;
        for (int i = 0; i < size; i++) {
            aGameResourceInfo.paint.setAlpha(mStars[i].a);
            aGameResourceInfo.paint.setStrokeWidth(mStars[i].r);
            canvas.drawPoint(mStars[i].x / M, mStars[i].y / M, aGameResourceInfo.paint);
        }
        aGameResourceInfo.paint.setStrokeWidth(1);
        aGameResourceInfo.paint.setAlpha(255);
    }

    @Override
    public final Rect getLogicRect() {
        return null;
    }

    @Override
    public final Rect getPhysRect() {
        return null;
    }

    private class Star extends ActiveObject {
        public int x, y, s, r, a;
        public Star(int x, int y, int s, int r, int a) {
            this.x = x;
            this.y = y;
            this.s = s;
            this.r = r;
            this.a = a;
        }
        public void update(int b) {
            if (r > 2) {
                y += s * (b > 32 ? Math.min((b / 32), 5) : 1);
            } else {
                y += s * 1;
            }
        }
        @Override
        public void update() {
            y += s;
        }
        @Override
        public final Rect getLogicRect() {
            return null;
        }
        @Override
        public final Rect getPhysRect() {
            return null;
        }
        @Override
        public HitState IsInteract(Object object) {
            return HitState.NONE;
        }
    }
}
