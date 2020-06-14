package kr.ac.sunmoon.gijoe.camaps;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PostApi {
    @POST("api/test/all")
    Call<Post> createPost(@Body Post post);
}
