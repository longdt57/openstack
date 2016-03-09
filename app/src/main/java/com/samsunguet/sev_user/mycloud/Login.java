package com.samsunguet.sev_user.mycloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.samsunguet.sev_user.mycloud.api.IdentifyAPI;
import com.samsunguet.sev_user.mycloud.object.Tenant;
import com.samsunguet.sev_user.mycloud.object.User;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by MeHuong on 3/5/2016.
 */
public class Login extends Activity {
    FancyButton btnSignIn;
    EditText edtUserName,edtPassword;

    public static User USER_DEFAULT = new User();

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnSignIn = (FancyButton)findViewById(R.id.btnSignIn);
        edtUserName = (EditText)findViewById(R.id.edtUserNameInLogIn);
        edtPassword = (EditText)findViewById(R.id.edtPasswordInLogIn);

        String username = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString(USERNAME, "");
        String password = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString(PASSWORD, "");

        edtUserName.setText(username);
        edtPassword.setText(password);

        //login();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        String username = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();

        if (username.compareTo("demo") == 0) {
            //getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isLogIn", true).commit();
            Intent main = new Intent(getBaseContext(), MainActivity.class);
            startActivity(main);
            finish();
        }else {
            if (username.compareTo("") == 0 || password.compareTo("") == 0) {
                Toast.makeText(Login.this, "username or password is empty", Toast.LENGTH_SHORT).show();
            } else {

                Tenant tenant = new Tenant("s2uet");
                User user = USER_DEFAULT;
                user.setName(username);
                user.setPassword(password);
                user.setTenant(tenant);

                IdentifyAPI requestLogin = new IdentifyAPI(MainActivity.URL_DEFAULT + ":5000/v2.0/tokens", user);

                new LoginTask(Login.this).execute(requestLogin);


            }
        }
    }

    private class LoginTask extends AsyncTask<IdentifyAPI, Void, Boolean>{

        Context mContext;
        public LoginTask(Context context){
            mContext = context;
        }
        @Override
        protected Boolean doInBackground(IdentifyAPI... params) {
            boolean isLogIn = params[0].setTokenandStorageurl();
            return isLogIn;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString(USERNAME, USER_DEFAULT.getUsername()).commit();
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString(PASSWORD, USER_DEFAULT.getPassword()).commit();
                Intent main = new Intent(mContext, MainActivity.class);
                startActivity(main);
                ((Login)mContext).finish();
            }else{
                Toast.makeText(mContext, "username or password is wrong",Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(aBoolean);
        }
    }
}
