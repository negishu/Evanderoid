package com.shuriken.evanderoid.Game;

import android.os.SystemClock;

import com.shuriken.evanderoid.GameObject.Bullets;
import com.shuriken.evanderoid.GameObject.Clouds;
import com.shuriken.evanderoid.GameObject.Invaders;
import com.shuriken.evanderoid.GameObject.MyShip;
import com.shuriken.evanderoid.GameObject.Rocks;
import com.shuriken.evanderoid.GameObject.Stars;
import com.shuriken.evanderoid.GameObject.Targets;

import java.util.Random;

/**
 * Created by shu on 3/17/2016.
 */
public class GameData {

    protected final Random rand = new Random();
    protected enum GAME_STATE {TITLE, READY, ACTIVE, ACTIVE_END, FINISHED, EXIT}
    protected GAME_STATE mStata = GAME_STATE.TITLE;
    protected int width, height, mposx, mposy, mdgr, game_time, dist, score, score_add, shootcnt, initx, inity, lastx, lasty, newx, newy, energy;
    protected long m_timer;
    protected Stars mStars = null;
    protected Bullets mBullets = null;
    protected Rocks mRocks = null;
    protected MyShip mMyShip = null;
    protected Invaders mInvaders = null;
    protected Clouds mClouds = null;
    protected Targets mTargets = null;

    public GameData() {

        mStars = new Stars(rand, 32);
        mBullets = new Bullets(rand, 0);
        mRocks = new Rocks(rand, 64);
        mMyShip = new MyShip(rand);
        mInvaders = new Invaders(rand, 0);
        mClouds = new Clouds(rand, 16);
        mTargets = new Targets(rand, 16);

        game_time = 0;
        score = 0;
        score_add = 0;
        shootcnt = 0;
        lastx = 0;
        lasty = 0;
        newx = 0;
        newy = 0;
        dist = 0;
        energy = 1000;

        m_timer = SystemClock.currentThreadTimeMillis();
    }
}
