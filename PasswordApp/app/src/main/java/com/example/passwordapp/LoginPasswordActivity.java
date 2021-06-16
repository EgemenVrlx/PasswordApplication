package com.example.passwordapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import java.io.File;
import java.io.FileInputStream;

public class LoginPasswordActivity extends AppCompatActivity {

    public final static String LOGIN_PASSWORD = "login_password";

    private String password;

    private EditText editTextLoginPass;
    private Button buttonOk;
    private Button buttonChangePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);

        init();

        getPassword();

        if(this.password == null || this.password.isEmpty()){
            startActivity(new Intent(this, SetLoginPasswordActivity.class));
            finish();
        }

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempPass = editTextLoginPass.getText().toString();
                if(isPasswordCorrect(tempPass)){
                    startActivity(new Intent(LoginPasswordActivity.this, MainActivity.class));
                    finish();
                }
                else
                    Toast.makeText(LoginPasswordActivity.this, getString(R.string.incorrect_password), Toast.LENGTH_LONG).show();
            }
        });

        buttonChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPasswordActivity.this, ResetPasswordActivity.class));
                finish();
            }
        });
    }

    private void getPassword() {
        File file = new File(LoginPasswordActivity.this.getFilesDir(), LOGIN_PASSWORD);
        FileInputStream inputStream;

        try{
            inputStream = openFileInput(LOGIN_PASSWORD);
            int c;
            StringBuilder temp = new StringBuilder();

            while((c = inputStream.read()) != -1){
                temp.append((char) c);
            }

            inputStream.close();

            this.password = temp.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isPasswordCorrect(String password){
        return password.equals(this.password);
    }

    private void init() {
        editTextLoginPass = findViewById(R.id.editTextLoginPass);
        buttonOk = findViewById(R.id.buttonOk);
        buttonChangePass = findViewById(R.id.buttonChangePass);
    }
}
