package com.shuriken.evanderoid.Game;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;

import com.shuriken.evanderoid.R;

/**
 * Created by shu on 2016/02/25.
 */
public class GameResourceInfo {
    final Context context;
    public final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    public Bitmap bmp_title = null;
    public Bitmap bmp_explosion = null;
    public Bitmap bmp_ship = null;
    public Bitmap bmp_rocks = null;
    public Bitmap bmp_effects = null;
    public Bitmap bmp_fires = null;
    public Bitmap bmp_cloud_1 = null;
    public Bitmap bmp_cloud_2 = null;
    public Bitmap bmp_cloud_3 = null;
    public Bitmap bmp_target1 = null;
    public Bitmap bmp_target2 = null;
    public Bitmap bmp_target3 = null;
    MediaPlayer mediaPlayer = null;
    SoundPool mSoundPool = null;
    int mSndExpId, mSndPwrUP;
    private int mHighScore;
    GameResourceInfo(final Context context) {
        this.context = context;
        final Resources res = context.getResources();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bmp_title = BitmapFactory.decodeResource(res, R.drawable.spr_title_2_flattened);
        bmp_ship = BitmapFactory.decodeResource(res, R.drawable.mainship);
        bmp_explosion = BitmapFactory.decodeResource(res, R.drawable.explosion);
        bmp_effects = BitmapFactory.decodeResource(res, R.drawable.effects);
        bmp_rocks = BitmapFactory.decodeResource(res, R.drawable.rocks);
        bmp_fires = BitmapFactory.decodeResource(res, R.drawable.fire);
        bmp_cloud_1 = BitmapFactory.decodeResource(res, R.drawable.spr_cloud_1);
        bmp_cloud_2 = BitmapFactory.decodeResource(res, R.drawable.spr_cloud_2);
        bmp_cloud_3 = BitmapFactory.decodeResource(res, R.drawable.spr_cloud_3);
        bmp_target1 = BitmapFactory.decodeResource(res, R.drawable.effect);
        bmp_target2 = BitmapFactory.decodeResource(res, R.drawable.effects);
        bmp_target3 = BitmapFactory.decodeResource(res, R.drawable.effects);
        mediaPlayer = MediaPlayer.create(context, R.raw.b);
        //SoundPool.Builder builder = new SoundPool.Builder();
        mSoundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0); //builder.build();
        mSndExpId = mSoundPool.load(context, R.raw.explosion, 0);
        mSndPwrUP = mSoundPool.load(context, R.raw.power_up, 0);
        mHighScore = GetHighScore();
    }

    public int GetHighScore() {
        SharedPreferences sharedprefs= context.getSharedPreferences("game_pre" , Context.MODE_PRIVATE);
        mHighScore = sharedprefs.getInt("high_score", 0 );
        return mHighScore;
    }
    public void SetHighScore(int nScore) {
        mHighScore = GetHighScore();
        if (nScore > mHighScore) {
            SharedPreferences sharedprefs = context.getSharedPreferences("game_pre", Context.MODE_PRIVATE);
            Editor e = sharedprefs.edit();
            e.putInt("high_score", nScore);
            e.commit();
        }
        mHighScore = GetHighScore();
    }
    public void Vibrate01() {
        Vibrator vibrator = (Vibrator)(context.getSystemService(Context.VIBRATOR_SERVICE));
        long[] pattern = {0, 45, 45, 20, 5};
        vibrator.vibrate(pattern, -1);

    }

    public void Vibrate02() {
        Vibrator vibrator = (Vibrator)(context.getSystemService(Context.VIBRATOR_SERVICE));
        long[] pattern = {0, 25, 15, 5};
        vibrator.vibrate(pattern, -1);

    }

    public void SndExplosion() {
        mSoundPool.play(mSndExpId, 1.0F, 1.0F, 0, 0, 1.0F);
    }

    public void SndPowerUP() {
        mSoundPool.play(mSndPwrUP, 1.0F, 1.0F, 0, 0, 1.0F);
    }

    public void PlayBGM() {
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }
    public void StopBGM() {
        mediaPlayer.pause();
    }
}
