package com.shuriken.evanderoid.GameObject;

import android.graphics.Rect;

public interface Object {
    enum HitState {NONE, TOP, TOPLEFT, TOPRIGHT, LEFT, RIGHT, BOTTOMLEFT, BOTTOM, BOTTOMRIGHT}
    void update();
    Rect getLogicRect();
    Rect getPhysRect();
    HitState IsInteract(final Object object);
}
