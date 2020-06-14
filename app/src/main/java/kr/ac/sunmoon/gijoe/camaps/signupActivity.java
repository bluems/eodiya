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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class signupActivity extends AppCompatActivity {

    private Button signupBtn;
    private EditText signupID;
    private EditText signupPW;
    private EditText signupNick;

    private PostApi postApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeView();
        SetListener();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseUrl))
                .build();
        postApi = retrofit.create(PostApi.class);
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
        Post post = new Post(username, password, nickname);

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
        });
    }
}
