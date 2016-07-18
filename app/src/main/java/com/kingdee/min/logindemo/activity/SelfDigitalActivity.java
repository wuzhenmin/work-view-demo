package com.kingdee.min.logindemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kingdee.min.logindemo.R;
import com.kingdee.min.logindemo.view.CreditPanelView;

public class SelfDigitalActivity extends AppCompatActivity {

    private String SELF_TITLE = "个人情况";
    private CreditPanelView mCreditPanelView;
    private int score = 350;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_digital);
        getSupportActionBar().setTitle(SELF_TITLE);
        mCreditPanelView = (CreditPanelView) findViewById(R.id.credit_panel_view);
        mCreditPanelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score <= 950){
                    score += 49;
                }else {
                    score -= 400;
                }
                mCreditPanelView.toggleIndicatorValue(score);
            }
        });
    }


}
