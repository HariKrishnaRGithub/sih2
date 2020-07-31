package com.example.sih2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    TextView tagline,title;
    ImageView ceo,employee,deal;
    ConstraintLayout constraintLayout;
    Boolean click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        click =false;

        constraintLayout=(ConstraintLayout)findViewById(R.id.screen);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click=true;
                Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tagline=(TextView)findViewById(R.id.tagline);
        title=(TextView)findViewById(R.id.futuretext);
        //ceo=(ImageView)findViewById(R.id.ceo);
        //employee=(ImageView)findViewById(R.id.employee);
        deal=(ImageView)findViewById(R.id.deal);
        //ceo.animate().translationX(0).setDuration(1000);
        //.animate().translationX(0).setDuration(1000);
        deal.animate().translationY(0).setDuration(2500);
        title.setAlpha(0);
        title.animate().alpha((float) 0.8).setDuration(3000).setStartDelay(1500);
        tagline.setAlpha(0);
        tagline.animate().alpha((float) 0.5).setDuration(3000).setStartDelay(1500);


/*
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ceo.animate().alpha(0).setDuration(1000);
                    employee.animate().alpha(0).setDuration(1000);
                }
            },2000);
*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    tagline.animate().alpha((float) 0.5).setDuration(3000).setStartDelay(1500);
                    Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },5000);

    }
}