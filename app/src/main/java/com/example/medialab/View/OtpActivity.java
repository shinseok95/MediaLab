package com.example.medialab.View;

import android.content.Context;
import android.os.Bundle;

import com.example.medialab.Presenter.OptContract;
import com.example.medialab.Presenter.OptPresenter;
import com.example.medialab.R;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

public class OtpActivity extends BaseActivity implements OptContract.View{

    private OtpView otpView;
    private OptPresenter optPresenter = null;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt);

        optPresenter = new OptPresenter(this,getSharedPreferences("Manager",Context.MODE_PRIVATE));
        init();
    }

    private void init(){

        count =0;
        otpView = findViewById(R.id.otp_view);
        otpView.setCursorVisible(false);
        otpView.requestFocus();

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override public void onOtpCompleted(String otp) {

                optPresenter.validateRequest(otp,count);
                otpView.setText(null);
                count++;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        optPresenter.releaseView();
        optPresenter=null;
    }
}
