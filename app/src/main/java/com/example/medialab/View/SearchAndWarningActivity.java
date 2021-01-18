package com.example.medialab.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.medialab.Model.StudentVO;
import com.example.medialab.Presenter.SearchAndWarningContract;
import com.example.medialab.Presenter.SearchAndWarningPresenter;
import com.example.medialab.Presenter.SearchPresenter;
import com.example.medialab.R;

/** 관리자가 학생의 정보를 조회, 경고 할 수 있는 Activity입니다.
 *
 */

public class SearchAndWarningActivity extends BaseActivity implements SearchAndWarningContract.View {

    private TextView studentText;
    private TextView studentSubText;
    private EditText studentIdEdit;
    private TextView studentIdText;
    private TextView nameText;
    private TextView departmentText;
    private EditText warningEdit;
    private TextView warningText;
    private Button searchAndWarningBtn;
    private LinearLayout nameLayout;
    private LinearLayout departmentLayout;
    private LinearLayout warningLayout;

    private StudentVO studentVO;
    private SearchAndWarningPresenter searchAndWarningPresenter;

    boolean warningStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_warning);

        searchAndWarningPresenter = new SearchAndWarningPresenter(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchAndWarningPresenter.releaseView();
        searchAndWarningPresenter=null;
        studentVO=null;
    }

    public void init(){

        setActionBar("정보 조회");

        studentText = (TextView)findViewById(R.id.studentTextID);
        studentSubText = (TextView)findViewById(R.id.studentSubTextID);
        studentIdEdit = (EditText)findViewById(R.id.warningStudentEditID);
        studentIdText = (TextView)findViewById(R.id.warningNameTextID);
        nameText = (TextView)findViewById(R.id.warningNameID);
        departmentText = (TextView)findViewById(R.id.warningDepartmentID);
        warningEdit = (EditText)findViewById(R.id.warningEditID);
        warningText = (TextView)findViewById(R.id.warningTextID);
        searchAndWarningBtn = (Button)findViewById(R.id.warningBtnID);

        nameLayout=(LinearLayout)findViewById(R.id.nameLayoutID);
        departmentLayout=(LinearLayout)findViewById(R.id.departmentLayoutID);
        warningLayout = (LinearLayout)findViewById(R.id.warningLayoutID);

        studentIdEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if((keyEvent.getAction()==KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER){
                    searchAndWarningBtn.performClick();
                    return true;
                }
                else
                    return false;
            }
        });

        studentIdEdit.requestFocus();
    }

    // 학생의 경고 상태에 따라서, 다른 view를 보여주는 메소드 ( true : 학번을 입력한 상태 / false : 학번을 입력하기 전 상태)
    @Override
    public boolean setWarningStatus(boolean status) {

        if(status){

            warningStatus = false;

            searchAndWarningBtn.setText("정보조회");

            studentIdText.setText("");
            studentIdEdit.setText("");
            nameText.setText("");
            departmentText.setText("");
            warningEdit.setText("");

            studentText.setVisibility(View.VISIBLE);
            studentSubText.setVisibility(View.GONE);

            studentIdEdit.setVisibility(View.VISIBLE);
            studentIdText.setVisibility(View.GONE);

            warningEdit.setVisibility(View.GONE);
            warningText.setVisibility(View.GONE);
            warningText.setSelected(false);

            nameLayout.setVisibility(View.GONE);
            departmentLayout.setVisibility(View.GONE);
            warningLayout.setVisibility(View.GONE);

            studentIdEdit.requestFocus();
            return false;
        }
        else{

            if(searchAndWarningPresenter.isWarningMember(studentVO)){
                searchAndWarningBtn.setText("경고해제");
                warningText.setVisibility(View.VISIBLE);
                warningText.setSelected(true);

            }else{
                searchAndWarningBtn.setText("경고등록");
                warningEdit.setVisibility(View.VISIBLE);
                warningEdit.requestFocus();
            }


            studentIdText.setText(studentIdEdit.getText().toString());


            studentText.setVisibility(View.GONE);
            studentSubText.setVisibility(View.VISIBLE);
            studentIdEdit.setVisibility(View.GONE);
            studentIdText.setVisibility(View.VISIBLE);


            nameText.setText(studentVO.getName());
            departmentText.setText(studentVO.getDepartment());

            if(studentVO.getWarning()) {
                warningText.setText(studentVO.getWarningReason());
                warningText.setVisibility(View.VISIBLE);
                warningText.setSelected(true);
            }

            nameLayout.setVisibility(View.VISIBLE);
            departmentLayout.setVisibility(View.VISIBLE);
            warningLayout.setVisibility(View.VISIBLE);

            return true;
        }

    }

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.warningBtnID:

                if(warningStatus) {

                    if(!studentVO.getWarning() && !isWarningFilled()){
                        warningEdit.requestFocus();
                        showToast("경고 이유를 입력하세요.");
                        return;
                    }

                    if(studentVO.getWarning())
                        studentVO.setWarningReason("");
                    else
                        studentVO.setWarningReason(warningEdit.getText().toString());

                    if(searchAndWarningPresenter.setWarningRequest(studentVO))
                        warningStatus = setWarningStatus(true);

                }else{

                    if(!isStudentIDFilled()){
                        studentIdEdit.requestFocus();
                        showToast("조회하실 학번을 입력하세요.");
                        return;
                    }

                    studentVO = searchAndWarningPresenter.memberSearchRequest(studentIdEdit.getText().toString());

                    if(studentVO==null){
                        studentIdEdit.requestFocus();
                        showToast("일치하는 정보가 존재하지 않습니다.");
                        return;
                    }
                    warningStatus = setWarningStatus(false);
                }
        }
    }

    /*-------------------------유효성 체크 메소드-------------------------*/

    @Override
    public boolean isStudentIDFilled(){

        String studentID = studentIdEdit.getText().toString();
        studentID = studentID.trim();

        if (studentID.getBytes().length <= 0)
            return false;
        else
            return true;
    }

    @Override
    public boolean isWarningFilled() {

        String warning = warningEdit.getText().toString();
        warning = warning.trim();

        if (warning.getBytes().length <= 0)
            return false;
        else
            return true;
    }

}
