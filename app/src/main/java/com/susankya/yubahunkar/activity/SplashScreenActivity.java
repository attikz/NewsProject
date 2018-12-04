package com.susankya.yubahunkar.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.susankya.yubahunkar.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1500);
        animation.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                }
        );

        findViewById(R.id.rootView).startAnimation(animation);
    }
}
