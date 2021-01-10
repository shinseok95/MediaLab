package com.example.medialab.Presenter;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.medialab.Model.DBManageServiceImpl;
import com.example.medialab.R;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelDownloadPresenter extends BasePresenter implements ExcelDownloadContract.Presenter {

    ExcelDownloadContract.View view;

    private ExcelDownloadPresenter(){
        super();
    }

    public ExcelDownloadPresenter(ExcelDownloadContract.View view){
        this.view = view;
        dBManager = new DBManageServiceImpl(view.getInstanceContext());
    }

    @Override
    public File downLoadRequest(String date) {

        if(!dBManager.isTableExist(date)){
            view.showToast("해당 날짜의 데이터가 존재하지 않습니다.\n날짜를 다시 확인해주세요.");
            return null;
        }

        int visitorIdx=0;

        if(!dBManager.isDateUpdate(date))
            dBManager.changeVisitorTable(date);

        String parsingDate = date.substring(0,4)+'-'+date.substring(4,6)+'-'+date.substring(6);
        Cursor visitorCursor = dBManager.visitorQuery(visitorColumns,null,null,null,null,null);

        if(visitorCursor.getCount()==0){
            view.showToast("해당 날짜에는 아무도 이용하지 않았습니다.\n날짜를 다시 확인해주세요.");
            return null;
        }

        Workbook wb = new HSSFWorkbook();

        Sheet sheet = wb.createSheet(parsingDate);

        Row row = sheet.createRow(0); // 새로운 행 생성
        visitorIdx++;
        Cell cell;

        cell = row.createCell(0);
        cell.setCellValue("이름");

        cell = row.createCell(1);
        cell.setCellValue("학번");

        cell = row.createCell(2);
        cell.setCellValue("이용 목적");

        cell = row.createCell(3);
        cell.setCellValue("컴퓨터 번호");

        cell = row.createCell(4);
        cell.setCellValue("입실 시간");

        cell = row.createCell(5);
        cell.setCellValue("퇴실 시간");

        while(visitorCursor.moveToNext()){

            row = sheet.createRow(visitorIdx);
            cell = row.createCell(0);
            cell.setCellValue(visitorCursor.getString(1));
            cell = row.createCell(1);
            cell.setCellValue(visitorCursor.getInt(2)+'('+visitorCursor.getString(3)+')');
            cell = row.createCell(2);
            cell.setCellValue(visitorCursor.getString(4));
            cell = row.createCell(3);
            cell.setCellValue(visitorCursor.getString(5));
            cell = row.createCell(4);
            cell.setCellValue(visitorCursor.getString(6));
            cell = row.createCell(5);
            cell.setCellValue(visitorCursor.getString(7));

            visitorIdx++;
        }

        //File dir = Environment.getExternalStorageDirectory();
        //File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File xls = new File(view.getInstanceContext().getFilesDir(), parsingDate+".xls");
        try {
            FileOutputStream os = new FileOutputStream(xls);
            wb.write(os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xls;
    }

    /*--------------------------View 관련 메소드--------------------------*/

    @Override
    public void setView(ExcelDownloadContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }

}