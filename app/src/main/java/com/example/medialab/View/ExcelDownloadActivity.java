package com.example.medialab.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.medialab.Presenter.ExcelDownloadContract;
import com.example.medialab.Presenter.ExcelDownloadPresenter;
import com.example.medialab.R;

import java.io.File;

/** DB에서 특정일의 엑셀 파일을 저장 및 삭제할 수 있는 Activity입니다.
 *
 * DB에서 visitor 데이터를 받아와 POI 오픈소스를 통해 엑셀 파일로 저장합니다.
 * 그 후, 암시적 intent를 활용하여 파일을 공유합니다.
 * 만약 사용자가 특정 날짜의 DB table을 삭제하고자 한다면, 삭제도 가능합니다.
 */

public class ExcelDownloadActivity extends BaseActivity implements ExcelDownloadContract.View {

    private EditText dateEdit;
    private Button downloadBtn;
    private ExcelDownloadPresenter excelDownloadPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_download);

        excelDownloadPresenter = new ExcelDownloadPresenter(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        excelDownloadPresenter.releaseView();
        excelDownloadPresenter=null;
    }

    public void init(){

        setActionBar("엑셀 관리");

        dateEdit = (EditText)findViewById(R.id.dateEditID);
        downloadBtn = (Button)findViewById(R.id.downloadBtnID);

        dateEdit.setHint(excelDownloadPresenter.getTodayDate());
        dateEdit.setText(excelDownloadPresenter.getTodayDate());

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

    // 엑셀 파일 저장 및 공유 메소드
    @Override
    public void excelDownload() {

        String inputDate = dateEdit.getText().toString();
        File xls = excelDownloadPresenter.downLoadRequest(inputDate);

        if(xls==null){
            dateEdit.setText("");
            dateEdit.requestFocus();
            return;
        }

        // 엑셀 공유
        try {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/excel");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            Uri contentUri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                contentUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", xls);
            else
                contentUri = Uri.fromFile(xls);

            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(intent, "EXCEL_SHARE"));

        }catch (Exception e){
            Log.e("Excel download presenter","엑셀 공유 실패 : "+e.getMessage());
            showToast("엑셀 공유에 실패하였습니다.");
        }
        Log.v("Excel download presenter","엑셀 공유 성공");
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

                // DB에서 table을 삭제하고자 하는 경우
            case R.id.excelDeleteBtnID:

                if(isDateFilled()) {

                    String inputDate = dateEdit.getText().toString();

                    new AlertDialog.Builder(ExcelDownloadActivity.this)
                            .setMessage(inputDate+" : 해당 날짜의 데이터를 삭제하시겠습니까?\n")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which){

                                            boolean isDeleteSuccess = excelDownloadPresenter.deleteTableRequest(inputDate);

                                            if(isDeleteSuccess)
                                                showToast("성공적으로 삭제되었습니다.");
                                        }
                                    }
                            )
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which){
                                }
                            })
                            .show();
                }
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

    @Override
    public boolean isDateFilled() {

        String warning = dateEdit.getText().toString();
        warning = warning.trim();

        if (warning.getBytes().length <= 0)
            return false;
        else
            return true;
    }
}
