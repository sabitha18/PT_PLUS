package com.pt_plus.Activitys;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;


import com.pt_plus.R;

public class NavigationActivity extends AppCompatActivity {

    public void openScreenAnimation(Intent intent , Activity currentActivity ,Boolean isFinish) {
        if(intent != null){
            startActivity(intent);
        }
        if(isFinish){
            currentActivity.finish();
        }
        overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
    }
    public void exitScreenAnimation(Intent intent , Activity currentActivity ,Boolean isFinish) {
        if(intent != null){
            startActivity(intent);
        }
        if(isFinish){
            currentActivity.finish();
        }
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
    }
}
