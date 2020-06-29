package kr.ac.sunmoon.gijoe.camaps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    EditText field_id;
    EditText field_pw;
    TextView signupBtn;

    private int signupResult = 1;
    private SignupData signupData;

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
        loginBtn.setOnClickListener(v -> login(field_id.getText().toString(),field_pw.getText().toString()));
        signupBtn.setOnClickListener(v -> signup());
    }

    protected void login(String id, String pw) {
        // 앱 테스트를 위한 기본 비밀번호 하드코딩.
        // 서버 연동 시 제거될 예정
        //if (id.equals("test") && pw.equals("1234")) loginSuccessed();
        String url = getString(R.string.baseUrl) + "api/auth/signin";

        JSONObject loginJson = new JSONObject();
        try {
            loginJson.put("username", id);
            loginJson.put("password", pw);

            final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
//서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,loginJson, response -> {
                try {

                    //받은 json형식의 응답을 받아
                    JSONObject jsonObject = new JSONObject(response.toString());

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    String resultId = jsonObject.getString("username");
                    String resultToken = jsonObject.getString("accessToken");
                    String resultNick = jsonObject.getString("nickname");

                    //만약 그 값이 같다면 로그인에 성공한 것입니다.
                    if(loginJson.getString("username").equals(resultId) & resultToken.length() != 0){

                        Log.d("Login","Success");
                        loginSuccessed(resultId, resultToken, resultNick);
                    }else{
                        Log.d("Login","Failed");
                        Log.d("Login",jsonObject.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void loginSuccessed(String id, String token, String nick) {
        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        LoginData data = new LoginData(id, token, nick);
        intent.putExtra("userData", data);
        setResult(RESULT_OK, intent);
        //startActivity(intent);
        finish();
    }

    protected void signup() {
        Intent intent = new Intent(LoginActivity.this, signupActivity.class);
        startActivityForResult(intent,signupResult);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0) {
            easyToast("회원가입 성공. 로그인 절차 실행.");

            if (Objects.requireNonNull(data).hasExtra("data")) {
                signupData = (SignupData) Objects.requireNonNull(data.getExtras()).get("data");

                if (signupData != null) {
                    field_id.setText(signupData.getId());
                }
                if (signupData != null) {
                    field_pw.setText(signupData.getPw());
                }

                if (signupData != null) {
                    login(signupData.getId(), signupData.getPw());
                }
            }
        }
    }

    void easyToast(String str){
        Toast.makeText(getApplicationContext(),str, Toast.LENGTH_SHORT).show();
    }
}
