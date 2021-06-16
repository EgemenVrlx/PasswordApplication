package com.example.passwordapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class SetLoginPasswordActivity extends AppCompatActivity {

    public final static String LOGIN_PASSWORD = "login_password";

    private String password;

    private EditText editTextSetPass1;
    private EditText editTextSetPass2;
    private Button buttonSetOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_login_password);

        init();

        buttonSetOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextSetPass1.getText().toString().equals(editTextSetPass2.getText().toString())){
                    Toast.makeText(SetLoginPasswordActivity.this, getString(R.string.error_match), Toast.LENGTH_LONG).show();
                }
                else if(editTextSetPass1.getText().toString().isEmpty() || editTextSetPass2.getText().toString().isEmpty()){
                    Toast.makeText(SetLoginPasswordActivity.this, getString(R.string.cannot_be_empty), Toast.LENGTH_LONG).show();
                }
                else{
                    SetLoginPasswordActivity.this.password = editTextSetPass1.getText().toString();
                    savePassword();
                    Toast.makeText(SetLoginPasswordActivity.this, getString(R.string.password_has_been_set), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SetLoginPasswordActivity.this, LoginPasswordActivity.class));
                    finish();
                }
            }
        });
    }

    private void savePassword(){
        File file = new File(SetLoginPasswordActivity.this.getFilesDir(), LOGIN_PASSWORD);
        FileOutputStream outputStream;

        try{
            outputStream = openFileOutput(LOGIN_PASSWORD, Context.MODE_PRIVATE);
            outputStream.write(SetLoginPasswordActivity.this.password.getBytes());
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init() {
        editTextSetPass1 = findViewById(R.id.editTextSetPass1);
        editTextSetPass2 = findViewById(R.id.editTextSetPass2);
        buttonSetOk = findViewById(R.id.buttonSetOk);
    }
}
