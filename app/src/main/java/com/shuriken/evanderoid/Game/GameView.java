package com.shuriken.evanderoid.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private GameResourceInfo mGameResourceInfo = null;
    private Thread thread = null;
    private SurfaceHolder holder = null;
    private GameMain theGame;
    private boolean execFlg = true;
    private boolean pauseFlg = false;

    public GameView(Context context) {
        super(context);
        mGameResourceInfo = new GameResourceInfo(context);
        theGame = new GameMain(mGameResourceInfo);
        holder = getHolder();
        holder.setFormat(0x00000004);
        holder.addCallback(this);
        setFocusable(true);
        pauseFlg = false;
    }

    public void restartLoop() {
        execFlg = true;
        thread = new Thread(this);
        thread.start();
    }

    public void endLoop() {
        if (thread != null) {
            synchronized (thread) {
                execFlg = false;
            }
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        thread = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        restartLoop();
        if (pauseFlg) {
            mGameResourceInfo.mediaPlayer.start();
            pauseFlg = false;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        theGame.SetScreenSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mGameResourceInfo.mediaPlayer.isPlaying()) {
            mGameResourceInfo.mediaPlayer.pause();
            pauseFlg = true;
        }
        endLoop();
        execFlg = false;
        thread = null;
    }

    @Override
    public void run() {
        while (execFlg) {
            GameUpdate();
            GameDraw();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        theGame.onTouchEvent(event);
        return true;
    }

    private void GameUpdate() {
        theGame.GameUpdate();
    }

    private void GameDraw() {
        if (holder.getSurface().isValid()) {
            final Canvas canvas = holder.lockCanvas();
            theGame.GameDraw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }
}