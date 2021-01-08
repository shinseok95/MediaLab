package com.example.medialab.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StudentDAO extends SQLiteOpenHelper {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    Calendar calendar = Calendar.getInstance();

    private static final String FILE_NAME = "EntryList.db";
    private static final String TABLE_MEMBER= "Member";
    private String TABLE_TODAY_VISITOR_LIST ="Day_" + dateFormat.format(calendar.getTime());
    private static final int DB_VERSION = 1;

    private Context mContext = null;
    private static StudentDAO mStudentDAO = null;

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

        // 일단 비워둠
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

        if(TABLE_TODAY_VISITOR_LIST.equals("Day_"+accessDate))
            return true;
        else
            return false;
    }

    public void updateVisitorTable(String accessDate){

        TABLE_TODAY_VISITOR_LIST ="Day_" + accessDate;

        getWritableDatabase().execSQL(

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

    public void close(){

        mStudentDAO =null;
        mContext=null;
    }

}
