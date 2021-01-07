package com.example.medialab.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBManageServiceImpl implements DBManageService {

    StudentDAO studentDAO;

    private DBManageServiceImpl(){}

    public DBManageServiceImpl(Context context){
        this.studentDAO = StudentDAO.getInstance(context);
    }

    @Override
    public long registerMember(ContentValues addRowValue) {
        return studentDAO.registerMember(addRowValue);
    }

    @Override
    public long registerVisitor(ContentValues addRowValue) {
        return studentDAO.registerVisitor(addRowValue);
    }

    @Override
    public Cursor memberQuery(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return studentDAO.memberQuery(columns,selection,selectionArgs,groupBy,having,orderBy);
    }

    @Override
    public Cursor visitorQuery(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return studentDAO.visitorQuery(columns,selection,selectionArgs,groupBy,having,orderBy);
    }

    @Override
    public int memberUpdate(ContentValues updateRowValue, String whereClause, String[] whereArgs) {
        return studentDAO.memberUpdate(updateRowValue,whereClause,whereArgs);
    }

    @Override
    public int visitorUpdate(ContentValues updateRowValue, String whereClause, String[] whereArgs) {
        return studentDAO.visitorUpdate(updateRowValue,whereClause,whereArgs);
    }

    @Override
    public int memberDelete(String whereClause, String[] whereArgs) {
        return studentDAO.memberDelete(whereClause,whereArgs);
    }

    @Override
    public int visitorDelete(String whereClause, String[] whereArgs) {
        return studentDAO.visitorDelete(whereClause,whereArgs);
    }

    @Override
    public boolean isDateUpdate(String accessDate) {
        return studentDAO.isDateUpdate(accessDate);
    }

    @Override
    public void updateVisitorTable(String accessDate) {
        studentDAO.updateVisitorTable(accessDate);
    }

    @Override
    public void close() {
        studentDAO.close();
    }
}
