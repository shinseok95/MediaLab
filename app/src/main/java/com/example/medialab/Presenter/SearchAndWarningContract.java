package com.example.medialab.Presenter;

import com.example.medialab.Model.StudentVO;

public interface SearchAndWarningContract extends BaseContract {

    interface View extends BaseContract.View{

        boolean setWarningStatus(boolean status);

        boolean isStudentIDFilled();
        boolean isWarningFilled();

    }

    interface Presenter extends BaseContract.Presenter<SearchAndWarningContract.View>{

        @Override
        void setView(SearchAndWarningContract.View view);

        @Override
        void releaseView();

        StudentVO memberSearchRequest(String studentID);
        boolean setWarningRequest(StudentVO studentVO);
    }

}
