package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText edtUserName,edtPassword;
    Button btnGoToSignUp,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        edtUserName = findViewById(R.id.edtUserNameLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToSignUp = findViewById(R.id.btnGoToSignUp);
        btnLogin.setOnClickListener(Login.this);
        btnGoToSignUp.setOnClickListener(Login.this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btnLogin){
            ParseUser parseUser = new ParseUser();
            parseUser.logInInBackground(edtUserName.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null && e == null){
                        Toast.makeText(Login.this,"WelcomeBack "+edtUserName.getText().toString(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this,Contacts.class);
                        finish();
                        startActivity(intent);
                    }else {
                        Toast.makeText(Login.this,"Something Wrong",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
            if (v.getId()==R.id.btnGoToSignUp){
                Intent intent = new Intent(Login.this,SignUp.class);
                finish();
                startActivity(intent);
            }

    }
}