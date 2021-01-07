package com.example.medialab.Presenter;

import com.example.medialab.Model.StudentVO;

public interface MainContract {

    interface View extends BaseContract.View{

        void moveToAnotherActivity(StudentVO studentVO, int requestCode);
    }

    interface Presenter extends BaseContract.Presenter<View>{

        @Override
        void setView(View view);

        @Override
        void releaseView();

        void moveToAccessActivity(String scanData);
        void moveToSearchActivity(String scanData);
        void moveToSignUpActivity(String scanData);
        void moveToManagerModeActivity(String scanData);
        void moveToDeveloperInfoActivity();
    }
}
