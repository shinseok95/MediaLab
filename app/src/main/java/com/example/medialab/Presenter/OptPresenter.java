package com.example.medialab.Presenter;

import android.content.SharedPreferences;
import android.util.Log;

public class OptPresenter extends BasePresenter implements OptContract.Presenter {

    private final int MAX_ATTEMPT = 3;
    private OptContract.View view;

    private String password =null;
    private SharedPreferences mPref = null;

    private OptPresenter(){
        super();
    }

    public OptPresenter(OptContract.View view, SharedPreferences mPref){
        this.view=view;
        this.mPref = mPref;
    }

    // 관리자 모드 전환 메소드
    @Override
    public void validateRequest(String otp, int count) {

        if(count==MAX_ATTEMPT)
            view.moveToCalledActivity(RESULT_CANCELED);

        String originalPassword = mPref.getString("PASSWORD","0000");
        if(originalPassword.equals(otp)) {
            Log.v("Opt Presenter","관리자 모드 전환");
            view.showToast("관리자 모드로 전환되었습니다.");
            view.moveToCalledActivity(RESULT_OK);
        }
    }

    // 관지자 코드 변경 메소드
    @Override
    public boolean validateChangeRequest(String otp, int count) {

        if(count ==0){

            String originalPassword = mPref.getString("PASSWORD","0000");

            if(originalPassword.equals(otp)) {

                view.showToast("동일한 Code입니다.");
                view.moveToCalledActivity(RESULT_OK);
                return false;
            }else
                password = otp;
            return true;
        }
        else if(count ==1){

            if(password.equals(otp)){

                try {
                    SharedPreferences.Editor prefEditor = mPref.edit();
                    prefEditor.putString("PASSWORD", password);
                    prefEditor.apply();

                    Log.v("Opt Presenter","코드 변환 성공");
                    view.showToast("Code가 변경되었습니다.");
                    view.moveToCalledActivity(RESULT_OK);
                }catch (Exception e){
                    Log.e("Opt Presenter","관리자 코드 변경 에러 : "+e.getMessage());
                }
                return true;
            }
            password=null;
            return false;
        }
        else
            return false;
    }


    @Override
    public void setView(OptContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
        mPref = null;
    }
}
