package com.example.medialab.Presenter;

import com.example.medialab.Model.StudentVO;

public interface AccessContract extends BaseContract{


    interface View extends BaseContract.View{

        boolean isComputerNumberFilled();
        boolean isPurposeFilled();
    }

    interface Presenter extends BaseContract.Presenter<AccessContract.View>{

        @Override
        void setView(AccessContract.View view);
        @Override
        void releaseView();

        void accessRequest(StudentVO studentVO);

    }

}
