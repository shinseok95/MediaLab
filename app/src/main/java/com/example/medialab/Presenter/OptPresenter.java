package com.example.medialab.Presenter;

import android.content.SharedPreferences;

public class OptPresenter extends BasePresenter implements OptContract.Presenter {

    final int MAX_ATTEMPT = 3;
    OptContract.View view;

    private String password =null;
    private SharedPreferences mPref = null;

    private OptPresenter(){
        super();
    }

    public OptPresenter(OptContract.View view, SharedPreferences mPref){
        this.view=view;
        this.mPref = mPref;
    }

    @Override
    public void setView(OptContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }

    @Override
    public void validateRequest(String otp, int count) {

        if(count==MAX_ATTEMPT)
            view.moveToCalledActivity(RESULT_CANCELED);

        String originalPassword = mPref.getString("PASSWORD","0000");

        if(originalPassword.equals(otp)) {
            view.showToast("관리자 모드로 전환되었습니다.");
            view.moveToCalledActivity(RESULT_OK);
        }
    }

    @Override
    public boolean validateChangeRequest(String otp, int count) {

        if(count ==0){

            String originalPassword = mPref.getString("PASSWORD","0000");

            if(originalPassword.equals(otp)) {

                view.showToast("동일한 OTP입니다.");
                view.moveToCalledActivity(RESULT_OK);
            }else
                password = otp;
            return true;
        }
        else if(count ==1){

            if(password.equals(otp)){

                SharedPreferences.Editor prefEditor = mPref.edit();
                prefEditor.putString("PASSWORD",password);
                prefEditor.apply();

                view.showToast("OTP가 변경되었습니다.");
                view.moveToCalledActivity(RESULT_OK);
            }
            password=null;
            return false;
        }
        else
            return false;
    }
}
