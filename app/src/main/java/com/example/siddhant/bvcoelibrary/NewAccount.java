package com.example.siddhant.bvcoelibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NewAccount extends AppCompatActivity {
    private NewAccount.UserLoginTask mAuthTask = null;
    private EditText Name, Email, Roll, Password, RePassword;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Name = (EditText)findViewById(R.id.name);
        Email = (EditText)findViewById(R.id.email);
        Roll = (EditText)findViewById(R.id.roll);
        Password = (EditText)findViewById(R.id.password);
        RePassword = (EditText)findViewById(R.id.re_password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

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
        View fv;
        JSONObject postData = new JSONObject();
        Boolean status=false;
        InputStream is = null;
        String result="";
        String url = "http://shrouded-island-99834.herokuapp.com/api/student/register";

        UserLoginTask(String name, String roll, String email, String password, View v) {
            String branch;
            if(roll.charAt(7)=='2' && roll.charAt(8)=='7')
                branch="CSE";
            else if(roll.charAt(7)=='2' && roll.charAt(8)=='8')
                branch="ECE";
            else if(roll.charAt(7)=='3' && roll.charAt(8)=='1')
                branch="IT";
            else if(roll.charAt(7)=='4' && roll.charAt(8)=='9')
                branch="EEE";
            else
                branch="ICE";

            int year;
            year=roll.charAt(roll.length()-2)-'0';
            year*=10;
            year+=roll.charAt(roll.length()-1)-'0';

            try
            {
                postData.put("studentEmail", email);
                postData.put("studentRollnumber", roll);
                postData.put("password", password);
                postData.put("studentName", name);
                postData.put("branch", branch);
                postData.put("semester", (16 - year) * 2);
                postData.put("year", year);
            }
            catch(Exception e)
            {
            }
        }
        @Override
        protected Boolean doInBackground(Void... params)
        {
            boolean status=false;
            // Simulate network access.
            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                json = postData.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                is = httpResponse.getEntity().getContent();
                if(is != null) {
                    result = convertInputStreamToString(is);
                    JSONObject jObj = new JSONObject(result);
                    status = jObj.getBoolean("status");
                    result=jObj.getString("result");
                }
                Thread.sleep(3000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return status;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException
        {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line;
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;

        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            //Toast.makeText(getApplicationContext(),"Holalua",Toast.LENGTH_LONG).show();

            if (success) {
                Toast.makeText(getApplicationContext(),"Account created successfully",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
            else{
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }
}