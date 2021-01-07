package com.example.medialab.Presenter;

import com.example.medialab.Model.StudentVO;

public interface SignUpContract extends BaseContract{

    interface View extends BaseContract.View{

        boolean isNameFilled();
        boolean isDepartmentFilled();
    }

    interface Presenter extends BaseContract.Presenter<SignUpContract.View>{

        @Override
        void setView(SignUpContract.View view);

        @Override
        void releaseView();

        void signUpRequest(StudentVO studentVO);
    }

}
