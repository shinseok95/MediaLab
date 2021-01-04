package com.example.medialab.Presenter;

import android.content.ContentValues;

import com.example.medialab.Model.DBManager;
import com.example.medialab.Model.Student;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AccessPresenter extends BasePresenter implements AccessContract.Presenter{

    AccessContract.View view;

    public AccessPresenter(){
        super();
    }

    public AccessPresenter(AccessContract.View view){
        this.view=view;
        dbManager = DBManager.getInstance(view.getInstanceContext());
    }

    /*-----------------------Request 관련 메소드----------------------------*/

    @Override
    public void accessRequest(Student student) {

        boolean isDateUpdate = dbManager.isDateUpdate(student.getAccessDay());

        if(!isDateUpdate)
            dbManager.updateVisitorTable(student.getAccessDay());

        long isSuccess;
        calendar = Calendar.getInstance();
        String entranceTime = dateFormat.format(calendar.getTime());

        student.setEntranceTime(entranceTime);

        ContentValues addRowValue = new ContentValues();
        addRowValue.put("name",student.getName());
        addRowValue.put("studentId",student.getStudentId());
        addRowValue.put("department",student.getDepartment());
        addRowValue.put("purpose",student.getPurpose());
        addRowValue.put("computerNumber",student.getComputerNumber());
        addRowValue.put("entranceTime",student.getEntranceTime());

        isSuccess = dbManager.registerVisitor(addRowValue);

        if(isSuccess != -1) {
            view.showToast("("+student.getEntranceTime()+") " + student + " 입장");
            view.moveToMainActivity(RESULT_OK);
        }
        else {
            view.showToast("입장에 실패하였습니다.");
            view.moveToMainActivity(RESULT_CANCELED);
        }
    }

    /*--------------------------View 관련 메소드--------------------------*/

    @Override
    public void setView(AccessContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }
}
