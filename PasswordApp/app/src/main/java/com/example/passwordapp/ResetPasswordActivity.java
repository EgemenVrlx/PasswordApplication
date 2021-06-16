package com.example.passwordapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ResetPasswordActivity extends AppCompatActivity {

    public final static String LOGIN_PASSWORD = "login_password";

    private String password;

    private EditText editTextSetPassReset;
    private EditText editTextSetPass1;
    private EditText editTextSetPass2;
    private Button buttonSetOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        init();

        getPassword();

        buttonSetOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextSetPassReset.getText().toString().equals(ResetPasswordActivity.this.password)){
                    if(!editTextSetPass1.getText().toString().equals(editTextSetPass2.getText().toString())){
                        Toast.makeText(ResetPasswordActivity.this, getString(R.string.error_match), Toast.LENGTH_LONG).show();
                    }
                    else if(editTextSetPass1.getText().toString().isEmpty() || editTextSetPass2.getText().toString().isEmpty()){
                        Toast.makeText(ResetPasswordActivity.this, getString(R.string.cannot_be_empty), Toast.LENGTH_LONG).show();
                    }
                    else{
                        ResetPasswordActivity.this.password = editTextSetPass1.getText().toString();
                        savePassword();
                        Toast.makeText(ResetPasswordActivity.this, getString(R.string.password_has_been_set), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ResetPasswordActivity.this, LoginPasswordActivity.class));
                        finish();
                    }
                }
                else{
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.incorrect_password), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void savePassword(){
        File file = new File(ResetPasswordActivity.this.getFilesDir(), LOGIN_PASSWORD);
        FileOutputStream outputStream;

        try{
            outputStream = openFileOutput(LOGIN_PASSWORD, Context.MODE_PRIVATE);
            outputStream.write(ResetPasswordActivity.this.password.getBytes());
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getPassword() {
        File file = new File(ResetPasswordActivity.this.getFilesDir(), LOGIN_PASSWORD);
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

    private void init() {
        editTextSetPassReset = findViewById(R.id.editTextSetPassReset);
        editTextSetPass1 = findViewById(R.id.editTextSetPass1);
        editTextSetPass2 = findViewById(R.id.editTextSetPass2);
        buttonSetOk = findViewById(R.id.buttonSetOk);
    }
}
