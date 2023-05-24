package com.dilanka456.myprojectcustomer10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class AboutUs extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        videoView = findViewById(R.id.my_video_player);
        mediaController = new MediaController(this);
        videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.myvideo);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

    }
}