package com.samsunguet.sev_user.mycloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by MeHuong on 3/5/2016.
 */
public class Login extends Activity {
    FancyButton btnSignIn;
    EditText edtUserName,edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnSignIn = (FancyButton)findViewById(R.id.btnSignIn);
        edtUserName = (EditText)findViewById(R.id.edtUserNameInLogIn);
        edtPassword = (EditText)findViewById(R.id.edtPasswordInLogIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("isLogIn",true).commit();
                Intent main = new Intent(getBaseContext(),MainActivity.class);
                startActivity(main);
                finish();
            }
        });
    }
}
