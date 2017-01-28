package com.diegoalvis.android.happywish.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.diegoalvis.android.happywish.R;

public class SplashActivity extends AppCompatActivity {

    private static final int ANIMATION_TIME = 280; // time in milliseconds

    ImageView circleCenter1,circleCenter2,circleCenter3,circleCenter4,circleCenter5,circleCenter6,circleCenter7;
    ImageView imageLogo;
    TextView nameApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // get views
        circleCenter1   = (ImageView) findViewById(R.id.center_circle_view_1);
        circleCenter2   = (ImageView) findViewById(R.id.center_circle_view_2);
        circleCenter3   = (ImageView) findViewById(R.id.center_circle_view_3);
        circleCenter4   = (ImageView) findViewById(R.id.center_circle_view_4);
        circleCenter5   = (ImageView) findViewById(R.id.center_circle_view_5);
        circleCenter6   = (ImageView) findViewById(R.id.center_circle_view_6);
        circleCenter7   = (ImageView) findViewById(R.id.center_circle_view_7);
        imageLogo       = (ImageView) findViewById(R.id.image_logo_app);
        nameApp         = (TextView) findViewById(R.id.name_app);

        // start thread of animations
        AnimationThread animationThread = new AnimationThread();
        animationThread.start();
    }


    public void animCircle(final ImageView imageView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.zoom_in);
                    animation.reset();
                    imageView.startAnimation(animation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void showLogo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageLogo.setVisibility(View.VISIBLE);
                nameApp.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, android.R.anim.fade_in);
                animation.reset();
                imageLogo.startAnimation(animation);
                nameApp.startAnimation(animation);
            }
        });
    }


    private void goToMainActiviy() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private class AnimationThread extends Thread {
        @Override
        public void run() {
            try {
                animCircle(circleCenter1);
                Thread.sleep(ANIMATION_TIME);
                animCircle(circleCenter2);
                Thread.sleep(ANIMATION_TIME);
                animCircle(circleCenter3);
                Thread.sleep(ANIMATION_TIME);
                animCircle(circleCenter4);
                Thread.sleep(ANIMATION_TIME);
                animCircle(circleCenter5);
                Thread.sleep(ANIMATION_TIME);
                animCircle(circleCenter6);
                Thread.sleep(ANIMATION_TIME);
                animCircle(circleCenter7);
                Thread.sleep(ANIMATION_TIME * 3);
                showLogo();
                Thread.sleep(ANIMATION_TIME * 2);
                goToMainActiviy();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

