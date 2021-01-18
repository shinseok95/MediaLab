package com.example.medialab.Presenter;

import android.content.ContentValues;
import android.util.Log;

import com.example.medialab.Model.DBManageServiceImpl;
import com.example.medialab.Model.StudentVO;

public class SignUpPresenter extends BasePresenter implements SignUpContract.Presenter{

    private SignUpContract.View view;

    private SignUpPresenter(){
        super();
    }

    public SignUpPresenter(SignUpContract.View view){
        this.view = view;
        dBManager = new DBManageServiceImpl(view.getInstanceContext());
    }

    /*-----------------------Request 관련 메소드----------------------------*/

    // 학생 정보 등록 메소드
    @Override
    public void signUpRequest(StudentVO studentVO) {

        boolean isMemberTableExist = dBManager.isMemberTableExist();

        if(!isMemberTableExist){
            dBManager.updateMemberTable();
        }

        ContentValues addRowValue = new ContentValues();
        addRowValue.put("studentId", studentVO.getStudentId());
        addRowValue.put("name", studentVO.getName());
        addRowValue.put("department", studentVO.getDepartment());
        addRowValue.put("warning", 0);
        addRowValue.put("warningReason","");

        long isSuccess = dBManager.registerMember(addRowValue);

        if(isSuccess >0) {
            Log.v("Sign up Presenter","정보 등록 성공");
            view.showToast(studentVO + " : 등록완료");
            view.moveToCalledActivity(RESULT_OK);
        }
        else {
            Log.v("Sign up Presenter","정보 등록 실패");
            view.showToast("등록에 실패하셨습니다.");
            view.moveToCalledActivity(RESULT_CANCELED);
        }
    }

    /*--------------------------View 관련 메소드--------------------------*/

    @Override
    public void setView(SignUpContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }
}
