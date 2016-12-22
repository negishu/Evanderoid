package com.shuriken.evanderoid.Game;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.shuriken.evanderoid.GameObject.Targets;

public class GameMain extends GameData {
    protected final GameResourceInfo mGameResourceInfo;
    protected boolean isInitialTouch = false;
    public GameMain(GameResourceInfo aGameResourceInfo) {
        mGameResourceInfo = aGameResourceInfo;
    }
    public void SetScreenSize(int width, int height) {
        this.width = width;
        this.height = height;
        mStars.SetScreenSize(width, height);
        mBullets.SetScreenSize(width, height);
        mRocks.SetScreenSize(width, height);
        mMyShip.SetScreenSize(width, height);
        mInvaders.SetScreenSize(width, height);
        mClouds.SetScreenSize(width, height);
        mTargets.SetScreenSize(width, height);
    }

    public void onTouchEvent(MotionEvent me) {
        switch (mStata) {
            case TITLE:
                onTouchEventTITLE(me);
                break;
            case READY:
                onTouchEventREADY(me);
                break;
            case ACTIVE:
                onTouchEventACTIVE(me);
                break;
            case ACTIVE_END:
                onTouchEventACTIVE(me);
                break;
            case FINISHED:
                onTouchEventFINISHED(me);
                break;
            case EXIT:
                break;
        }

        final int action = me.getAction();
        if (action == MotionEvent.ACTION_UP) {
            isInitialTouch = false;
        }
    }

