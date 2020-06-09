package kr.ac.sunmoon.gijoe.camaps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class signupActivity extends AppCompatActivity {

    Button signupBtn;
    EditText signupID;
    EditText signupPW;
    EditText signupNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeView();
    }

    protected void initializeView() {
        signupBtn = findViewById(R.id.callSignupBtn);
        signupID = findViewById(R.id.field_signup_id);
        signupPW = findViewById(R.id.field_signup_password);
        signupNick = findViewById(R.id.field_signup_nickname);
    }

    protected void SetListener() {
        signupBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                signup(signupID.getText().toString(), signupPW.getText().toString(), signupNick.getText().toString());
            }
        });
    }

    private void signup(String username, String password, String nickname) {

    }
}
