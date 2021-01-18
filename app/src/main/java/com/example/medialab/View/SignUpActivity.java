package com.example.medialab.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medialab.Model.StudentVO;
import com.example.medialab.Presenter.SignUpContract;
import com.example.medialab.Presenter.SignUpPresenter;
import com.example.medialab.R;

/** 학생이 본인의 정보를 등록하는 Activity입니다.
 *
 */

public class SignUpActivity extends BaseActivity implements SignUpContract.View {

    private EditText nameEdit;
    private TextView studentIdText;
    private EditText departmentEdit;
    private Button signUpBtn;

    private StudentVO studentVO;
    private SignUpPresenter signUpPresenter;

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
        studentVO=null;
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

                studentVO.setName(nameEdit.getText().toString());
                studentVO.setDepartment(departmentEdit.getText().toString());
                signUpPresenter.signUpRequest(studentVO);
                break;
        }
    }

    public void init(){

        setActionBar("정보등록");

        nameEdit = (EditText)findViewById(R.id.signUpNameID);
        studentIdText = (TextView)findViewById(R.id.signUpStudentID);
        departmentEdit = (EditText)findViewById(R.id.signUpDepartmentID);
        signUpBtn = (Button)findViewById(R.id.signUpID);

        Intent intent = getIntent();
        studentVO = (StudentVO)intent.getSerializableExtra("VISITOR");
        studentIdText.setText(String.valueOf(studentVO.getStudentId()));

        nameEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if((keyEvent.getAction()==KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER){
                    departmentEdit.requestFocus();
                    return true;
                }
                else
                    return false;
            }
        });

        departmentEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if((keyEvent.getAction()==KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER){
                    signUpBtn.performClick();
                    return true;
                }
                else
                    return false;
            }
        });

        nameEdit.requestFocus();
    }

    /*-------------------------유효성 체크 메소드-------------------------*/

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
