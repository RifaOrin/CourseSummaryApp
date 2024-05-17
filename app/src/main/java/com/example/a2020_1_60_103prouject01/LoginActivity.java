package com.example.a2020_1_60_103prouject01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserId, etPW;
    private CheckBox cbRemUserId, cbRemPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserId = findViewById(R.id.etUserId);
        etPW = findViewById(R.id.etPW);

        cbRemUserId = findViewById(R.id.cbRemUserId);
        cbRemPass = findViewById(R.id.cbRemPass);

        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        boolean remUserIdChecked = sp.getBoolean("REM_USER", false);
        boolean remPassChecked = sp.getBoolean("REM_PASS", false);

        cbRemUserId.setChecked(remUserIdChecked);
        cbRemPass.setChecked(remPassChecked);

        if (remUserIdChecked) {
            String spUserId = sp.getString("USER_ID", "CREATED");
            etUserId.setText(spUserId);
        }
        if (remPassChecked) {
            String spPW = sp.getString("PASSWORD", "CREATED");
            etPW.setText(spPW);
        }

        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btnSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                finish();
            }
        });
        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLogin();
            }
        });
    }
    private void processLogin(){
        String userId = etUserId.getText().toString().trim();
        String userPW = etPW.getText().toString().trim();
        String errMsg = "";

        // write code to check validation data



        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        String userID = sp.getString("USER_ID", "NOT_CREATED");
        String password = sp.getString("PASSWORD", "NOT_CREATED");
        if(!userId.equals(userID)){
            errMsg += "Invalid User ID\n";
        }
        if(!userPW.equals(password)){
            errMsg += "Invalid Password\n";
        }
        if(errMsg.length() > 0){
            // show the error message here
            showErrorDialog(errMsg);
            return;
        }
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("REM_USER", cbRemUserId.isChecked());
        e.putBoolean("REM_PASS", cbRemPass.isChecked());
        e.commit();
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();

    }
    private void showErrorDialog(String errorMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage);
        builder.setTitle("Error");
        builder.setCancelable(true);
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}