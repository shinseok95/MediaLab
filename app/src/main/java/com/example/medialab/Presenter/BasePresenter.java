package com.example.medialab.Presenter;

import android.database.Cursor;

import com.example.medialab.Model.DBManageService;
import com.example.medialab.Model.StudentDAO;
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
    private final String authenticationCode="_KW";

    protected final int ACCESS_ACTIVITY_REQUEST_CODE = 1000;
    protected final int SIGN_UP_ACTIVITY_REQUEST_CODE = 1001;
    protected final int SEARCH_ACTIVITY_REQUEST_CODE = 1002;
    protected final int MANAGER_ACTIVITY_REQUEST_CODE = 1003;
    protected final int DEVELOPER_INFO_ACTIVITY_REQUEST_CODE = 1004;

    protected String[] memBerColumns = new String[] {"studentId","name","department","warning","manager","warningReason"};
    protected String[] visitorColumns = new String[] {"_id","name","studentId","department","purpose","computerNumber","entranceTime","exitTime"};

    protected DBManageService dBManager =null;

    protected boolean isAuthentic(String scanData){

        if(scanData == null || scanData.length() < dateEndIdx)
            return false;

        String inputCode = scanData.substring(authBeginIdx,authEndIdx);

        if(inputCode.equals(authenticationCode))
            return true;
        else
            return false;
    }

    protected StudentVO scanDataParsing(String scanData){

        int studentID = Integer.parseInt(scanData.substring(idBeginIdx,idEndIdx));
        String accessDate = scanData.substring(dateBeginIdx,dateEndIdx);
        StudentVO studentVO = new StudentVO(studentID,accessDate);

        return studentVO;
    }

    boolean isFirstVisit(StudentVO studentVO){

        boolean isDateUpdate = dBManager.isDateUpdate(studentVO.getAccessDay());

        if(!isDateUpdate)
            dBManager.updateVisitorTable(studentVO.getAccessDay());

        Cursor visitorCursor = dBManager.visitorQuery(visitorColumns,"studentID="+ studentVO.getStudentId(),null,null,null,null);

        if (visitorCursor.moveToFirst())
            return false;
        else
            return true;
    }

    boolean isMember(StudentVO studentVO){
        Cursor memberCursor = dBManager.memberQuery(memBerColumns,"studentID="+ studentVO.getStudentId(),null,null,null,null);

        if(memberCursor.moveToFirst())
            return true;
        else
            return false;
    }

    boolean isManager(StudentVO studentVO){

        Cursor memberCursor = dBManager.memberQuery(memBerColumns,"studentID="+ studentVO.getStudentId(),null,null,null,null);

        if(memberCursor.moveToFirst() && memberCursor.getInt(4)==1)
            return true;
        else
            return false;
    }

    boolean isExitRequest(StudentVO studentVO){

        String exitTime;
        boolean isDateUpdate = dBManager.isDateUpdate(studentVO.getAccessDay());

        if(!isDateUpdate)
            dBManager.updateVisitorTable(studentVO.getAccessDay());

        Cursor visitorCursor = dBManager.visitorQuery(visitorColumns,"studentID="+ studentVO.getStudentId(),null,null,null,null);

        if(visitorCursor.moveToLast()){
            exitTime = visitorCursor.getString(7);

            if(exitTime!=null)
                return false;
            else
                return true;
        }
        else
            return false;
    }

    boolean isWarningMember(StudentVO studentVO){

        Cursor memberCursor = dBManager.memberQuery(memBerColumns,"studentID="+ studentVO.getStudentId(),null,null,null,null);

        if(memberCursor.moveToFirst() && memberCursor.getInt(3)==1)
            return true;
        else
            return false;
    }

    int getVisitSequenceID(StudentVO studentVO){

        int _id;
        boolean isDateUpdate = dBManager.isDateUpdate(studentVO.getAccessDay());

        if(!isDateUpdate)
            dBManager.updateVisitorTable(studentVO.getAccessDay());

        Cursor visitorCursor = dBManager.visitorQuery(visitorColumns,"studentID="+ studentVO.getStudentId(),null,null,null,null);

        if(visitorCursor.moveToLast()){
            _id = visitorCursor.getInt(0);
            return _id;
        }
        else
            return -1;
    }

    protected String getAuthenticationCode(){
        return authenticationCode;
    }


    public void dbClose(){
        dBManager.close();
    }
}
