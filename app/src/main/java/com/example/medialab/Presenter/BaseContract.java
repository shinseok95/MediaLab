package com.example.medialab.Presenter;

import android.content.Context;

import com.example.medialab.Model.Student;

public interface BaseContract {

    public interface Presenter<T> {

        void setView(T view);
        void releaseView();
    }

    public interface View{
        public void showToast(String msg);
        public Context getInstanceContext();
        public void moveToMainActivity(int resultCode);
    }
}