    public void onTouchEventTITLE(MotionEvent me) {
        final int action = me.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mStata = GameData.GAME_STATE.READY;
            score_add = 0;
        }
    }

    public void onTouchEventREADY(MotionEvent me) {
        final int action = me.getAction();
        final int x = (int) me.getX();
        final int y = (int) me.getY();
        if (action == MotionEvent.ACTION_DOWN) {
            mMyShip.Init();
            mGameResourceInfo.PlayBGM();
            dist = 0;
            score_add = 0;
            if (!isInitialTouch) {
                isInitialTouch = true;
                initx = x;
                inity = y;
                Rect rc = mMyShip.getPhysRect();
                mposx = rc.centerX();
                mposy = rc.centerY();
                int dg = mMyShip.getTurnDegrees();
                mdgr = dg;
            }
            mStata = GameData.GAME_STATE.ACTIVE;
        }
    }

    public void onTouchEventACTIVE(MotionEvent me) {
        final int action = me.getAction();
        final int x = (int) me.getX();
        final int y = (int) me.getY();
        if (action == MotionEvent.ACTION_DOWN) {
            if (!isInitialTouch) {
                isInitialTouch = true;
                initx = x;
                inity = y;
            }
            Rect physRC = mMyShip.getPhysRect();
            mposx = physRC.centerX();
            mposy = physRC.centerY();
        }
        else if (action == MotionEvent.ACTION_MOVE) {
            if (isInitialTouch) {
                int dx = initx - x;
                int dy = inity - y;
                Rect physRC = mMyShip.getPhysRect();
                mposx = physRC.centerX();
                mposy = physRC.centerY();
                mMyShip.MoveTo((mposx - dx), (mposy - dy));
                initx = x;
                inity = y;
            }
        }
    }

    public void onTouchEventFINISHED(MotionEvent me) {
        final int action = me.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mStata = GameData.GAME_STATE.READY;
            score = 0;
            dist  = 0;
            score_add = 0;
            energy = 1000;
            mMyShip.Init();
            mRocks.Init(0);
            mBullets.Init(0);
            mInvaders.Init(0);
            mTargets.Init(0);
        }
    }

    public void GameUpdate() {
        switch (mStata) {
            case TITLE:
                mStars.update();
                mClouds.update();
            break;
            case READY:
                mStars.update();
                mTargets.update();
                mClouds.update();
                mInvaders.update(mBullets);
                mRocks.update();
                mBullets.update();
            break;
            case ACTIVE:
            case ACTIVE_END:
                GameUpdateActive();
            break;
            case FINISHED:
                mStars.update();
                mTargets.update();
                mClouds.update();
                mRocks.update();
                mBullets.update();
                mMyShip.update();
            break;
            case EXIT:
                break;
        }
        game_time++;
    }

    private void GameUpdateActive() {
        final Rect aLogicRC = mMyShip.getLogicRect();
        mStars.update(score_add);
        mTargets.update(aLogicRC.centerX(), aLogicRC.centerY());
        mRocks.update(aLogicRC.centerX(), aLogicRC.centerY());
        mClouds.update(score_add, aLogicRC.centerX(), aLogicRC.centerY());
        mMyShip.update();
        if (mMyShip.isHitRock(mRocks) || mMyShip.isHitBullet(mBullets) || energy == 0) {
            mGameResourceInfo.SndExplosion();
            mGameResourceInfo.Vibrate01();
            mGameResourceInfo.StopBGM();
            mGameResourceInfo.SetHighScore(score);
            mStata = GameData.GAME_STATE.FINISHED;
        }
        else {
            Targets.Type type = mTargets.isHit(mMyShip);
            if (type == Targets.Type.ONE) {
                mGameResourceInfo.SndPowerUP();
                mGameResourceInfo.Vibrate02();
                score_add = 300;
                energy = Math.min(1000, energy + 150);
            }
            else if (type == Targets.Type.TWO) {
                mGameResourceInfo.SndPowerUP();
                mGameResourceInfo.Vibrate02();
                score_add = 600;
                energy = Math.min(1000, energy + 100);
            }
            if (mMyShip.isReady() == true) {
                dist++;
                mStata = GameData.GAME_STATE.ACTIVE;
            }

            if ((dist % 20) == 0) {
                score++;
            }

            if (energy > 0) {
                energy--;
            }

            if (score_add > 0) {
                score_add--;
                score++;
            }
        }
    }

    private void DrawTitle(final Canvas canvas) {
        final int nWidth  = (mGameResourceInfo.bmp_title.getWidth() / 2);
        final int nHeight = (mGameResourceInfo.bmp_title.getHeight()/ 2);
        final Rect rc = new Rect((width/2) - nWidth/2, (height/2) - nHeight/2, (width/2) + nWidth/2, (height/2) + nHeight/2);
        final Rect src = new Rect(0 , 0, nWidth*2, nHeight*2);
        canvas.drawBitmap(mGameResourceInfo.bmp_title, src, rc, mGameResourceInfo.paint);
    }

    public void GameDraw(final Canvas canvas) {
        switch (mStata) {
            case TITLE:
                canvas.drawColor(Color.argb(255, 0x66, 0xcc, 0xff));
                mStars.draw(canvas, mGameResourceInfo);
                mTargets.draw(canvas, mGameResourceInfo);
                DrawTitle(canvas);
                mGameResourceInfo.paint.setTextSize(80);
                mGameResourceInfo.paint.setColor(Color.WHITE);
                mGameResourceInfo.paint.setAlpha(255);
                mGameResourceInfo.paint.setTextAlign(Paint.Align.CENTER);
                mGameResourceInfo.paint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawText("Tap to Start ", width / 2, height / 2 + 120, mGameResourceInfo.paint);
                mClouds.draw(canvas, mGameResourceInfo);
                break;
            case READY:
                canvas.drawColor(Color.argb(255, 0x66, 0xcc, 0xff));
                mStars.draw(canvas, mGameResourceInfo);
                mTargets.draw(canvas, mGameResourceInfo);
                mClouds.draw(canvas, mGameResourceInfo);
                mRocks.draw(canvas, mGameResourceInfo);
                mInvaders.draw(canvas, mGameResourceInfo);
                mBullets.draw(canvas, mGameResourceInfo);
                mMyShip.draw(canvas, mGameResourceInfo);
                mGameResourceInfo.paint.setTextSize(80);
                mGameResourceInfo.paint.setColor(Color.WHITE);
                mGameResourceInfo.paint.setAlpha(255);
                mGameResourceInfo.paint.setTextAlign(Paint.Align.CENTER);
                mGameResourceInfo.paint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawText("Ready", width / 2, height / 2 - 160, mGameResourceInfo.paint);
                break;
            case ACTIVE:
                canvas.drawColor(Color.argb(255, 0x66, 0xcc, 0xff));
                //canvas.drawColor(Color.argb(255, 0, 0, 0));
                mStars.draw(canvas, mGameResourceInfo);
                mGameResourceInfo.paint.setColor(Color.WHITE);
                mGameResourceInfo.paint.setAlpha(255);
                mGameResourceInfo.paint.setTextSize(36);
                int mx = ((width - 24) * energy) /1000;
                canvas.drawRect(10, 190, mx + 2, 220, mGameResourceInfo.paint);
                mGameResourceInfo.paint.setColor(Color.BLUE);
                mGameResourceInfo.paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(12, 192, mx, 218, mGameResourceInfo.paint);
                mGameResourceInfo.paint.setTextSize(48);
                mGameResourceInfo.paint.setColor(Color.WHITE);
                mGameResourceInfo.paint.setTextAlign(Paint.Align.LEFT);
                mGameResourceInfo.paint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawText("Energy", 10, 170, mGameResourceInfo.paint);

                mClouds.draw(canvas, mGameResourceInfo);
                mRocks.draw(canvas, mGameResourceInfo);
                mInvaders.draw(canvas, mGameResourceInfo);
                mBullets.draw(canvas, mGameResourceInfo);
                mTargets.draw(canvas, mGameResourceInfo);
                mMyShip.draw(canvas, mGameResourceInfo);
                canvas.drawText("Distance : " + score + " miles", 10, 260, mGameResourceInfo.paint);
                break;
            case ACTIVE_END:
                canvas.drawColor(Color.argb(255, 0x66, 0xcc, 0xff));
                mStars.draw(canvas, mGameResourceInfo);
                mTargets.draw(canvas, mGameResourceInfo);
                mGameResourceInfo.paint.setColor(Color.WHITE);
                mGameResourceInfo.paint.setAlpha(255);
                mGameResourceInfo.paint.setTextAlign(Paint.Align.CENTER);
                mGameResourceInfo.paint.setStyle(Paint.Style.FILL_AND_STROKE);
                mGameResourceInfo.paint.setTextSize(60);
                canvas.drawText("Distance : " + score + " miles", width / 2, height / 2 + 80, mGameResourceInfo.paint);
                mMyShip.draw(canvas, mGameResourceInfo);
                mTargets.draw(canvas, mGameResourceInfo);
                mRocks.draw(canvas, mGameResourceInfo);
                mInvaders.draw(canvas, mGameResourceInfo);
                mBullets.draw(canvas, mGameResourceInfo);
                mClouds.draw(canvas, mGameResourceInfo);
                break;
            case FINISHED:
                canvas.drawColor(Color.argb(255, 0x66, 0xcc, 0xff));
                mStars.draw(canvas, mGameResourceInfo);
                mTargets.draw(canvas, mGameResourceInfo);
                mRocks.draw(canvas, mGameResourceInfo);
                mInvaders.draw(canvas, mGameResourceInfo);
                mBullets.draw(canvas, mGameResourceInfo);
                mClouds.draw(canvas, mGameResourceInfo);
                DrawTitle(canvas);
                mGameResourceInfo.paint.setColor(Color.WHITE);
                mGameResourceInfo.paint.setAlpha(255);
                mGameResourceInfo.paint.setTextAlign(Paint.Align.CENTER);
                mGameResourceInfo.paint.setStyle(Paint.Style.FILL_AND_STROKE);
                mGameResourceInfo.paint.setTextSize(80);
                canvas.drawText("Game Over", width / 2, height / 2 - 100, mGameResourceInfo.paint);
                mGameResourceInfo.paint.setTextSize(80);
                int high_score = mGameResourceInfo.GetHighScore();
                canvas.drawText("Best : " + high_score + " miles", width / 2, height / 2 + 150, mGameResourceInfo.paint);
                canvas.drawText("You : " + score + " miles", width / 2, height / 2 + 240, mGameResourceInfo.paint);
                break;
            case EXIT:
                canvas.drawColor(Color.argb(255, 0x66, 0xcc, 0xff));
                mClouds.draw(canvas, mGameResourceInfo);
                mStars.draw(canvas, mGameResourceInfo);
                break;
        }
    }
}
