package kr.ac.sunmoon.gijoe.camaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    EditText field_id;
    EditText field_pw;
    TextView signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeView();
        SetListener();
    }

    protected void initializeView() {
        loginBtn = findViewById(R.id.loginBtn);
        field_id = findViewById(R.id.field_id);
        field_pw = findViewById(R.id.field_pw);
        signupBtn = findViewById(R.id.btnSignUp);
    }

    protected void SetListener()
    {
        loginBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                login(field_id.getText().toString(),field_pw.getText().toString());
            }
        });
        signupBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    protected void login(String id, String pw) {
        // 앱 테스트를 위한 기본 비밀번호 하드코딩.
        // 서버 연동 시 제거될 예정
        //if (id.equals("test") && pw.equals("1234")) loginSuccessed();
    }

    protected void loginSuccessed() {
        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    protected void signup() {
        Intent intent = new Intent(LoginActivity.this, signupActivity.class);
        startActivity(intent);
    }

    public void request() {
        String url = getString(R.string.baseUrl) + "api/auth/signin";

        JSONObject loginJson = new JSONObject();
        try {
            loginJson.put("username", field_id.getText().toString());
            loginJson.put("password", field_pw.getText().toString());

            String jsonString = loginJson.toString();

            final RequestQueue requestQueue = Volley.newRequestQueue()
            final JsonObjectRequest
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
