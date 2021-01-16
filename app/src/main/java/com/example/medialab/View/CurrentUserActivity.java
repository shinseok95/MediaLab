package com.example.medialab.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.medialab.Model.StudentVO;
import com.example.medialab.Presenter.CurrentUserContract;
import com.example.medialab.Presenter.CurrentUserPresenter;
import com.example.medialab.R;

import java.util.ArrayList;
import java.util.List;

public class CurrentUserActivity extends BaseActivity implements CurrentUserContract.View {

    protected final int MEMBER_SEARCH_ACTIVITY_REQUEST_CODE = 1007;

    CurrentUserPresenter currentUserPresenter;
    ArrayList<StudentVO> studentList = null;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    DividerItemDecoration dividerItemDecoration=null;
    CurrentUserAdapter currentUserAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user);

        currentUserPresenter = new CurrentUserPresenter(this);
        init();
    }

    public void init() {

        setActionBar("현재 사용자");

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(0,113,185));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                currentUserAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        studentList = currentUserPresenter.studentListRequest();
        currentUserAdapter = new CurrentUserAdapter(studentList);

        currentUserAdapter.setOnItemClickListener(new CurrentUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                new AlertDialog.Builder(CurrentUserActivity.this)
                        .setMessage("해당 학생에게 경고를 등록하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){

                                StudentVO studentVO = currentUserAdapter.getStudentVO(pos);
                                studentVO.setWarningReason("퇴장시 QR 스캔하지 않음("+currentUserPresenter.getTodayDate()+")");
                                if(currentUserPresenter.setWarningRequest(studentVO)){
                                    studentList = currentUserPresenter.studentListRequest();
                                    currentUserAdapter.setStudentList(studentList);
                                    currentUserAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){
                            }
                        })
                        .show();
            }
        });

        recyclerView.setAdapter(currentUserAdapter);
    }

}
