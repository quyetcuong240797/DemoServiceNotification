package com.example.demoservicenotification;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        OnItemClickListener, MusicService.OnSyncActivityListerner {
    private MusicService mService;
    public static final int NEXT_SONG = 1;
    public static final int PREVIOUS_SONG = -1;
    public static final int MESSAGE_DELAY = 1000;
    public static ArrayList<Song> arrayListSongs = new ArrayList<>();
    private ImageView img_view_play, img_view_pause, img_view_next, img_view_prev;
    private SeekBar mSeekbar;
    private TextView tv_start_time, tv_total_time, tv_name_song;
    private RecyclerView mRecyclerView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentIndex = mService.getCurrentIndex();
            int currentPosition = mService.getCurrentPosition();
            mSeekbar.setProgress(currentPosition);
            tv_name_song.setText(arrayListSongs.get(currentIndex).getmNameSong());
            tv_start_time.setText(convertMilisecondToFormatTime(currentPosition));
            mHandler.sendMessageDelayed(new Message(), MESSAGE_DELAY);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addDataSong();
        initViews();
        Intent intent = new Intent(this, MusicService.class);
        if (mService == null) {
            startService(intent);
        }
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    private void initViews() {
        tv_name_song = findViewById(R.id.tv_name_song_playing);
        img_view_next = findViewById(R.id.img_view_next);
        img_view_pause = findViewById(R.id.img_view_pause);
        img_view_prev = findViewById(R.id.img_view_prev);
        img_view_play = findViewById(R.id.img_view_play);
        tv_start_time = findViewById(R.id.tv_start_time);
        img_view_pause.setOnClickListener(this);
        img_view_next.setOnClickListener(this);
        img_view_prev.setOnClickListener(this);
        img_view_play.setOnClickListener(this);
        mSeekbar = findViewById(R.id.seek_bar);
        tv_total_time = findViewById(R.id.tv_total);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        SongAdapter musicAdapter = new SongAdapter(arrayListSongs, this);
        mRecyclerView.setAdapter(musicAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if (fromUser) {
                    mService.seek(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void addDataSong() {
        arrayListSongs.clear();
        arrayListSongs.add(new Song(R.raw.anh_chang_sao_ma, "Anh chẳng sao mà", "Khang Việt"));
        arrayListSongs.add(new Song(R.raw.em_van_chua_ve, "Em vẫn chưa về", "Trịnh Tuấn Vỹ"));
        arrayListSongs.add(new Song(R.raw.mau_nuoc_mat, "Màu nước mắt", "Nguyền Trần Trung Quân"));
        arrayListSongs.add(new Song(R.raw.vo_cung, "Vô cùng", "Phan Duy Anh"));
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) iBinder;
            mService = binder.getService();
            mService.setSyncSeekbarListerner(MainActivity.this);
            syncSeekbar(mService.getDuration());
//            syncNotification(mService.isPlaying());
            mHandler.sendMessageDelayed(new Message(), MESSAGE_DELAY);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            unbindService(mConnection);
        }
    };


    @Override
    public void onItemClick(View view, int position) {
        mService.create(position);
        mService.start();
        img_view_pause.setVisibility(View.VISIBLE);
        img_view_play.setVisibility(View.INVISIBLE);
    }

    @Override
    public void syncSeekbar(int max) {
        mSeekbar.setMax(max);
        tv_total_time.setText(convertMilisecondToFormatTime(max));
    }

    @Override
    public void syncNotification(boolean isPlaying) {

    }


    private String convertMilisecondToFormatTime(long msec) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(msec) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(msec)),
                TimeUnit.MILLISECONDS.toSeconds(msec) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(msec)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_view_next:
                mService.changeSong(NEXT_SONG);
                img_view_pause.setVisibility(View.VISIBLE);
                img_view_play.setVisibility(View.GONE);
                break;
            case R.id.img_view_prev:
                mService.changeSong(PREVIOUS_SONG);
                img_view_pause.setVisibility(View.VISIBLE);
                img_view_play.setVisibility(View.GONE);
                break;
            case R.id.img_view_play:
                if (mService.getMediaPlayer() == null) {
                    mService.create(0);
                    mService.start();
                } else {
                    mService.start();
                }
                img_view_play.setVisibility(View.GONE);
                img_view_pause.setVisibility(View.VISIBLE);
                break;
            case R.id.img_view_pause:
                mService.pause();
                img_view_play.setVisibility(View.VISIBLE);
                img_view_pause.setVisibility(View.GONE);
                break;
        }
    }
}
