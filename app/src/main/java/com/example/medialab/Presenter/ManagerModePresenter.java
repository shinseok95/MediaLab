package com.example.medialab.Presenter;

public class ManagerModePresenter extends BasePresenter implements ManagerModeContract.Presenter {

    protected final int EXCEL_DOWNLOAD_ACTIVITY_REQUEST_CODE = 1006;
    protected final int MEMBER_SEARCH_ACTIVITY_REQUEST_CODE = 1007;
    protected final int VISITOR_SEARCH_ACTIVITY_REQUEST_CODE = 1008;
    protected final int OTP_CHANGE_ACTIVITY_REQUEST_CODE = 1009;

    ManagerModeContract.View view = null;

    private ManagerModePresenter(){
        super();
    }

    public ManagerModePresenter(ManagerModeContract.View view){
        this.view = view;
    }

    @Override
    public void moveToExcelDownloadActivity() {
        view.moveToAnotherActivity(EXCEL_DOWNLOAD_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void moveToMemberSearchActivity() {
        view.moveToAnotherActivity(MEMBER_SEARCH_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void moveToVisitorSearchActivity() {
        view.moveToAnotherActivity(VISITOR_SEARCH_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void moveToOtpChangeActivity() {
        view.moveToAnotherActivity(OTP_CHANGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void setView(ManagerModeContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }
}
