package com.example.medialab.Presenter;

import android.content.ContentValues;
import android.util.Log;

import com.example.medialab.Model.DBManageServiceImpl;
import com.example.medialab.Model.StudentVO;

public class SearchPresenter extends BasePresenter implements SearchContract.Presenter {

    private SearchContract.View view;

    private SearchPresenter(){
        super();
    }

    public SearchPresenter(SearchContract.View view){
        this.view = view;
        dBManager = new DBManageServiceImpl(view.getInstanceContext());
    }

    // 학생 정보 수정 메소드
    @Override
    public void modifyRequest(StudentVO studentVO) {

        boolean isMemberTableExist = dBManager.isMemberTableExist();

        if(!isMemberTableExist){
            dBManager.updateMemberTable();
        }

        ContentValues addRowValue = new ContentValues();
        addRowValue.put("studentId", studentVO.getStudentId());
        addRowValue.put("name", studentVO.getName());
        addRowValue.put("department", studentVO.getDepartment());

        long isSuccess = dBManager.memberUpdate(addRowValue,"studentID="+ studentVO.getStudentId(),null);

        if(isSuccess >0) {
            Log.v("Search Presenter","정보 수정 성공");
            view.showToast(studentVO + " : 수정완료");
            view.moveToCalledActivity(RESULT_OK);
        }
        else {
            Log.v("Search Presenter","정보 수정 실패");
            view.showToast("수정에 실패하셨습니다.");
            view.moveToCalledActivity(RESULT_CANCELED);
        }
    }

    // 학생 정보 삭제 메소드
    @Override
    public void deleteRequest(String id) {

        boolean isMemberTableExist = dBManager.isMemberTableExist();

        if(!isMemberTableExist){
            dBManager.updateMemberTable();
        }

        //경고 멤버라면
        if(isWarningMember(id)) {
            Log.v("Search Presenter",id+" : 경고로 인한 정보 삭제 제한");
            view.showWarningToast("[경고] 삭제불가\t\t\n" + "사유 : " + "경고로 인한 삭제 불가" + "\n\n" + "조교한테 문의하세요");
            return;
        }

        long isSuccess = dBManager.memberDelete("studentID="+id,null);

        if(isSuccess >0) {
            Log.v("Search Presenter","정보 삭제 성공");
            view.showToast("정보가 삭제되었습니다.");
            view.moveToCalledActivity(RESULT_OK);
        }
        else {
            Log.v("Search Presenter","정보 수정 실패");
            view.showToast("삭제에 실패하셨습니다.");
            view.moveToCalledActivity(RESULT_CANCELED);
        }
    }

    /*--------------------------View 관련 메소드--------------------------*/

    @Override
    public void setView(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }

}
