package com.example.medialab.Presenter;

import android.database.Cursor;
import android.util.Log;

import com.example.medialab.Model.DBManageService;
import com.example.medialab.Model.StudentVO;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BasePresenter {

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    protected Calendar calendar;

    protected final int RESULT_CANCELED = 0;
    protected final int RESULT_OK = 1;

    private final int authBeginIdx =0;
    private final int authEndIdx =3;
    private final int idBeginIdx = 8;
    private final int idEndIdx = 18;
    private final int dateBeginIdx = 18;
    private final int dateEndIdx = 26;
    private final int hourBeginIdx =26;
    private final int hourEndIdx =28;
    private final int minuteBeginIdx =28;
    private final int minuteEndIdx =30;
    private final int secondBeginIdx =30;
    private final int secondEndIdx =32;

    private final String authenticationCode="_KW";

    protected String[] memBerColumns = new String[] {"studentId","name","department","warning","warningReason"};
    protected String[] visitorColumns = new String[] {"_id","name","studentId","department","purpose","computerNumber","entranceTime","exitTime"};

    protected DBManageService dBManager =null;

    // 인증된 qr인지 확인하는 메소드
    protected boolean isAuthentic(String scanData){

        if(scanData == null || scanData.length() < dateEndIdx)
            return false;

        String inputCode = scanData.substring(authBeginIdx,authEndIdx);

        if(inputCode.equals(authenticationCode))
            return true;
        else
            return false;
    }

    // 최근 1분간 발급받은 qr인지 확인하는 메소드
    protected boolean isRecentlyQR(String scanTime){

        calendar = Calendar.getInstance();
        String parsedScanTime = scanTime.replaceAll(":","");
        String currentTime = dateFormat.format(calendar.getTime()).replaceAll(":","");
        calendar=null;

        int integerScanTime = Integer.valueOf(parsedScanTime);
        int integerCurrentTime = Integer.valueOf(currentTime);

        if(integerCurrentTime - integerScanTime < 100)
            return true;
        else
            return false;
    }

    // QR에서 학번과 접근 시간을 파싱하는 메소드
    public StudentVO scanDataParsing(String scanData){

        int studentID = Integer.parseInt(scanData.substring(idBeginIdx,idEndIdx));
        String accessDate = scanData.substring(dateBeginIdx,dateEndIdx);
        String entranceTime = scanData.substring(hourBeginIdx,hourEndIdx) +':'+scanData.substring(minuteBeginIdx,minuteEndIdx)+':'+scanData.substring(secondBeginIdx,secondEndIdx);

        StudentVO studentVO = new StudentVO(studentID,accessDate,entranceTime);

        return studentVO;
    }

    // 첫 방문인지 확인하는 메소드
    public boolean isFirstVisit(StudentVO studentVO){

        boolean isDateUpdate = dBManager.isDateUpdate(studentVO.getAccessDay());
        boolean isTableExist = dBManager.isVisitorTableExist(studentVO.getAccessDay());

        if(!isDateUpdate || !isTableExist)
            dBManager.updateVisitorTable(studentVO.getAccessDay());

        Cursor visitorCursor = dBManager.visitorQuery(visitorColumns,"studentID="+ studentVO.getStudentId(),null,null,null,null);

        if (visitorCursor.moveToFirst()) {
            visitorCursor.close();
            return false;
        }
        else {
            visitorCursor.close();
            return true;
        }
    }

    // 등록된 학생인지 확인하는 메소드
    public boolean isMember(StudentVO studentVO){

        boolean isMemberTableExist = dBManager.isMemberTableExist();

        if(!isMemberTableExist){
            dBManager.updateMemberTable();
        }

        Cursor memberCursor = dBManager.memberQuery(memBerColumns,"studentID="+ studentVO.getStudentId(),null,null,null,null);

        if(memberCursor.moveToFirst()) {
            memberCursor.close();
            return true;
        }
        else {
            memberCursor.close();
            return false;
        }
    }

    // qr 스캔시 입장을 요청하는 건지 퇴장을 요청하는 건지 확인하는 메소드
    public boolean isExitRequest(StudentVO studentVO){

        String exitTime;
        boolean isDateUpdate = dBManager.isDateUpdate(studentVO.getAccessDay());
        boolean isTableExist = dBManager.isVisitorTableExist(studentVO.getAccessDay());

        if(!isDateUpdate || !isTableExist)
            dBManager.updateVisitorTable(studentVO.getAccessDay());

        Cursor visitorCursor = dBManager.visitorQuery(visitorColumns,"studentID="+ studentVO.getStudentId(),null,null,null,null);

        if(visitorCursor.moveToLast()){
            exitTime = visitorCursor.getString(7);

            visitorCursor.close();

            if(exitTime!=null)
                return false;
            else
                return true;
        }
        else
            return false;
    }

    // 경고를 받은 학생인지 확인하는 메소드(studentVO)
    public boolean isWarningMember(StudentVO studentVO){

        boolean isMemberTableExist = dBManager.isMemberTableExist();

        if(!isMemberTableExist){
            dBManager.updateMemberTable();
        }

        Cursor memberCursor = dBManager.memberQuery(memBerColumns,"studentID="+ studentVO.getStudentId(),null,null,null,null);
        if(memberCursor.moveToFirst() && memberCursor.getInt(3)==1) {
            memberCursor.close();
            return true;
        }
        else {
            memberCursor.close();
            return false;
        }
    }
    // 경고를 받은 학생인지 확인하는 메소드
    public boolean isWarningMember(String id){

        boolean isMemberTableExist = dBManager.isMemberTableExist();

        if(!isMemberTableExist){
            dBManager.updateMemberTable();
        }

        Cursor memberCursor = dBManager.memberQuery(memBerColumns,"studentID="+ id,null,null,null,null);
        if(memberCursor.moveToFirst() && memberCursor.getInt(3)==1) {
            memberCursor.close();
            return true;
        }
        else {
            memberCursor.close();
            return false;
        }
    }



    // 이번 입장이 오늘의 몇 번째 입장인지 확인하는 메소드
    public int getVisitSequenceID(StudentVO studentVO){

        int _id;
        boolean isDateUpdate = dBManager.isDateUpdate(studentVO.getAccessDay());
        boolean isTableExist = dBManager.isVisitorTableExist(studentVO.getAccessDay());

        if(!isDateUpdate || !isTableExist)
            dBManager.updateVisitorTable(studentVO.getAccessDay());


        Cursor visitorCursor = dBManager.visitorQuery(visitorColumns,"studentID="+ studentVO.getStudentId(),null,null,null,null);

        if(visitorCursor.moveToLast()){
            _id = visitorCursor.getInt(0);
            visitorCursor.close();
            return _id;
        }
        else {
            visitorCursor.close();
            return -1;
        }
    }

    // 오늘 날짜를 확인하는 메소드
    public  String getTodayDate(){

        SimpleDateFormat todayDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        return todayDateFormat.format(calendar.getTime());
    }

    // QR 코드에서 인증해야할 문자를 제공하는 메소드
    protected String getAuthenticationCode(){
        return authenticationCode;
    }

    protected void dbClose(){
        dBManager.close();
        dBManager=null;
    }
}
