package com.example.medialab.Presenter;

import com.example.medialab.Model.StudentVO;

public interface SearchContract extends BaseContract {

    interface View extends BaseContract.View{

    }

    interface Presenter extends BaseContract.Presenter<SearchContract.View>{

        @Override
        void setView(SearchContract.View view);

        @Override
        void releaseView();

    }

}
