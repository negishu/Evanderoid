package com.shuriken.evanderoid.GameObject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shuriken.evanderoid.Game.GameResourceInfo;

public abstract class ActiveObject implements Object {
    public static final int M = 100;
    protected int tm = 0;
    protected final Rect src = new Rect();
    protected final Rect logicRC = new Rect();
    protected final Rect physRC  = new Rect();

    @Override
    public HitState IsInteract(final Object object) {
        final Rect rObj = object.getLogicRect();
        final Rect rThis = getLogicRect();
        HitState aHitState = HitState.NONE;
        if (rObj.top <= rThis.bottom && rObj.bottom >= rThis.top && rObj.right >= rThis.left && rObj.left <= rThis.right) {
            if (rObj.top <= rThis.top && rObj.bottom >= rThis.top) {
                aHitState = HitState.TOP;
                if (rObj.left < rThis.left) {
                    aHitState = HitState.TOPLEFT;
                }
                if (rObj.right > rThis.right) {
                    aHitState = HitState.TOPRIGHT;
                }
            } else if (rObj.top <= rThis.bottom && rObj.bottom > rThis.bottom) {
                aHitState = HitState.BOTTOM;
                if (rObj.left < rThis.left) {
                    aHitState = HitState.BOTTOMLEFT;
                }
                if (rObj.right > rThis.right) {
                    aHitState = HitState.BOTTOMRIGHT;
                }
            } else if (rObj.top >= rThis.top && rObj.bottom <= rThis.bottom) {
                if (rObj.left < rThis.left) {
                    aHitState = HitState.LEFT;
                }
                if (rObj.right > rThis.right) {
                    aHitState = HitState.RIGHT;
                }
            }
        }
        return aHitState;
    }

    public void drawSimple(final Canvas canvas, final Paint paint) {
    }

    public void draw(final Canvas canvas, final GameResourceInfo aGameResourceInfo) {
    }
}