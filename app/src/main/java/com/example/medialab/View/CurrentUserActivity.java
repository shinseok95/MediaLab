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

/** 현재 랩실을 사용하고 있는 학생 리스트를 보여주는 Activity입니다.
 *
 *  Presenter에게 당일 사용자 중 퇴장 시간을 입력하지 않은 학생들의 리스트를 요청합니다.
 *  퇴장시 스캔하지 않은 학생을 확인한다면, 해당 학생의 view를 클릭하여 경고를 줄 수 있습니다.
 */

public class CurrentUserActivity extends BaseActivity implements CurrentUserContract.View {

    private CurrentUserPresenter currentUserPresenter = null;
    private ArrayList<StudentVO> studentList = null;
    private RecyclerView recyclerView = null;
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private DividerItemDecoration dividerItemDecoration=null;
    private CurrentUserAdapter currentUserAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user);

        currentUserPresenter = new CurrentUserPresenter(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        studentList= null;
        recyclerView = null;
        swipeRefreshLayout=null;
        dividerItemDecoration=null;
        currentUserAdapter=null;
        currentUserPresenter.releaseView();
        currentUserPresenter = null;
    }

    public void init() {

        setActionBar("현재 사용자");

        // Recyclerview refresh 설정
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(0,113,185));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                currentUserAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Recyclerview 설정
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Recyclerview 밑줄 설정
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        studentList = currentUserPresenter.studentListRequest();
        currentUserAdapter = new CurrentUserAdapter(studentList);

        // 클릭시 경고 등록 메소드
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
