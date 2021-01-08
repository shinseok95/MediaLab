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

public class BaseActivity  extends AppCompatActivity implements BaseContract.View  {

    protected final int ACCESS_ACTIVITY_REQUEST_CODE = 1000;
    protected final int SIGN_UP_ACTIVITY_REQUEST_CODE = 1001;
    protected final int SEARCH_ACTIVITY_REQUEST_CODE = 1002;
    protected final int MANAGER_MODE_ACTIVITY_REQUEST_CODE = 1003;
    protected final int DEVELOPER_INFO_ACTIVITY_REQUEST_CODE = 1004;
    protected final int OTP_ACTIVITY_REQUEST_CODE = 1005;

    @Override
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

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
    public void showImportantToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StyleableToast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG, R.style.importanttoast).show();
            }
        });
    }

    @Override
    public Context getInstanceContext() {
        return getApplicationContext();
    }

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
