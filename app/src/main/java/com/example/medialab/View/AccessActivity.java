package com.example.medialab.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medialab.Model.StudentVO;
import com.example.medialab.Presenter.AccessContract;
import com.example.medialab.Presenter.AccessPresenter;
import com.example.medialab.R;

public class AccessActivity extends BaseActivity implements AccessContract.View {

    TextView nameText;
    TextView studentIdText;
    TextView departmentText;
    EditText computerNumEdit;
    EditText purposeEdit;
    Button accessBtn;

    StudentVO studentVO;
    AccessPresenter accessPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_activityy);

        accessPresenter = new AccessPresenter(this);
        init();
    }

    public void onClick(View view) {

        switch (view.getId()){

            case R.id.accessID:

                if(!isComputerNumberFilled()) {
                    computerNumEdit.requestFocus();
                    showToast("컴퓨터 번호를 입력하세요.");
                    return;
                }

                if(!isPurposeFilled()) {
                    purposeEdit.requestFocus();
                    showToast("이용 목적을 입력하세요.");
                    return;
                }

                studentVO.setComputerNumber(computerNumEdit.getText().toString());
                studentVO.setPurpose(purposeEdit.getText().toString());
                accessPresenter.accessRequest(studentVO);
                break;
        }
    }

    @Override
    public boolean isComputerNumberFilled() {

        String computerNumber = computerNumEdit.getText().toString();
        computerNumber = computerNumber.trim();

        if(computerNumber.getBytes().length <= 0)
            return false;
        else
            return true;
    }

    @Override
    public boolean isPurposeFilled() {

        String purpose = purposeEdit.getText().toString();
        purpose = purpose.trim();

        if (purpose.getBytes().length <= 0)
            return false;
        else
            return true;
    }

    public void init(){

        setActionBar("QR 입장");

        nameText = (TextView)findViewById(R.id.accessNameID);
        studentIdText = (TextView)findViewById(R.id.accessStudentID);
        departmentText = (TextView)findViewById(R.id.accessDepartmentID);
        computerNumEdit = (EditText) findViewById(R.id.accessComputerID);
        purposeEdit = (EditText) findViewById(R.id.accessPurposeID);
        accessBtn = (Button)findViewById(R.id.accessID);

        Intent intent = getIntent();
        studentVO = (StudentVO)intent.getSerializableExtra("VISITOR");

        nameText.setText(studentVO.getName());
        studentIdText.setText(String.valueOf(studentVO.getStudentId()));
        departmentText.setText(studentVO.getDepartment());

        computerNumEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if((keyEvent.getAction()==KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER){
                    purposeEdit.requestFocus();
                    return true;
                }
                else
                    return false;
            }
        });

        purposeEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if((keyEvent.getAction()==KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER){
                    accessBtn.performClick();
                    return true;
                }
                else
                    return false;
            }
        });

        computerNumEdit.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        accessPresenter.releaseView();
        accessPresenter = null;
    }

}
