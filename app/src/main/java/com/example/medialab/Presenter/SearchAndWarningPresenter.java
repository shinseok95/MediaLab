package com.example.medialab.Presenter;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.medialab.Model.DBManageServiceImpl;
import com.example.medialab.Model.StudentVO;

public class SearchAndWarningPresenter extends BasePresenter implements SearchAndWarningContract.Presenter {

    private SearchAndWarningContract.View view;

    private SearchAndWarningPresenter(){
        super();
    }

    public SearchAndWarningPresenter(SearchAndWarningContract.View view){
        this.view = view;
        dBManager = new DBManageServiceImpl(view.getInstanceContext());
    }

    // 학생 정보 조회 메소드
    @Override
    public StudentVO memberSearchRequest(String studentID){

        boolean isMemberTableExist = dBManager.isMemberTableExist();

        if(!isMemberTableExist){
            dBManager.updateMemberTable();
        }

        StudentVO studentVO = new StudentVO();
        Cursor memberCursor = dBManager.memberQuery(memBerColumns,"studentID="+ studentID,null,null,null,null);

        if(memberCursor.moveToFirst()){
            studentVO.setStudentId(memberCursor.getInt(0));
            studentVO.setName(memberCursor.getString(1));
            studentVO.setDepartment(memberCursor.getString(2));

            if(memberCursor.getInt(3)==1)
                studentVO.setWarning(true);
            else
                studentVO.setWarning(false);

            studentVO.setWarningReason(memberCursor.getString(4));

            memberCursor.close();
            return studentVO;
        }
        else
            return null;
    }


    // 경고 등록 및 해제 메소드
    @Override
    public boolean setWarningRequest(StudentVO studentVO){

        boolean isMemberTableExist = dBManager.isMemberTableExist();

        if(!isMemberTableExist){
            dBManager.updateMemberTable();
        }

        ContentValues addRowValue = new ContentValues();

        if(studentVO.getWarning())
            addRowValue.put("warning", 0);

        else
            addRowValue.put("warning", 1);

        addRowValue.put("warningReason",studentVO.getWarningReason());

        long isSuccess = dBManager.memberUpdate(addRowValue,"studentID="+ studentVO.getStudentId(),null);

        if(isSuccess >0) {

            if(studentVO.getWarning()) {
                Log.v("Search and warning presenter","경고 해제");
                view.showToast(studentVO + " : 경고해제");
            }
            else {
                Log.v("Search and warning presenter","경고 등록");
                view.showToast(studentVO + " : 경고등록");
            }
            return true;
        }
        else {
            Log.v("Search and warning presenter","경고 실패");
            view.showToast("경고에 실패하셨습니다.");
            return false;
        }
    }


    /*--------------------------View 관련 메소드--------------------------*/

    @Override
    public void setView(SearchAndWarningContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }
}
