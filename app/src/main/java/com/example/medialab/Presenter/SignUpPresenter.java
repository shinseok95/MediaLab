package com.example.medialab.Presenter;

import android.content.ContentValues;
import android.util.Log;

import com.example.medialab.Model.DBManager;
import com.example.medialab.Model.Student;

public class SignUpPresenter extends BasePresenter implements SignUpContract.Presenter{

    SignUpContract.View view;

    public SignUpPresenter(){
        super();
    }

    public SignUpPresenter(SignUpContract.View view){
        this.view = view;
        dbManager = DBManager.getInstance(view.getInstanceContext());
    }

    /*-----------------------Request 관련 메소드----------------------------*/

    @Override
    public void signUpRequest(Student student) {

        boolean isDateUpdate = dbManager.isDateUpdate(student.getAccessDay());

        if(!isDateUpdate)
            dbManager.updateVisitorTable(student.getAccessDay());

       long isSuccess;


            ContentValues addRowValue = new ContentValues();
            addRowValue.put("studentId", student.getStudentId());
            addRowValue.put("name", student.getName());
            addRowValue.put("department", student.getDepartment());
            addRowValue.put("warning", 0);

            isSuccess = dbManager.registerMember(addRowValue);

            if(isSuccess >0) {
                view.showToast(student + " : 등록완료");
                view.moveToMainActivity(RESULT_OK);
            }
            else {
                view.showToast("등록에 실패하셨습니다.");
                view.moveToMainActivity(RESULT_CANCELED);
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
