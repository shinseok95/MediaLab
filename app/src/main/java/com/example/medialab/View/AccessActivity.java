package com.example.medialab.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medialab.Model.Student;
import com.example.medialab.Presenter.AccessContract;
import com.example.medialab.Presenter.AccessPresenter;
import com.example.medialab.R;

public class AccessActivity extends BaseActivity implements AccessContract.View {

    TextView nameText;
    TextView studentIdText;
    TextView departmentText;
    EditText computerNumEdit;
    EditText purposeEdit;

    Student student;
    AccessPresenter accessPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_activityy);

        accessPresenter = new AccessPresenter(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        accessPresenter.releaseView();
        accessPresenter = null;
    }

    public void init(){

        setActionBar("QR 입장");

        nameText = (TextView)findViewById(R.id.accessNameID);
        studentIdText = (TextView)findViewById(R.id.accessStudentID);
        departmentText = (TextView)findViewById(R.id.accessDepartmentID);
        computerNumEdit = (EditText) findViewById(R.id.accessComputerID);
        purposeEdit = (EditText) findViewById(R.id.accessPurposeID);

        Intent intent = getIntent();
        student = (Student)intent.getSerializableExtra("VISITOR");

        nameText.setText(student.getName());
        studentIdText.setText(String.valueOf(student.getStudentId()));
        departmentText.setText(student.getDepartment());

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

                student.setComputerNumber(computerNumEdit.getText().toString());
                student.setPurpose(purposeEdit.getText().toString());
                accessPresenter.accessRequest(student);
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

}
