package com.example.medialab.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StudentDAO extends SQLiteOpenHelper {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("_yyyyMMdd");
    private Calendar calendar = Calendar.getInstance();

    private static final String FILE_NAME = "EntryList.db";
    private static final String TABLE_MEMBER= "Member";
    private String TABLE_TODAY_VISITOR_LIST =dateFormat.format(calendar.getTime());
    private static final int DB_VERSION = 1;

    private Context mContext = null;
    private static StudentDAO mStudentDAO = null;

    // 싱글톤으로 객체 생성
    public static StudentDAO getInstance(Context context){

        if(mStudentDAO ==null) {
            synchronized (StudentDAO.class) {
                mStudentDAO = new StudentDAO(context,FILE_NAME,null,DB_VERSION);
            }
        }
        return mStudentDAO;
    }

    private StudentDAO(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version){
        super(context,dbName,factory,version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(

                "CREATE TABLE IF NOT EXISTS "+ TABLE_MEMBER +
                        "(" + "studentId INTEGER PRIMARY KEY, " +
                        "name TEXT, "+
                        "department TEXT, "+
                        "warning INTEGER, "+
                        "warningReason TEXT);"

        );

        sqLiteDatabase.execSQL(

                "CREATE TABLE IF NOT EXISTS "+ TABLE_TODAY_VISITOR_LIST +
                        "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, "+
                        "studentId INTEGER, "+
                        "department TEXT, "+
                        "purpose TEXT, "+
                        "computerNumber TEXT, "+
                        "entranceTime TEXT, "+
                        "exitTime TEXT );"

        );
    }

    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(

                "CREATE TABLE IF NOT EXISTS "+ TABLE_TODAY_VISITOR_LIST +
                        "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, "+
                        "studentId INTEGER, "+
                        "department TEXT, "+
                        "purpose TEXT, "+
                        "computerNumber TEXT, "+
                        "entranceTime TEXT, "+
                        "exitTime TEXT );"

        );
        super.onOpen(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // 일단 비워둠
        if(oldVersion<newVersion){ }
    }

    public long registerMember(ContentValues addRowValue){

        return getWritableDatabase().insert(TABLE_MEMBER, null, addRowValue);
    }
    public long registerVisitor(ContentValues addRowValue){

        return getWritableDatabase().insert(TABLE_TODAY_VISITOR_LIST, null, addRowValue);
    }

    public Cursor memberQuery(String[] columns,
                        String selection,
                        String[] selectionArgs,
                        String groupBy,
                        String having,
                        String orderBy )
    {

        return getReadableDatabase().query( TABLE_MEMBER,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);
    }

    public Cursor visitorQuery(String[] columns,
                              String selection,
                              String[] selectionArgs,
                              String groupBy,
                              String having,
                              String orderBy )
    {
        return getReadableDatabase().query( TABLE_TODAY_VISITOR_LIST,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);
    }

    public int memberUpdate( ContentValues updateRowValue,
                                  String whereClause,
                                  String[] whereArgs )
    {

        return getWritableDatabase().update( TABLE_MEMBER,
                updateRowValue,
                whereClause,
                whereArgs );
    }

    public int visitorUpdate( ContentValues updateRowValue,
                             String whereClause,
                             String[] whereArgs )
    {

        return getWritableDatabase().update( TABLE_TODAY_VISITOR_LIST,
                updateRowValue,
                whereClause,
                whereArgs );
    }

    public int memberDelete( String whereClause,
                       String[] whereArgs )
    {
        return getWritableDatabase().delete( TABLE_MEMBER,
                whereClause, whereArgs);
    }

    public int visitorDelete( String whereClause,
                             String[] whereArgs )
    {
        return getWritableDatabase().delete( TABLE_TODAY_VISITOR_LIST,
                whereClause, whereArgs);
    }

    public boolean isDateUpdate(String accessDate){

        String parsingDate = '_'+accessDate;
        if(TABLE_TODAY_VISITOR_LIST.equals(parsingDate))
            return true;
        else
            return false;
    }

    public void updateVisitorTable(String accessDate){

        String parsingDate = '_'+accessDate;
        TABLE_TODAY_VISITOR_LIST =parsingDate;

        try {
            getWritableDatabase().execSQL(

                    "CREATE TABLE IF NOT EXISTS " + TABLE_TODAY_VISITOR_LIST +
                            "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "name TEXT, " +
                            "studentId INTEGER, " +
                            "department TEXT, " +
                            "purpose TEXT, " +
                            "computerNumber TEXT, " +
                            "entranceTime TEXT, " +
                            "exitTime TEXT );"

            );
            Log.v("StudentDAO","Visitor 테이블 수정 성공 : "+parsingDate);
        }catch (Exception e){
            Log.e("StudentDAO","Visitor 테이블 수정 실패 : "+parsingDate);
        }
    }

    public void updateMemberTable(){

        try {
            getWritableDatabase().execSQL(

                    "CREATE TABLE IF NOT EXISTS "+ TABLE_MEMBER +
                            "(" + "studentId INTEGER PRIMARY KEY, " +
                            "name TEXT, "+
                            "department TEXT, "+
                            "warning INTEGER, "+
                            "warningReason TEXT);"

            );
            Log.v("StudentDAO","Member 테이블 수정 성공 : ");
        }catch (Exception e){
            Log.e("StudentDAO","Member 테이블 수정 실패 : ");
        }
    }

    public void deleteVisitorTable(String accessDate){

        String parsingDate = '_'+accessDate;
        TABLE_TODAY_VISITOR_LIST = parsingDate;

        try {
            getWritableDatabase().execSQL(
                    "DROP TABLE IF EXISTS " + TABLE_TODAY_VISITOR_LIST + ";"
            );
            Log.v("StudentDAO","Visitor 테이블 삭제 성공 : "+parsingDate);
        }catch (Exception e){
            Log.e("StudentDAO","Visitor 테이블 삭제 실패 : "+e.getMessage());
        }
    }

    public void changeVisitorTable(String accessDate){

        String parsingDate = '_'+accessDate;
        TABLE_TODAY_VISITOR_LIST =parsingDate;
    }

    public boolean isVisitorTableExist(String date){

        String parsingDate = '_'+date;
        String query = "SELECT DISTINCT tbl_name from sqlite_master where tbl_name = '"+parsingDate+"'";

        try (Cursor cursor = getReadableDatabase().rawQuery(query, null)) {
            if(cursor!=null) {
                if(cursor.getCount()>0) {
                    cursor.close();
                    return true;
                }
            }
        }catch (Exception e){
            Log.e("StudentDAO","Visitor 테이블 조회 실패 : "+e.getMessage());
        }
        return false;
    }

    public boolean isMemberTableExist(){

        String query = "SELECT DISTINCT tbl_name from sqlite_master where tbl_name = '"+TABLE_MEMBER+"'";

        try (Cursor cursor = getReadableDatabase().rawQuery(query, null)) {
            if(cursor!=null) {
                if(cursor.getCount()>0) {
                    cursor.close();
                    return true;
                }
            }
        }catch (Exception e){
            Log.e("StudentDAO","Member 테이블 조회 실패 : "+e.getMessage());
        }
        return false;
    }

    public void close(){

        mStudentDAO =null;
        mContext=null;
    }

}
