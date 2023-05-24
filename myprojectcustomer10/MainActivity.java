package com.dilanka456.myprojectcustomer10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static int splash_screen = 4000;
    Animation topAnim, bottumAnim;
    ImageView topImage, bottumImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.loading_top);
        bottumAnim = AnimationUtils.loadAnimation(this,R.anim.loading_bottum);

        topImage = findViewById(R.id.image_top);
        bottumImage = findViewById(R.id.image_bottum);

        topImage.setAnimation(topAnim);
        bottumImage.setAnimation(bottumAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login_activity.class);
                startActivity(intent);
                MainActivity.this.finish();

            }
        },splash_screen);

    }
}