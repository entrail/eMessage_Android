package edu.csulb.android.emessage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sun.mail.imap.IMAPStore;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;

import edu.csulb.android.emessage.mail.authentication.Authenticator;
import edu.csulb.android.emessage.mail.authentication.Credentials;
import edu.csulb.android.emessage.mail.provider.GmailProvider;
import edu.csulb.android.emessage.mail.provider.IMailProvider;
import edu.csulb.android.emessage.models.Chat;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static Context contextOfApplication; // used for shared preferences in credentials class

    private EditText editText_email;
    private EditText editText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        contextOfApplication = getApplicationContext();

        setupToolbar();

        editText_email = (EditText) findViewById(R.id.editText_login_email);
        editText_password = (EditText) findViewById(R.id.editText_login_password);

        fillInCredentials();
        if (Credentials.areSet()) {
            startNextActivity();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login_toolbar);
        toolbar.setTitle("Sign In");
        setSupportActionBar(toolbar);
    }

    public void button_login_onClick(View view) {

        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();
        if (Authenticator.canAuthenticate(email, password)) {
            setCredentials();
            startNextActivity();
        } else {
            Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setCredentials() {
        Credentials.setEmailAddress(editText_email.getText().toString());
        Credentials.setPassword(editText_password.getText().toString());
    }

    private void fillInCredentials() {
        editText_email.setText(Credentials.getEmailAddress());
    }

    private void startNextActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
}
