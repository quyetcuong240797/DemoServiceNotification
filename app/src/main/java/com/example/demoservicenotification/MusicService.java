package com.example.demoservicenotification;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service implements IPlayMusic, MediaPlayer.OnCompletionListener {
    private final IBinder mBinder = new LocalBinder();
    private MediaPlayer mMediaPlayer;
    private int mCurrentIndex;
    private OnSyncActivityListerner mListerner;
    public static final int NUMBER_1 = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }




    @Override
    public void create(int index) {
        mCurrentIndex = index;
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        int resourceSong = MainActivity.arrayListSongs.get(mCurrentIndex).getFile();
        mMediaPlayer = MediaPlayer.create(MusicService.this, resourceSong);
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnCompletionListener(MusicService.this);
        }
    }

    @Override
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mListerner.syncSeekbar(mMediaPlayer.getDuration());
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }

    }

    @Override
    public int getDuration() {
        return mMediaPlayer != null ? mMediaPlayer.getDuration() : 0;
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void seek(int position) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(position);
        }
    }

    @Override
    public void loop(boolean isLoop) {

    }

    @Override
    public int getSong() {
        return 0;
    }

    @Override
    public void stopService() {

    }

    @Override
    public void changeSong(int i) {
        mCurrentIndex += i;
        if (mCurrentIndex >= MainActivity.arrayListSongs.size()) {
            mCurrentIndex = 0;
        } else if (mCurrentIndex < 0) {
            mCurrentIndex = MainActivity.arrayListSongs.size() - NUMBER_1;
        }
        this.create(mCurrentIndex);
        this.start();
    }

    @Override
    public void onCompletion(MediaPlayer mMediaPlayerp) {
        this.changeSong(MainActivity.NEXT_SONG);


    }

    public void setSyncSeekbarListerner(OnSyncActivityListerner listerner) {
        mListerner = listerner;
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public class LocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }


    interface OnSyncActivityListerner {
        void syncSeekbar(int max);

        void syncNotification(boolean isPlaying);
    }
}
