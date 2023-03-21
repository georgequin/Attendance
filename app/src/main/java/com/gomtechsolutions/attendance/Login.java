package com.gomtechsolutions.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Login extends Activity {
    Button login_btn;
    TextInputEditText email_edit_text,password_edit_text;
    EditText username, password;
    TextView warning_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpUi();
        // Login1();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if (user.equals("balami") && pass.equals("1234")){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    //Toast.makeText(this, "incorrect username or password", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Login.this, "incorrect username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void login(String user_id,String password) {
        // perform login operation here
    }
    private void setUpUi() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.loginbtn);

    }

    public void Login1() {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        if (user.equals("balami") && pass.equals("1234")){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, "incorrect username or password", Toast.LENGTH_SHORT).show();
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}