package com.example.medialab.Presenter;

import com.example.medialab.Model.Student;

public interface SignUpContract extends BaseContract{

    interface View extends BaseContract.View{

        public boolean isNameFilled();
        public boolean isDepartmentFilled();


    }

    interface Presenter extends BaseContract.Presenter<SignUpContract.View>{

        @Override
        void setView(SignUpContract.View view);

        @Override
        void releaseView();

        public void signUpRequest(Student student);

    }

}
