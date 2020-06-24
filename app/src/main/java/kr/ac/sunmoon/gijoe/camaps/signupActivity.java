package kr.ac.sunmoon.gijoe.camaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseUrl))
                .build();
        postApi = retrofit.create(PostApi.class);*/
    }

    protected void initializeView() {
        signupBtn = findViewById(R.id.callSignupBtn);
        signupID = findViewById(R.id.field_signup_id);
        signupPW = findViewById(R.id.field_signup_password);
        signupNick = findViewById(R.id.field_signup_nickname);
    }

    protected void SetListener() {
        signupBtn.setOnClickListener(v -> signup("1", "2", "3"));
    }

    private void signup(String username, String password, String nickname) {
        /*Post post = new Post(username, password, nickname);

        Call<Post> call = postApi.createPost(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    Log.d("API","code: "+response.code());
                    return;
                }

                Post postResponse = response.body();
                Log.d("API","body: " + postResponse.getText());


            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d("API", "Error: " + t.getMessage());
            }
        });*/


    }
}
