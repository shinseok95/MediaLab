package com.example.medialab.Model;

import android.content.ContentValues;
import android.database.Cursor;

public interface DBManageService {
    public long registerMember(ContentValues addRowValue);
    public long registerVisitor(ContentValues addRowValue);

    public Cursor memberQuery(String[] columns,
                              String selection,
                              String[] selectionArgs,
                              String groupBy,
                              String having,
                              String orderBy );

    public Cursor visitorQuery(String[] columns,
                               String selection,
                               String[] selectionArgs,
                               String groupBy,
                               String having,
                               String orderBy );

    public int memberUpdate( ContentValues updateRowValue,
                             String whereClause,
                             String[] whereArgs );

    public int visitorUpdate( ContentValues updateRowValue,
                              String whereClause,
                              String[] whereArgs );

    public int memberDelete( String whereClause,
                             String[] whereArgs );

    public int visitorDelete( String whereClause,
                              String[] whereArgs );

    public boolean isDateUpdate(String accessDate);

    public void updateVisitorTable(String accessDate);

    public void changeVisitorTable(String accessDate);

    public boolean isTableExist(String date);

    public void close();

}
