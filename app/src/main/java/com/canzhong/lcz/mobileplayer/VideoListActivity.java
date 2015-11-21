package com.canzhong.lcz.mobileplayer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.canzhong.lcz.mobileplayer.domian.VideoItem;
import com.canzhong.lcz.mobileplayer.util.Utils;

import java.util.ArrayList;

/**
 * Created by LCZ on 2015-11-19.
 */
public class VideoListActivity extends Activity {
    private ListView mVideo_List;
    private TextView mNo_Video;
    private ArrayList<VideoItem> mVideoList;
    private Utils util;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mVideoList!=null&&mVideoList.size()>0){
                mNo_Video.setVisibility(View.GONE);
                mVideo_List.setAdapter(new VideoListAdapter());
            }else{
                mNo_Video.setVisibility(View.VISIBLE);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list_layout);
        initView();
        getData();
        setListener();
    }
    /**
     * 初始化
     */
    private void initView(){
        util = new Utils();
        mVideo_List = (ListView) findViewById(R.id.video_list);
        mNo_Video = (TextView) findViewById(R.id.there_no_video);
    }
    /**
     * 设置监听
     */

    private void setListener(){
        mVideo_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoItem videoItem = mVideoList.get(position);
                //把手机里面所有的播放器调出来
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(videoItem.getData()),"video/*");
//                startActivity(intent);

                //调用自己的
                Intent intent = new Intent(VideoListActivity.this,VideoPlayActivity.class);
                intent.setData(Uri.parse(videoItem.getData()));
                startActivity(intent);
            }
        });
    }

    /**
     * 获取数据
     */
    class VideoListAdapter extends BaseAdapter{

        public VideoListAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return mVideoList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            VideoItem video = mVideoList.get(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                view =  View.inflate(VideoListActivity.this,R.layout.video_list_item,null);;
                viewHolder = new ViewHolder();
                viewHolder.video_item_name = (TextView) view.findViewById(R.id.video_item_name);
                viewHolder.video_item_duration = (TextView) view.findViewById(R.id.video_item_duration);
                viewHolder.video_item_size = (TextView) view.findViewById(R.id.video_item_size);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.video_item_name.setText(video.getName());
            viewHolder.video_item_duration.setText(util.stringForTime(((int) video.getDuration())));
            viewHolder. video_item_size.setText(Formatter.formatFileSize(VideoListActivity.this, video.getSize()));  //转换为对应的大小
            return view;
        }
        class ViewHolder {
            TextView video_item_name;
            TextView video_item_duration;
            TextView video_item_size;
        }
    }

    private void getData(){
        new Thread(){
            public void run(){
                //读取手机的视频
                mVideoList = new ArrayList<VideoItem>();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String [] projection = {
                        MediaStore.Video.Media.DISPLAY_NAME,  //视频名称
                        MediaStore.Video.Media.DURATION,    //视频时长
                        MediaStore.Video.Media.SIZE,      //视频大小
                        MediaStore.Video.Media.DATA      //视频在SD卡下的绝对路径
                };
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                while (cursor.moveToNext()){
                    String name = cursor.getString(0);
                    long duration = cursor.getLong(1);
                    long size = cursor.getLong(2);
                    String data = cursor.getString(3);
                    VideoItem videoItem = new VideoItem(name,duration,size,data);
                    mVideoList.add(videoItem);
                }
                cursor.close();
                handler.sendEmptyMessage(1);
            }
        }.start();
    }
}
