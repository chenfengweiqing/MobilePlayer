package com.canzhong.lcz.mobileplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by LCZ on 2015-11-20.
 */
public class VideoPlayActivity extends Activity {
    private VideoView mVideoView;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_layout);
        mVideoView = (VideoView)findViewById(R.id.video_view);
        uri = getIntent().getData();
        mVideoView.setVideoURI(uri);
        //设置播放监听
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //开始播放
                mVideoView.start();
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //播放完成
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.video_play_finish), Toast.LENGTH_LONG).show();
                finish();
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.video_play_error), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        //设置视频控制器
        mVideoView.setMediaController(new MediaController(this));
    }
}
