package com.example.medialab.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medialab.Model.Student;
import com.example.medialab.Presenter.SignUpContract;
import com.example.medialab.Presenter.SignUpPresenter;
import com.example.medialab.R;

public class SignUpActivity extends BaseActivity implements SignUpContract.View {

    EditText nameEdit;
    TextView studentIdText;
    EditText departmentEdit;

    Student student;
    SignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpPresenter = new SignUpPresenter(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        signUpPresenter.releaseView();
        signUpPresenter = null;
    }

    public void init(){

        setActionBar("정보등록");

        nameEdit = (EditText)findViewById(R.id.signUpNameID);
        studentIdText = (TextView)findViewById(R.id.signUpStudentID);
        departmentEdit = (EditText)findViewById(R.id.signUpDepartmentID);

        Intent intent = getIntent();
        student = (Student)intent.getSerializableExtra("VISITOR");
        studentIdText.setText(String.valueOf(student.getStudentId()));
    }

    public void onClick(View view) {

        switch (view.getId()){

            case R.id.signUpID:

                if(!isNameFilled()) {
                    nameEdit.requestFocus();
                    showToast("이름을 입력하세요.");
                    return;
                }

                if(!isDepartmentFilled()) {
                    departmentEdit.requestFocus();
                    showToast("학과를 입력하세요.");
                    return;
                }

                student.setName(nameEdit.getText().toString());
                student.setDepartment(departmentEdit.getText().toString());
                signUpPresenter.signUpRequest(student);
                break;

        }
    }

    @Override
    public boolean isNameFilled() {

        String name = nameEdit.getText().toString();
        name = name.trim();

        if(name.getBytes().length <= 0)
            return false;
        else
            return true;
    }

    @Override
    public boolean isDepartmentFilled() {

        String department = departmentEdit.getText().toString();
        department = department.trim();

        if(department.getBytes().length <= 0)
            return false;
        else
            return true;
    }


}