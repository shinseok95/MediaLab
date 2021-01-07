package com.example.medialab.Presenter;

import com.example.medialab.Model.DBManageServiceImpl;

public class SearchPresenter extends BasePresenter implements SearchContract.Presenter {

    SearchContract.View view;

    private SearchPresenter(){
        super();
    }

    public SearchPresenter(SearchContract.View view){
        this.view = view;
        dBManager = new DBManageServiceImpl(view.getInstanceContext());
    }

    /*--------------------------View 관련 메소드--------------------------*/

    @Override
    public void setView(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }
}
