package com.example.medialab.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.medialab.Model.StudentVO;
import com.example.medialab.Presenter.ManagerModeContract;
import com.example.medialab.Presenter.ManagerModePresenter;
import com.example.medialab.R;

public class ManagerModeActivity extends BaseActivity implements ManagerModeContract.View {

    protected final int EXCEL_DOWNLOAD_ACTIVITY_REQUEST_CODE = 1006;
    protected final int MEMBER_SEARCH_ACTIVITY_REQUEST_CODE = 1007;
    protected final int VISITOR_SEARCH_ACTIVITY_REQUEST_CODE = 1008;
    protected final int OTP_CHANGE_ACTIVITY_REQUEST_CODE = 1009;

    ManagerModePresenter managerModePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_mode);

        managerModePresenter = new ManagerModePresenter(this);
        init();
    }

    public void onClick(View view) {

        switch (view.getId()){

            case R.id.managerModeExcelID:
                managerModePresenter.moveToExcelDownloadActivity();
                break;

            case R.id.managerModeInfoSearchID:
                managerModePresenter.moveToMemberSearchActivity();
                break;

            case R.id.managerModeVisitorSearchID:
                break;

            case R.id.managerModePasswordSettingID:
                managerModePresenter.moveToOtpChangeActivity();
                break;
        }
    }

    public void init(){

        setActionBar("관리자 모드");
    }

    @Override
    public void moveToAnotherActivity(int requestCode) {

        Intent intent;
        switch (requestCode) {

            case EXCEL_DOWNLOAD_ACTIVITY_REQUEST_CODE:
                intent = new Intent(this, ExcelDownloadActivity.class);
                startActivityForResult(intent, requestCode);
                break;

            case MEMBER_SEARCH_ACTIVITY_REQUEST_CODE:
                intent = new Intent(this, SearchAndWarningActivity.class);
                startActivityForResult(intent, requestCode);
                break;

            case VISITOR_SEARCH_ACTIVITY_REQUEST_CODE:
                intent = new Intent(this, SearchActivity.class);
                startActivityForResult(intent, requestCode);
                break;

            case OTP_CHANGE_ACTIVITY_REQUEST_CODE:
                intent = new Intent(this, OtpChangeActivity.class);
                startActivityForResult(intent, requestCode);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case EXCEL_DOWNLOAD_ACTIVITY_REQUEST_CODE:
                break;

            case MEMBER_SEARCH_ACTIVITY_REQUEST_CODE:
                break;

            case VISITOR_SEARCH_ACTIVITY_REQUEST_CODE:
                break;

            case OTP_CHANGE_ACTIVITY_REQUEST_CODE:
                break;
        }
    }
}
