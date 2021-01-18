package com.example.thiefalarm;

import android.content.Context;
import android.media.MediaPlayer;

public class AlertSoundPlayer {

    private MediaPlayer mediaPlayer;

    public AlertSoundPlayer(Context context)
    {

        mediaPlayer = MediaPlayer.create(context,R.raw.siren_noise);
        mediaPlayer.setLooping(true);
    }


    void play()
    {
        if(!mediaPlayer.isPlaying())
            mediaPlayer.start();
    }
    void stop()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
