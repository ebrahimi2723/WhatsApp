package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmail,edtUserName,edtPassword;
    Button btnSignUp,btnGoToLogin;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        imageView = findViewById(R.id.imgSignUp);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        edtEmail = findViewById(R.id.edtEmailSignUp);
        edtUserName = findViewById(R.id.edtUserNameLogin);
        edtPassword = findViewById(R.id.edtPasswordSignUp);
        btnSignUp.setOnClickListener(this);
        btnGoToLogin.setOnClickListener(this);
        imageView.setOnClickListener(this);
        if (ParseUser.getCurrentUser()!=null){
            Intent intent = new Intent(SignUp.this,Contacts.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGoToLogin){
            Intent intent = new Intent(SignUp.this,Login.class);
            finish();
            startActivity(intent);
        }
        if (v.getId() == R.id.btnSignUp){
            ParseUser parseUser = new ParseUser();
            parseUser.setEmail(edtEmail.getText().toString());
            parseUser.setUsername(edtUserName.getText().toString());
            parseUser.setPassword(edtPassword.getText().toString());
            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null){
                        Toast.makeText(SignUp.this,"Welcome "+edtUserName.getText().toString(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignUp.this, Contacts.class);
                        finish();
                        startActivity(intent);
                    }else {
                        Toast.makeText(SignUp.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}