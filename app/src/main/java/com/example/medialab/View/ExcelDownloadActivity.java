package com.example.medialab.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.medialab.Presenter.ExcelDownloadContract;
import com.example.medialab.Presenter.ExcelDownloadPresenter;
import com.example.medialab.R;

import java.io.File;

public class ExcelDownloadActivity extends BaseActivity implements ExcelDownloadContract.View {

    EditText dateEdit;
    Button downloadBtn;

    ExcelDownloadPresenter excelDownloadPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_download);

        excelDownloadPresenter = new ExcelDownloadPresenter(this);
        init();
    }

    public void init(){

        setActionBar("엑셀 저장");

        dateEdit = (EditText)findViewById(R.id.dateEditID);
        downloadBtn = (Button)findViewById(R.id.downloadBtnID);

        dateEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if((keyEvent.getAction()==KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER){
                    downloadBtn.performClick();
                    return true;
                }
                else
                    return false;
            }
        });

        dateEdit.requestFocus();
    }

    @Override
    public boolean isDateFilled() {

        String warning = dateEdit.getText().toString();
        warning = warning.trim();

        if (warning.getBytes().length <= 0)
            return false;
        else
            return true;
    }

    @Override
    public void excelDownload() {

        File xls = excelDownloadPresenter.downLoadRequest(dateEdit.getText().toString());

        if(xls==null){
            dateEdit.setText("");
            dateEdit.requestFocus();
        }


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/excel");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        Uri contentUri;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            contentUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", xls);
        else
            contentUri = Uri.fromFile(xls);

        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        startActivity(Intent.createChooser(intent,"EXCEL_SHARE"));

        dateEdit.setText("");
        dateEdit.requestFocus();
    }

    public void onClick(View view) {

        switch (view.getId()){

            case R.id.downloadBtnID:
                if(isDateFilled())
                    excelDownload();
                else{
                    showToast("날짜를 입력해주세요.");
                    dateEdit.requestFocus();
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode==RESULT_OK){
            showToast("성공적으로 저장하였습니다.");
            dateEdit.setText("");
            dateEdit.requestFocus();
        }else{
            showToast("저장에 실패하였습니다.");
            dateEdit.setText("");
            dateEdit.requestFocus();
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
