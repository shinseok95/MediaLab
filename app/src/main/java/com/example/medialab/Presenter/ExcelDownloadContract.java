package com.example.medialab.Presenter;

import java.io.File;

public interface ExcelDownloadContract extends BaseContract {

    interface View extends BaseContract.View{

        boolean isDateFilled();
        void excelDownload();
    }

    interface Presenter extends BaseContract.Presenter<ExcelDownloadContract.View>{

        @Override
        void setView(ExcelDownloadContract.View view);

        @Override
        void releaseView();

        File downLoadRequest(String date);
        boolean deleteTableRequest(String accessDate);
    }
}
