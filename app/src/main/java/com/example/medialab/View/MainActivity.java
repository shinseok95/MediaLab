package com.example.medialab.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.medialab.Model.Student;
import com.example.medialab.Presenter.MainContract;
import com.example.medialab.Presenter.MainPresenter;
import com.example.medialab.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends BaseActivity implements MainContract.View {

    private final int ACCESS_QR_SCAN_REQUEST_CODE = 1;
    private final int SIGN_UP_QR_SCAN_REQUEST_CODE = 2;
    private final int SEARCH_QR_SCAN_REQUEST_CODE = 3;
    private final int MANAGER_MODE_QR_SCAN_REQUEST_CODE = 4;

    private long mExitModeTime = 0L;

    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try{
            Thread.sleep(1000);
            setTheme(R.style.AppTheme);
        }catch (Exception e){}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenter(this);
        setActionBar("");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mainPresenter.dbClose();
        mainPresenter.releaseView();
        mainPresenter = null;
    }

    @Override
    public void moveToAnotherActivity(Student student, int requestCode) {

        Intent intent;
        switch (requestCode){

            case ACCESS_ACTIVITY_REQUEST_CODE:
                intent = new Intent(this, AccessActivity.class);
                intent.putExtra("VISITOR", student);
                startActivityForResult(intent,requestCode);
                break;

            case SIGN_UP_ACTIVITY_REQUEST_CODE:
                intent = new Intent(this, SignUpActivity.class);
                intent.putExtra("VISITOR", student);
                startActivityForResult(intent,requestCode);
                break;

            case SEARCH_ACTIVITY_REQUEST_CODE:
                break;

            case MANAGER_ACTIVITY_REQUEST_CODE:
                break;
            case DEVELOPER_INFO_ACTIVITY_REQUEST_CODE:
                intent = new Intent(this, DeveloperInfoActivity.class);
                startActivityForResult(intent,requestCode);
                break;



        }
    }

    /*----------------------QR 관련 메소드------------------------*/

    public void qrScan(final int requestCode) {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setPrompt("모바일 학생증을 스캔하세요");
        intentIntegrator.setRequestCode(requestCode);
        intentIntegrator.initiateScan();
    }

    public void onClick(View view) {

        switch (view.getId()){

            case R.id.entranceID:
                qrScan(ACCESS_QR_SCAN_REQUEST_CODE);
                break;

            case R.id.signUpID:
                qrScan(SIGN_UP_QR_SCAN_REQUEST_CODE);
                break;

            case R.id.infoSearchID:
                qrScan(SEARCH_QR_SCAN_REQUEST_CODE);
                break;
        }
    }

    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {

        final IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        if(result != null) {

            if(result.getContents() == null) {
                super.onActivityResult(requestCode, resultCode, data);
            }

            else{

                new Thread(){
                    @Override
                    public void run() {

                        String scanData = result.getContents();

                        switch (requestCode) {

                            case ACCESS_QR_SCAN_REQUEST_CODE:
                                mainPresenter.moveToAccessActivity(scanData);
                                break;

                            case SIGN_UP_QR_SCAN_REQUEST_CODE:
                                mainPresenter.moveToSignUpActivity(scanData);
                                break;

                            case SEARCH_QR_SCAN_REQUEST_CODE:
                                mainPresenter.moveToSearchActivity(scanData);
                                break;

                            case MANAGER_MODE_QR_SCAN_REQUEST_CODE:
                                mainPresenter.moveToManagerModeActivity(scanData);
                                break;
                        }
                    }
                }.start();
            }
        }
    }

    /*----------------------액션바 메소드------------------------*/

    @Override
    public void setActionBar(CharSequence title){

        androidx.appcompat.widget.Toolbar toolbar =(androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.custom_menubar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.managerMode:
                qrScan(MANAGER_MODE_QR_SCAN_REQUEST_CODE);
                break;

            case R.id.developerMode:
                mainPresenter.moveToDeveloperInfoActivity();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    /*----------------------기타 메소드------------------------*/

    public void onBackPressed()
    {
        if( mExitModeTime != 0 &&
                SystemClock.uptimeMillis() - mExitModeTime < 3000 )
        {
            finish();
        }
        else
        {
            Toast.makeText( this,
                    "이전 키를 한번 더 누르면 종료됩니다.",
                    Toast.LENGTH_LONG ).show();

            mExitModeTime = SystemClock.uptimeMillis();
        }
    }

}