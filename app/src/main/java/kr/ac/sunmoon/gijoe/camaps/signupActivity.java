package kr.ac.sunmoon.gijoe.camaps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class signupActivity extends AppCompatActivity {
    private Retrofit mRetrofit;
    private authApi mAuthAPI;
    private Call<SignupAPI> mCallSignUp;

    Button signupBtn;
    EditText signupID;
    EditText signupPW;
    EditText signupNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setRetrofitInit();
    }

    protected void initializeView() {
        signupBtn = findViewById(R.id.callSignupBtn);
    }

    private void signup(String username, String password, String nickname) {
        mCallSignUp = mAuthAPI.signUp(username,password,nickname);
        mCallSignUp.enqueue(mRetrofitCallback);
    }

    private void setRetrofitInit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mAuthAPI = mRetrofit.create(authApi.class);
    }

    private Callback<SignupAPI> mRetrofitCallback = new Callback<SignupAPI>() {
        @Override
        public void onResponse(Call<SignupAPI> call, Response<SignupAPI> response) {
            Log.v("Test", response.body().toString());
        }

        @Override
        public void onFailure(Call<SignupAPI> call, Throwable t) {
            t.printStackTrace();
        }
    };
}
