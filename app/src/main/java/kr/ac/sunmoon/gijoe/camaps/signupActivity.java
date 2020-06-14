package kr.ac.sunmoon.gijoe.camaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class signupActivity extends AppCompatActivity {

    private Button signupBtn;
    private EditText signupID;
    private EditText signupPW;
    private EditText signupNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeView();
        SetListener();
    }

    protected void initializeView() {
        signupBtn = findViewById(R.id.callSignupBtn);
        signupID = findViewById(R.id.field_signup_id);
        signupPW = findViewById(R.id.field_signup_password);
        signupNick = findViewById(R.id.field_signup_nickname);
    }

    protected void SetListener() {
        signupBtn.setOnClickListener(v -> signup(signupID.getText().toString(), signupPW.getText().toString(), signupNick.getText().toString()));
    }

    private void signup(String username, String password, String nickname) {

    }
}
