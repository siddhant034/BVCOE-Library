package com.example.siddhant.bvcoelibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewAccount extends AppCompatActivity {
    private NewAccount.UserLoginTask mAuthTask = null;
    private EditText Name, Email, Roll, Password, RePassword;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        Name = (EditText)findViewById(R.id.name);
        Email = (EditText)findViewById(R.id.email);
        Roll = (EditText)findViewById(R.id.roll);
        Password = (EditText)findViewById(R.id.password);
        RePassword = (EditText)findViewById(R.id.re_password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void takeToSignIn(View v)
    {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }

    public void attemptLogin(View view) {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        Email.setError(null);
        Roll.setError(null);
        Password.setError(null);
        RePassword.setError(null);

        // Store values at the time of the login attempt.
        String name = Name.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String roll = Roll.getText().toString();
        String re_password = RePassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            Password.setError(getString(R.string.error_invalid_password));
            focusView = Password;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            Name.setError(getString(R.string.error_field_required));
            focusView = Name;
            cancel = true;
        }

        if (!TextUtils.isEmpty(re_password) && !isRePasswordValid(re_password,password)) {
            RePassword.setError(getString(R.string.error_invalid_password2));
            focusView = RePassword;
            cancel = true;
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            Email.setError(getString(R.string.error_field_required));
            focusView = Email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            Email.setError(getString(R.string.error_invalid_email));
            focusView = Email;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(name, roll, email, password, focusView);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //if(email.charAt(0)<'0' && email.charAt(0)>'9' )
        return true;
        //else
        //  return false;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    private boolean isRePasswordValid(String re_password, String password) {
        //TODO: Replace this with your own logic
        if (!password.equals(re_password))
            return false;
        return true;

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            TextView tv = (TextView)findViewById(R.id.textView5);
            tv.setVisibility(show ? View.GONE : View.VISIBLE);
            tv.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            Button b = (Button)findViewById(R.id.signIn);
            b.setVisibility(show ? View.GONE : View.VISIBLE);
            b.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            b = (Button)findViewById(R.id.button6);
            b.setVisibility(show ? View.GONE : View.VISIBLE);
            b.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

        }

    }
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        final String mRoll, mName, mPassword, mEmail;
        View fv;
        UserLoginTask(String name, String roll, String email, String password, View v) {
            mEmail = email;
            mPassword = password;
            mName = name;
            mRoll = roll;
            fv=v;
        }
        @Override
        protected Boolean doInBackground(Void... params)
        {
            boolean status=false;
            // Simulate network access.
            try
            {
            }
            catch (Exception e)
            {
                return false;
            }
            return status;
        }
        // TODO: register the new account here


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            //Toast.makeText(getApplicationContext(),"Holalua",Toast.LENGTH_LONG).show();

            if (success) {

            }
            else{
                //Email.setError(getString(R.string.error_invalid_email));
                //focusView = Email;
                //cancel = true;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }
}