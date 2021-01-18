package com.example.medialab.View;

import android.content.Context;
import android.os.Bundle;

import com.example.medialab.Presenter.OptContract;
import com.example.medialab.Presenter.OptPresenter;
import com.example.medialab.R;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

/** 관리자 모드에 접근하기 위한 validation check Activity입니다.
 */

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        optPresenter.releaseView();
        optPresenter=null;
        otpView=null;
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

}
