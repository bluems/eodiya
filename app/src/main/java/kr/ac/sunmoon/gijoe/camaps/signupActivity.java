package kr.ac.sunmoon.gijoe.camaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class signupActivity extends AppCompatActivity {

    private Button signupBtn;
    private EditText signupID;
    private EditText signupPW;
    private EditText signupNick;
    private Spinner spinner;
    private HashMap<String, Integer> map = new HashMap<String, Integer>();
    private ArrayList<String> list = new ArrayList<>();

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
        spinner = findViewById(R.id.find_q_spinner);

        getQuestion();
        ArrayAdapter<String> spinnerAdapter;
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(spinnerAdapter);

    }

    protected void SetListener() {
        signupBtn.setOnClickListener(v -> signup(signupID.getText().toString(), signupPW.getText().toString(), signupNick.getText().toString()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Finder", "선택된 인덱스: " + map.get(spinner.getSelectedItem()).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void getQuestion() {
        String url = getString(R.string.baseUrl) + "api/finder/getList";

        JSONObject questionJson = new JSONObject();

        final RequestQueue requestQueue = Volley.newRequestQueue(signupActivity.this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,questionJson, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response.toString());
                String resultCode = jsonObject.getString("code"); // 서버로부터 결과 코드 받아옴
                JSONArray data =  jsonObject.getJSONArray("data");

                if(resultCode.equals("OK")){
                    Log.d("Finder","Success getting");
                    getListSuccessed(data); // 성공하면 이 함수 통해서 로그인 화면 이동
                }else{
                    Log.d("Finder","Failed");
                    Log.d("Finder",jsonObject.toString());
                    easyToast("질문 목록 조회 실패");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        //위에서 지정한 메시지 큐에 추가 및 송신
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    void getListSuccessed(JSONArray data) throws JSONException {
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.getJSONObject(i);
            Log.d("Finder", obj.toString());
            map.put(obj.getString("find_q"), obj.getInt("id"));
            list.add(obj.getString("find_q"));
        }
    }

    protected void signup(String id, String pw, String nick) {
        String url = getString(R.string.baseUrl) + "api/auth/signup";

        JSONObject signupJson = new JSONObject();
        try {
            signupJson.put("username", id);
            signupJson.put("password", pw);
            signupJson.put("nickname", nick);

            final RequestQueue requestQueue = Volley.newRequestQueue(signupActivity.this);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,signupJson, response -> {
                try {

                    JSONObject jsonObject = new JSONObject(response.toString());
                    String resultCode = jsonObject.getString("code"); // 서버로부터 결과 코드 받아옴

                    if(resultCode.equals("OK")){

                        Log.d("SignUp","Success");
                        signUpSuccessed(id ,pw); // 성공하면 이 함수 통해서 로그인 화면 이동
                    }else{
                        Log.d("SignUp","Failed");
                        Log.d("SignUp",jsonObject.toString());
                        easyToast("회원가입 실패");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace);

            //위에서 지정한 메시지 큐에 추가 및 송신
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void signUpSuccessed(String id, String pw) {
        Intent intent = new Intent();

        // 로그인 화면에 전달할 값 저장. 자동 로그인을 위함.
        SignupData signupData = new SignupData(id ,pw);

        // 값 전달
        intent.putExtra("data", signupData);
        setResult(0, intent);
        finish();
    }

    void easyToast(String str){
        Toast.makeText(getApplicationContext(),str, Toast.LENGTH_SHORT).show();
    }
}
