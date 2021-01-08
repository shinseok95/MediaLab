package com.example.medialab.Presenter;

import android.content.Context;

public interface BaseContract {

    interface Presenter<T> {

        void setView(T view);
        void releaseView();
    }

    interface View{
        void showToast(String msg);
        void showWarningToast(String msg);
        void showImportantToast(String msg);
        Context getInstanceContext();
        void moveToCalledActivity(int resultCode);
    }
}
