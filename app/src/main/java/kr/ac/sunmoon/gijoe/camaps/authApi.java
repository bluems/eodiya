package kr.ac.sunmoon.gijoe.camaps;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface authApi {
    @FormUrlEncoded
    @POST("/api/auth/signup")
    Call<SignupAPI> signUp(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nickname") String nickname
    );
}
