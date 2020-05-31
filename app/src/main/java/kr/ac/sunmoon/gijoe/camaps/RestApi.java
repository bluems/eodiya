package kr.ac.sunmoon.gijoe.camaps;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestApi {
    @FormUrlEncoded
    @POST("/api/auth/signin")
    Call<Map<String, String>> getToken(@Field(""))
}
