package com.example.medialab.Presenter;

import android.content.ContentValues;

import com.example.medialab.Model.DBManageServiceImpl;
import com.example.medialab.Model.StudentVO;

public class SearchPresenter extends BasePresenter implements SearchContract.Presenter {

    SearchContract.View view;

    private SearchPresenter(){
        super();
    }

    public SearchPresenter(SearchContract.View view){
        this.view = view;
        dBManager = new DBManageServiceImpl(view.getInstanceContext());
    }


    @Override
    public void modifyRequest(StudentVO studentVO) {

        boolean isDateUpdate = dBManager.isDateUpdate(studentVO.getAccessDay());

        if(!isDateUpdate)
            dBManager.updateVisitorTable(studentVO.getAccessDay());

        ContentValues addRowValue = new ContentValues();
        addRowValue.put("studentId", studentVO.getStudentId());
        addRowValue.put("name", studentVO.getName());
        addRowValue.put("department", studentVO.getDepartment());


        long isSuccess = dBManager.memberUpdate(addRowValue,"studentID="+ studentVO.getStudentId(),null);

        if(isSuccess >0) {
            view.showToast(studentVO + " : 수정완료");
            view.moveToCalledActivity(RESULT_OK);
        }
        else {
            view.showToast("수정에 실패하셨습니다.");
            view.moveToCalledActivity(RESULT_CANCELED);
        }

    }

    @Override
    public void deleteRequest(String id) {

        long isSuccess = dBManager.memberDelete("studentID="+id,null);

        if(isSuccess >0) {
            view.showToast("정보가 삭제되었습니다.");
            view.moveToCalledActivity(RESULT_OK);
        }
        else {
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
