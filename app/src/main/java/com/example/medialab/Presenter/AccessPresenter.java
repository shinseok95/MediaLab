package com.example.medialab.Presenter;

import android.content.ContentValues;

import com.example.medialab.Model.DBManageServiceImpl;
import com.example.medialab.Model.StudentDAO;
import com.example.medialab.Model.StudentVO;

import java.util.Calendar;

public class AccessPresenter extends BasePresenter implements AccessContract.Presenter{

    AccessContract.View view;

    private AccessPresenter(){
        super();
    }

    public AccessPresenter(AccessContract.View view){
        this.view=view;
        dBManager = new DBManageServiceImpl(view.getInstanceContext());
    }

    /*-----------------------Request 관련 메소드----------------------------*/

    @Override
    public void accessRequest(StudentVO studentVO) {

        boolean isDateUpdate = dBManager.isDateUpdate(studentVO.getAccessDay());

        if(!isDateUpdate)
            dBManager.updateVisitorTable(studentVO.getAccessDay());

        long isSuccess;
        calendar = Calendar.getInstance();
        String entranceTime = dateFormat.format(calendar.getTime());

        studentVO.setEntranceTime(entranceTime);

        ContentValues addRowValue = new ContentValues();
        addRowValue.put("name", studentVO.getName());
        addRowValue.put("studentId", studentVO.getStudentId());
        addRowValue.put("department", studentVO.getDepartment());
        addRowValue.put("purpose", studentVO.getPurpose());
        addRowValue.put("computerNumber", studentVO.getComputerNumber());
        addRowValue.put("entranceTime", studentVO.getEntranceTime());

        isSuccess = dBManager.registerVisitor(addRowValue);

        if(isSuccess != -1) {
            view.showToast(studentVO + " 입장("+ studentVO.getEntranceTime()+")");
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
