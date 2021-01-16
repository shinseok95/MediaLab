package com.example.medialab.Presenter;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.medialab.Model.DBManageServiceImpl;
import com.example.medialab.Model.StudentVO;

import java.util.ArrayList;

public class CurrentUserPresenter extends BasePresenter implements CurrentUserContract.Presenter{

    protected final int MEMBER_SEARCH_ACTIVITY_REQUEST_CODE = 1007;

    CurrentUserContract.View view;

    private CurrentUserPresenter(){
        super();
    }

    public CurrentUserPresenter(CurrentUserContract.View view){
        this.view=view;
        dBManager = new DBManageServiceImpl(view.getInstanceContext());
    }

    /*-----------------------Request 관련 메소드----------------------------*/

    @Override
    public ArrayList<StudentVO> studentListRequest() {

        ArrayList<StudentVO> studentList = new ArrayList<StudentVO>();
        String todayDate = getTodayDate();
        boolean isDateUpdate = dBManager.isDateUpdate(todayDate);

        if(!isDateUpdate)
            dBManager.updateVisitorTable(todayDate);

        Cursor visitorCursor = dBManager.visitorQuery(visitorColumns,"exitTime IS NULL",null,null,null,null);

        while(visitorCursor.moveToNext()){

            StudentVO studentVO = new StudentVO(visitorCursor.getString(1),visitorCursor.getInt(2),todayDate);
            studentVO.setDepartment(visitorCursor.getString(3));
            studentVO.setPurpose(visitorCursor.getString(4));
            studentVO.setComputerNumber(visitorCursor.getString(5));
            studentVO.setEntranceTime(visitorCursor.getString(6));
            studentList.add(studentVO);
        }

        return studentList;
    }

    @Override
    public boolean setWarningRequest(StudentVO studentVO){

        ContentValues warningAddRowValue = new ContentValues();
        ContentValues exitAddRowValue = new ContentValues();

        if(studentVO.getWarning())
            warningAddRowValue.put("warning", 0);

        else
            warningAddRowValue.put("warning", 1);

        warningAddRowValue.put("warningReason",studentVO.getWarningReason());

        long isWarningSuccess = dBManager.memberUpdate(warningAddRowValue,"studentID="+ studentVO.getStudentId(),null);

        exitAddRowValue.put("exitTime","00:00:00");
        long isExitSuccess = dBManager.visitorUpdate(exitAddRowValue,"studentID="+ studentVO.getStudentId()+" AND entranceTime='"+studentVO.getEntranceTime()+"'",null);

        if(isWarningSuccess >0 && isExitSuccess>0) {

            if(studentVO.getWarning())
                view.showToast(studentVO + " : 경고해제");

            else
                view.showToast(studentVO + " : 경고등록");

            return true;
        }
        else {
            view.showToast("경고에 실패하셨습니다.");
            return false;
        }
    }

    /*--------------------------View 관련 메소드--------------------------*/

    @Override
    public void setView(CurrentUserContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }
}
