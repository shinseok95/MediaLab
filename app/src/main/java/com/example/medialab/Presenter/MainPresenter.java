package com.example.medialab.Presenter;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.medialab.Model.DBManager;
import com.example.medialab.Model.Student;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainPresenter extends BasePresenter implements MainContract.Presenter{

    MainContract.View view = null;

    public MainPresenter(){
        super();
    }

    public MainPresenter(MainContract.View view){
        this.view = view;
        dbManager = DBManager.getInstance(view.getInstanceContext());
    }

    /*-----------------------moveToActivity 관련 메소드----------------------------*/

    @Override
    public void moveToAccessActivity(String scanData) {

        Student student;
        boolean isIdentifiableCode  = isAuthentic(scanData); // QR 인증
        boolean isFirstVisit;

        if(!isIdentifiableCode) {
            view.showToast("인증에 실패했습니다");
            return;
        }

        student = scanDataParsing(scanData); // QR 데이터 파싱

        Cursor memberCursor = dbManager.memberQuery(memBerColumns,"studentID="+ student.getStudentId(),null,null,null,null);

        // 기존 멤버라면 -> 명부 작성

         if(memberCursor.moveToFirst()){

            student.setStudentId(memberCursor.getInt(0));
            student.setName(memberCursor.getString(1));
            student.setDepartment(memberCursor.getString(2));

             isFirstVisit=isFirstVisit(student);

            //오늘 첫 방문이라면
            if(isFirstVisit)
                view.moveToAnotherActivity(student,ACCESS_ACTIVITY_REQUEST_CODE);

            //오늘 첫 방문이 아니라면
            else{
                calendar = Calendar.getInstance();
                String exitTime = dateFormat.format(calendar.getTime());

                ContentValues updateRowValue = new ContentValues();
                updateRowValue.put("exitTime", exitTime);
                dbManager.visitorUpdate(updateRowValue,"studentID="+ student.getStudentId(),null);
                view.showToast("("+String.valueOf(exitTime)+") "+student + " 퇴장");
            }
        }

        // 기존 멤버가 아니라면 -> Toast
        else
             view.showToast("정보를 등록해주세요");
    }

    @Override
    public void moveToSignUpActivity(String scanData) {

        Student student;
        boolean isIdentifiableCode  = isAuthentic(scanData); // QR 인증

        boolean isMember;

        if(!isIdentifiableCode) {
            view.showToast("인증에 실패했습니다");
            return;
        }

        student = scanDataParsing(scanData); // QR 데이터 파싱

        isMember = isMember(student);

        // 기존 멤버라면 -> Toast
        if(isMember)
            view.showToast("["+String.valueOf(student.getStudentId()) +"]"+ " : 이미 등록되어있습니다.");
        // 기존 멤버가 아니라면 -> 가입
        else
            view.moveToAnotherActivity(student,SIGN_UP_ACTIVITY_REQUEST_CODE);
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


    /*--------------------------기타 메소드---------------------------*/
}
