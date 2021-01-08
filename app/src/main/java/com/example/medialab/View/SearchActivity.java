package com.example.medialab.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medialab.Model.StudentVO;
import com.example.medialab.Presenter.SearchContract;
import com.example.medialab.Presenter.SearchPresenter;
import com.example.medialab.R;

public class SearchActivity extends BaseActivity implements SearchContract.View {

    TextView nameEdit;
    TextView studentIdText;
    EditText departmentEdit;
    Button modifyBtn;

    StudentVO studentVO;
    SearchPresenter searchPresenter;

    boolean modifyStatus = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchPresenter = new SearchPresenter(this);
        init();
    }

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.modifyBtnID:

                if(modifyStatus) {

                    if(!isNameFilled()){
                        nameEdit.requestFocus();
                        showToast("수정하실 이름을 입력하세요.");
                        return;
                    }

                    if(!isDepartmentFilled()){
                        departmentEdit.requestFocus();
                        showToast("수정하실 학과를 입력하세요.");
                        return;
                    }

                    String inputName = nameEdit.getText().toString().trim();
                    String inputDepartment = departmentEdit.getText().toString().trim();

                    if(!inputName.equals(studentVO.getName()) || !inputDepartment.equals(studentVO.getDepartment())){

                        studentVO.setName(inputName);
                        studentVO.setDepartment(inputDepartment);
                        searchPresenter.modifyRequest(studentVO);
                    }
                    else
                        modifyStatus = setModifyStatus(true);
                }else
                    modifyStatus = setModifyStatus(false);

        }
    }

    public void init(){

        setActionBar("정보 조회");

        studentIdText = (TextView) findViewById(R.id.searchStudentID);
        nameEdit = (EditText)findViewById(R.id.searchNameID);
        departmentEdit = (EditText) findViewById(R.id.searchDepartmentID);
        modifyBtn = (Button)findViewById(R.id.modifyBtnID);

        Intent intent = getIntent();
        studentVO = (StudentVO)intent.getSerializableExtra("MEMBER");

        nameEdit.setText(studentVO.getName());
        studentIdText.setText(String.valueOf(studentVO.getStudentId()));
        departmentEdit.setText(studentVO.getDepartment());
    }

    @Override
    public boolean setModifyStatus(boolean status) {

        if(status){

            modifyStatus = false;
            modifyBtn.setText("수정하기");
            nameEdit.setEnabled(false);
            departmentEdit.setEnabled(false);

            return false;
        }
        else{

            modifyStatus = true;
            modifyBtn.setText("수정완료");
            nameEdit.setEnabled(true);
            departmentEdit.setEnabled(true);

            return true;
        }
    }

    @Override
    public boolean isNameFilled() {

        String name = nameEdit.getText().toString();
        name = name.trim();

        if (name.getBytes().length <= 0)
            return false;
        else
            return true;
    }

    @Override
    public boolean isDepartmentFilled() {

        String department = departmentEdit.getText().toString();
        department = department.trim();

        if (department.getBytes().length <= 0)
            return false;
        else
            return true;
    }
}
