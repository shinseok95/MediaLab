package com.example.medialab.Presenter;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.medialab.Model.DBManageServiceImpl;
import com.example.medialab.Model.StudentVO;

import java.util.ArrayList;

public class CurrentUserPresenter extends BasePresenter implements CurrentUserContract.Presenter{

    private CurrentUserContract.View view =null;

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
        boolean isTableExist = dBManager.isVisitorTableExist(todayDate);

        if(!isDateUpdate || !isTableExist)
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

        visitorCursor.close();

        return studentList;
    }

    @Override
    public boolean setWarningRequest(StudentVO studentVO){

        boolean isDateUpdate = dBManager.isDateUpdate(studentVO.getAccessDay());
        boolean isVisitorTableExist = dBManager.isVisitorTableExist(studentVO.getAccessDay());
        boolean isMemberTableExist = dBManager.isMemberTableExist();

        if(!isDateUpdate || !isVisitorTableExist)
            dBManager.updateVisitorTable(studentVO.getAccessDay());

        if(!isMemberTableExist){
            dBManager.updateMemberTable();
        }

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

            if(studentVO.getWarning()) {
                Log.v("Current User Presenter",studentVO.toString()+"경고 해제");
                view.showToast(studentVO + " : 경고해제");
            }
            else {
                Log.v("Current User Presenter",studentVO.toString()+"경고 등록");
                view.showToast(studentVO + " : 경고등록");
            }
            return true;
        }
        else {
            Log.v("Current User Presenter",studentVO.toString()+"경고 실패");
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
