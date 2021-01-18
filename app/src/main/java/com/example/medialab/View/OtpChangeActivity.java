package com.example.medialab.View;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.medialab.Presenter.OptContract;
import com.example.medialab.Presenter.OptPresenter;
import com.example.medialab.R;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

/** Validation code를 변경 할 수 있는 Activity입니다.
 */

public class OtpChangeActivity extends BaseActivity implements OptContract.View{

    TextView otpText;

    private OtpView otpView;
    private OptPresenter optPresenter = null;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_change);

        optPresenter = new OptPresenter(this,getSharedPreferences("Manager", Context.MODE_PRIVATE));
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

        otpText = (TextView)findViewById(R.id.OtpTextId);

        count =0;
        otpView = findViewById(R.id.otp_view);
        otpView.setCursorVisible(false);
        otpView.requestFocus();

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override public void onOtpCompleted(String otp) {

                if(count == 0){
                    if(optPresenter.validateChangeRequest(otp,count)){
                        otpText.setText("한번 더 입력해주세요.");
                        count++;
                    }
                }else{
                    if(!optPresenter.validateChangeRequest(otp,count)){
                        otpText.setText("Code가 일치하지 않습니다.");
                        count=0;
                    }
                }
                otpView.setText(null);
            }
        });
    }
}
