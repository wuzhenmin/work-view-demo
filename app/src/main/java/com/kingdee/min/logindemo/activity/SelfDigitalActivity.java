package com.kingdee.min.logindemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kingdee.min.logindemo.R;

public class SelfDigitalActivity extends AppCompatActivity {

    private String SELF_TITLE = "个人情况";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_digital);
        getSupportActionBar().setTitle(SELF_TITLE);
    }
}
