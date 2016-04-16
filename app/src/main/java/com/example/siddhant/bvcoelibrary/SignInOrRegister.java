package com.example.siddhant.bvcoelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SignInOrRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_register);
    }

    public void signIn(View view)
    {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }

    public void createAccount(View view)
    {
        Intent i = new Intent(this,NewAccount.class);
        startActivity(i);
    }

    public void skipSignIn(View view)
    {
        Intent i = new Intent(this,FrontPage.class);
        startActivity(i);
    }
}