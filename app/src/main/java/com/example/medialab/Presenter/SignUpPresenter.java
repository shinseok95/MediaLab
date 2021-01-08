package com.example.medialab.Presenter;

import android.content.ContentValues;

import com.example.medialab.Model.DBManageServiceImpl;
import com.example.medialab.Model.StudentVO;

public class SignUpPresenter extends BasePresenter implements SignUpContract.Presenter{

    SignUpContract.View view;

    private SignUpPresenter(){
        super();
    }

    public SignUpPresenter(SignUpContract.View view){
        this.view = view;
        dBManager = new DBManageServiceImpl(view.getInstanceContext());
    }

    /*-----------------------Request 관련 메소드----------------------------*/

    @Override
    public void signUpRequest(StudentVO studentVO) {

        boolean isDateUpdate = dBManager.isDateUpdate(studentVO.getAccessDay());

        if(!isDateUpdate)
            dBManager.updateVisitorTable(studentVO.getAccessDay());

        ContentValues addRowValue = new ContentValues();
        addRowValue.put("studentId", studentVO.getStudentId());
        addRowValue.put("name", studentVO.getName());
        addRowValue.put("department", studentVO.getDepartment());
        addRowValue.put("warning", 0);
        addRowValue.put("manager",0);

        long isSuccess = dBManager.registerMember(addRowValue);

        if(isSuccess >0) {
            view.showToast(studentVO + " : 등록완료");
            view.moveToCalledActivity(RESULT_OK);
        }
        else {
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
