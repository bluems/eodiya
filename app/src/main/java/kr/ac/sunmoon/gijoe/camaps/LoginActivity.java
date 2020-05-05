package kr.ac.sunmoon.gijoe.camaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    EditText field_id;
    EditText field_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initalizeView();
        SetListener();
    }

    protected void initalizeView() {
        loginBtn = findViewById(R.id.loginBtn);
        field_id = findViewById(R.id.field_id);
        field_pw = findViewById(R.id.field_pw);
    }

    protected void SetListener()
    {
        loginBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                login(field_id.getText().toString(),field_pw.getText().toString());
            }
        });
    }

    protected void login(String id, String pw) {
        // 앱 테스트를 위한 기본 비밀번호 하드코딩.
        // 서버 연동 시 제거될 예정
        if (id.equals("test") && pw.equals("1234")) loginSuccessed();
    }

    protected void loginSuccessed() {
        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(intent);
    }
}
