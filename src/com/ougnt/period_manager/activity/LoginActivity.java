package com.ougnt.period_manager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ougnt.period_manager.R;
import com.ougnt.period_manager.handler.LoginHandler;


/**
 * Created by wacharint on 7/17/2016 AD.
 */
public class LoginActivity extends Activity {

    public static final String LoginActivityResultExtra = "LoginActivityResultExtra";
    public static final String LoginActivityResultCancel = "Cancel";
    public static final String LoginActiviryResultLoginSuccess = "LoginSuccess";
    public static final String LoginActiviryResultLoginFail = "LoginFail";

    private LoginHandler loginHandler = new LoginHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);

        findViewById(R.id.login_forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });
    }

    public void onButtonClick(View v) {

        switch (v.getId()) {
            case R.id.login_cancel_button: {
                closeActivity();
                break;
            }
            case R.id.login_login_button: {

                String email = ((EditText)findViewById(R.id.login_email_edittext)).getText().toString();
                String password = ((EditText) findViewById(R.id.login_password_edittext)).getText().toString();
                loginHandler.login(this, email, password);
                break;
            }
            case R.id.login_forget_password: {
                findViewById(R.id.login_forget_password_panel).setVisibility(View.VISIBLE);
                findViewById(R.id.login_forget_password).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    private void closeActivity() {
        Intent retIntent = new Intent();
        retIntent.putExtra(LoginActivityResultExtra, LoginActivityResultCancel);
        setResult(RESULT_OK, retIntent);
        finish();
    }
}
