package com.samsunguet.sev_user.mycloud;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import com.melnykov.fab.FloatingActionButton;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MeHuong on 3/7/2016.
 */
public class SettingActivity  extends Activity{

    FloatingActionButton fab;
    ImageView imgAvatar,imgCover;
    TextView tvUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        //REGISTERY
        fab = (FloatingActionButton) findViewById(R.id.floatButton);
        imgAvatar = (ImageView)findViewById(R.id.imgAvatarInSetting);
        imgCover = (ImageView)findViewById(R.id.imgCoverInSetting);
        tvUserName = (TextView)findViewById(R.id.tvUserNameInSetting);
        // ACTION EDIT PROFILE

    }
}
