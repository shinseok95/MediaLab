package com.example.medialab.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.medialab.R;
import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

public class DeveloperInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);

        init();
    }

    public void init(){

        setActionBar("개발자 정보");

        AboutView view = AboutBuilder.with(this)
                .setPhoto(R.mipmap.profile_photo)
                .setCover(Color.rgb(155,207,223))
                .setName("신현석")
                .setSubTitle("광운대학교 소프트웨어학부")
                .setBrief("문의하실 부분은 "+"\nInstagram DM을 통해 연락주세요.")
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .addInstagramLink("shin__seok")
                .addGitHubLink("shinseok95")
                .setVersionNameAsAppSubTitle()
                .setWrapScrollView(false)
                .setLinksAnimated(true)
                .setShowAsCard(true)
                .build();

        LayoutInflater inflater = getLayoutInflater();

        addContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

    }

}
