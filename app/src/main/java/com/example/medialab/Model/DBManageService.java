package com.example.medialab.Model;

import android.content.ContentValues;
import android.database.Cursor;

public interface DBManageService {
    long registerMember(ContentValues addRowValue);
    long registerVisitor(ContentValues addRowValue);

    Cursor memberQuery(String[] columns,
                              String selection,
                              String[] selectionArgs,
                              String groupBy,
                              String having,
                              String orderBy );

    Cursor visitorQuery(String[] columns,
                               String selection,
                               String[] selectionArgs,
                               String groupBy,
                               String having,
                               String orderBy );

    int memberUpdate( ContentValues updateRowValue,
                             String whereClause,
                             String[] whereArgs );

    int visitorUpdate( ContentValues updateRowValue,
                              String whereClause,
                              String[] whereArgs );

    int memberDelete( String whereClause,
                             String[] whereArgs );

    int visitorDelete( String whereClause,
                              String[] whereArgs );

    boolean isDateUpdate(String accessDate);

    void updateVisitorTable(String accessDate);
    void deleteVisitorTable(String accessDate);

    void changeVisitorTable(String accessDate);

    boolean isTableExist(String date);

    void close();

}
