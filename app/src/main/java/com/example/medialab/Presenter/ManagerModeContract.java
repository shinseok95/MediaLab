package com.example.medialab.Presenter;

import com.example.medialab.Model.StudentVO;

public interface ManagerModeContract extends BaseContract {

    interface View extends BaseContract.View{

        void moveToAnotherActivity(int requestCode);
    }

    interface Presenter extends BaseContract.Presenter<ManagerModeContract.View>{

        @Override
        void setView(ManagerModeContract.View view);

        @Override
        void releaseView();

        void moveToExcelDownloadActivity();
        void moveToMemberSearchActivity();
        void moveToVisitorSearchActivity();
        void moveToOtpChangeActivity();
    }
}
