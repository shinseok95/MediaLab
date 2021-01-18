package com.example.medialab.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.medialab.Model.StudentVO;
import com.example.medialab.Presenter.SearchContract;
import com.example.medialab.Presenter.SearchPresenter;
import com.example.medialab.R;

/** 사용자가 본인의 정보를 조회, 수정, 삭제할 수 있는 Activity입니다.
 *
 */

public class SearchActivity extends BaseActivity implements SearchContract.View {

    private EditText nameEdit;
    private TextView studentIdText;
    private EditText departmentEdit;
    private Button modifyBtn;

    private StudentVO studentVO;
    private SearchPresenter searchPresenter;
    private boolean modifyStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchPresenter = new SearchPresenter(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        searchPresenter.releaseView();
        searchPresenter=null;
        studentVO=null;
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
                break;

                case R.id.deleteBtnID:

                    new AlertDialog.Builder(SearchActivity.this)
                            .setMessage("정보를 삭제하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which){
                                    searchPresenter.deleteRequest(studentIdText.getText().toString());
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which){
                                }
                            })
                            .show();
                    break;

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
                    modifyBtn.performClick();
                    return true;
                }
                else
                    return false;
            }
        });
    }

    // 수정 상태를 변경하는 메소드 (true : 수정 중인 상태 / false : 수정 전 상태)
    @Override
    public boolean setModifyStatus(boolean status) {

        if(status){

            modifyStatus = false;
            modifyBtn.setText("수정");
            nameEdit.setEnabled(false);
            departmentEdit.setEnabled(false);

            return false;
        }
        else{

            modifyStatus = true;
            modifyBtn.setText("완료");
            nameEdit.setEnabled(true);
            departmentEdit.setEnabled(true);

            return true;
        }
    }

    /*-------------------------유효성 체크 메소드-------------------------*/

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
