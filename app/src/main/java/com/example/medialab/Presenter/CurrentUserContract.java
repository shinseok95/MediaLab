package com.example.medialab.Presenter;

import com.example.medialab.Model.StudentVO;

import java.util.ArrayList;

public interface CurrentUserContract extends BaseContract{

    interface View extends BaseContract.View{

    }

    interface Presenter extends BaseContract.Presenter<CurrentUserContract.View>{

        @Override
        void setView(CurrentUserContract.View view);
        @Override
        void releaseView();

        ArrayList<StudentVO> studentListRequest();
        boolean setWarningRequest(StudentVO studentVO);
    }
}
