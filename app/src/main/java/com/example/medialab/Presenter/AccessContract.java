package com.example.medialab.Presenter;

import com.example.medialab.Model.Student;

public interface AccessContract extends BaseContract{


    interface View extends BaseContract.View{

        public boolean isComputerNumberFilled();
        public boolean isPurposeFilled();

    }

    interface Presenter extends BaseContract.Presenter<AccessContract.View>{

        @Override
        void setView(AccessContract.View view);

        @Override
        void releaseView();

        public void accessRequest(Student student);

    }

}
