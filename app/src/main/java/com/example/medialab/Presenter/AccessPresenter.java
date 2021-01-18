package com.example.medialab.Presenter;

import android.content.ContentValues;
import android.util.Log;

import com.example.medialab.Model.DBManageServiceImpl;
import com.example.medialab.Model.StudentVO;

public class AccessPresenter extends BasePresenter implements AccessContract.Presenter{

    private AccessContract.View view;

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
        boolean isTableExist = dBManager.isVisitorTableExist(studentVO.getAccessDay());

        if(!isDateUpdate || !isTableExist)
            dBManager.updateVisitorTable(studentVO.getAccessDay());


        ContentValues addRowValue = new ContentValues();
        addRowValue.put("name", studentVO.getName());
        addRowValue.put("studentId", studentVO.getStudentId());
        addRowValue.put("department", studentVO.getDepartment());
        addRowValue.put("purpose", studentVO.getPurpose());
        addRowValue.put("computerNumber", studentVO.getComputerNumber());
        addRowValue.put("entranceTime", studentVO.getEntranceTime());

        long isSuccess = dBManager.registerVisitor(addRowValue);

        if(isSuccess != -1) {
            Log.v("Access presenter",studentVO.toString()+"입장 성공");
            view.showToast(studentVO + " 입장("+ studentVO.getEntranceTime()+")");
            view.moveToCalledActivity(RESULT_OK);
        }
        else {
            Log.v("Access presenter",studentVO.toString()+"입장 실패");
            view.showToast("입장에 실패하였습니다.");
            view.moveToCalledActivity(RESULT_CANCELED);
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
