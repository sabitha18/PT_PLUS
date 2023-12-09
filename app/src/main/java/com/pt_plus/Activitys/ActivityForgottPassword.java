package com.pt_plus.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pt_plus.Fragment.ForgottPassword.FragmentForgotPasswordEmail;
import com.pt_plus.Fragment.FragmentHome;
import com.pt_plus.R;

public class ActivityForgottPassword extends SuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgott_password);

        initView();
    }

    private void initView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.lnr_content, new FragmentForgotPasswordEmail())
                .commit();
    }
}