package com.canzhong.lcz.mobileplayer;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

public class MainActivity extends AppCompatActivity {
    private boolean isStartVideoList = false;   //判断是否启动列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startVideo();
            }
        }, 2000);
    }
    private void startVideo(){
        if(!isStartVideoList){
            isStartVideoList = true;
            Intent intent = new Intent(this,VideoListActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startVideo();
        return super.onTouchEvent(event);
    }
}
