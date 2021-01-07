package com.example.medialab.Presenter;

import android.content.Context;

public interface BaseContract {

    interface Presenter<T> {

        void setView(T view);
        void releaseView();
    }

    interface View{
        void showToast(String msg);
        Context getInstanceContext();
        void moveToMainActivity(int resultCode);
    }
}
