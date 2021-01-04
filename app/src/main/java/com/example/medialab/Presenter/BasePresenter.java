package com.example.medialab.Presenter;

import android.database.Cursor;

import com.example.medialab.Model.DBManager;
import com.example.medialab.Model.Student;

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
    private final String authenticationCode="_KW";

    protected final int ACCESS_ACTIVITY_REQUEST_CODE = 1000;
    protected final int SIGN_UP_ACTIVITY_REQUEST_CODE = 1001;
    protected final int SEARCH_ACTIVITY_REQUEST_CODE = 1002;
    protected final int MANAGER_ACTIVITY_REQUEST_CODE = 1003;
    protected final int DEVELOPER_INFO_ACTIVITY_REQUEST_CODE = 1004;

    protected String[] memBerColumns = new String[] {"studentId","name","department","warning"};
    protected String[] visitorColumns = new String[] {"_id","name","studentId","department","purpose","computerNumber","entranceTime","exitTime"};

    protected DBManager dbManager = null;

    protected boolean isAuthentic(String scanData){

        if(scanData == null || scanData.length() < dateEndIdx)
            return false;

        String inputCode = scanData.substring(authBeginIdx,authEndIdx);

        if(inputCode.equals(authenticationCode))
            return true;
        else
            return false;
    }

    protected Student scanDataParsing(String scanData){

        int studentID = Integer.parseInt(scanData.substring(idBeginIdx,idEndIdx));
        String accessDate = scanData.substring(dateBeginIdx,dateEndIdx);
        Student student = new Student(studentID,accessDate);

        return student;
    }

    protected String getAuthenticationCode(){
        return authenticationCode;
    }

    boolean isMember(Student student){

        Cursor memberCursor = dbManager.memberQuery(memBerColumns,"studentID="+ student.getStudentId(),null,null,null,null);

        if(memberCursor.moveToFirst())
            return true;
        else
            return false;

    }
    boolean isFirstVisit(Student student){

        boolean isDateUpdate = dbManager.isDateUpdate(student.getAccessDay());

        if(!isDateUpdate)
            dbManager.updateVisitorTable(student.getAccessDay());

        Cursor visitorCursor = dbManager.visitorQuery(visitorColumns,"studentID="+ student.getStudentId(),null,null,null,null);

        if(visitorCursor.moveToFirst())
            return false;
        else
            return true;

    }
    /*
    boolean isManager(Student student){

    }
    */

    public void dbClose(){
        dbManager.close();
    }
}
