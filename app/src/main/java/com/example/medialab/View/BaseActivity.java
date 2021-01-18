package com.example.medialab.View;

import android.content.Context;
import android.graphics.Color;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.medialab.Presenter.BaseContract;
import com.example.medialab.R;
import com.muddzdev.styleabletoast.StyleableToast;

/** 공통된 기능을 모아놓은 Activity입니다.
 *
 */

public class BaseActivity  extends AppCompatActivity implements BaseContract.View  {

    protected final int ACCESS_ACTIVITY_REQUEST_CODE = 1000;
    protected final int SIGN_UP_ACTIVITY_REQUEST_CODE = 1001;
    protected final int SEARCH_ACTIVITY_REQUEST_CODE = 1002;
    protected final int MANAGER_MODE_ACTIVITY_REQUEST_CODE = 1003;
    protected final int DEVELOPER_INFO_ACTIVITY_REQUEST_CODE = 1004;
    protected final int OTP_ACTIVITY_REQUEST_CODE = 1005;

    // 기본 toast(blue)
    @Override
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StyleableToast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG, R.style.importanttoast).show();
                    }
                });
            }
        });
    }

    // 경고 toast(red)
    @Override
    public void showWarningToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StyleableToast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG, R.style.warningtoast).show();
            }
        });
    }

    @Override
    public Context getInstanceContext() {
        return getApplicationContext();
    }

    // 모든 작업을 마치고 호출한 activity로 돌아가는 메소드
    @Override
    public void moveToCalledActivity(int resultCode){

        setResult(resultCode);
        finish();
    }

    /*----------------------액션바 메소드------------------------*/

    public void setActionBar(CharSequence title){

        androidx.appcompat.widget.Toolbar toolbar =(androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.rgb(0,113,185));
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
