package com.example.medialab.Presenter;

public interface OptContract extends BaseContract {

    interface View extends BaseContract.View{

    }

    interface Presenter extends BaseContract.Presenter<OptContract.View>{

        @Override
        void setView(OptContract.View view);
        @Override
        void releaseView();

        void validateRequest(String otp,int count);

        boolean validateChangeRequest(String otp,int count);
    }

}
