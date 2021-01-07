package com.example.medialab.Presenter;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.medialab.Model.DBManageServiceImpl;
import com.example.medialab.Model.StudentDAO;
import com.example.medialab.Model.StudentVO;

import java.util.Calendar;

public class MainPresenter extends BasePresenter implements MainContract.Presenter{

    MainContract.View view = null;

    private MainPresenter(){
        super();
    }

    public MainPresenter(MainContract.View view){
        this.view = view;
        dBManager = new DBManageServiceImpl(view.getInstanceContext());
    }

    /*-----------------------moveToActivity 관련 메소드----------------------------*/

    @Override
    public void moveToAccessActivity(String scanData) {

        if(!isAuthentic(scanData)) {
            view.showToast("인증에 실패했습니다");
            return;
        }

        StudentVO studentVO = scanDataParsing(scanData); // QR 데이터 파싱

        Cursor memberCursor = dBManager.memberQuery(memBerColumns,"studentID="+ studentVO.getStudentId(),null,null,null,null);

        // 기존 멤버라면 -> 명부 작성
         if(memberCursor.moveToFirst()){

            studentVO.setStudentId(memberCursor.getInt(0));
            studentVO.setName(memberCursor.getString(1));
            studentVO.setDepartment(memberCursor.getString(2));

            //경고 멤버라면
            if(isWarningMember(studentVO)) {
                view.showToast("[경고] 입장불가\n" + "사유 : " + memberCursor.getString(5) + "\n" + "조교한테 문의하세요");
                return;
            }
            //오늘 첫 방문이라면
            if(isFirstVisit(studentVO))
                view.moveToAnotherActivity(studentVO,ACCESS_ACTIVITY_REQUEST_CODE);

            //오늘 첫 방문이 아니라면
            else{

                // 입장을 요청하는 경우
                if(!isExitRequest(studentVO))
                    view.moveToAnotherActivity(studentVO,ACCESS_ACTIVITY_REQUEST_CODE);

                // 퇴장을 요청하는 경우
                else{
                    calendar = Calendar.getInstance();
                    String exitTime = dateFormat.format(calendar.getTime());

                    ContentValues updateRowValue = new ContentValues();
                    updateRowValue.put("exitTime", exitTime);
                    dBManager.visitorUpdate(updateRowValue,"studentID="+ studentVO.getStudentId()+" AND _id="+getVisitSequenceID(studentVO),null);
                    view.showToast(studentVO + " 퇴장("+exitTime+")");
                }
            }
        }

        // 기존 멤버가 아니라면 -> Toast
        else
             view.showToast("정보를 등록해주세요");
    }

    @Override
    public void moveToSignUpActivity(String scanData) {

        if(!isAuthentic(scanData)) {
            view.showToast("인증에 실패했습니다");
            return;
        }

        StudentVO studentVO = scanDataParsing(scanData); // QR 데이터 파싱

        // 기존 멤버라면 -> Toast
        if(isMember(studentVO))
            view.showToast("["+String.valueOf(studentVO.getStudentId()) +"]"+ " : 이미 등록되어있습니다.");
        // 기존 멤버가 아니라면 -> 가입
        else
            view.moveToAnotherActivity(studentVO,SIGN_UP_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void moveToSearchActivity(String scanData) {

    }

    @Override
    public void moveToManagerModeActivity(String scanData) {

    }

    @Override
    public void moveToDeveloperInfoActivity() {
        view.moveToAnotherActivity(null,DEVELOPER_INFO_ACTIVITY_REQUEST_CODE);
    }

    /*--------------------------View 관련 메소드--------------------------*/

    @Override
    public void setView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }
}
