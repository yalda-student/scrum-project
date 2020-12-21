package ir.scrumproject.retrofit;

import ir.scrumproject.data.model.User2;
import ir.scrumproject.retrofit.response.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author yalda mohasseli
 */
public interface ApiInterface {

    @GET("/user/:id")
    public void getUser(@Path("id") int userId);

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> loginUserWithEmail(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> loginUserWithUsername(@Field("username") String username, @Field("password") String password);

    @GET("user/me")
    Call<User2> getCurrentUser(@Header("Authorization") String token);
}
