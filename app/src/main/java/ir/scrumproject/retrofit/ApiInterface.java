package ir.scrumproject.retrofit;

import java.util.List;

import ir.scrumproject.api.Group;
import ir.scrumproject.data.model.User2;
import ir.scrumproject.retrofit.response.LoginResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @POST("user/logout")
    Call<Void> logout(@Header("Authorization") String token);

    @POST("group")
    @Multipart
    Call<Group> createGroup(
            @Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("bio") RequestBody bio,
            @Part("max") RequestBody maxSize,
            @Part("welcomeMessage") RequestBody welcomeMessage,
            @Part MultipartBody.Part avatar
    );

    @GET("group/mine")
    Call<List<Group>> getUserGroups(@Header("Authorization") String token);

    @GET("group/{id}")
    Call<Group> getGroupById(@Path("id") int groupId);

    @POST("/group/{id}/join/{userId}")
    Call<Void> addMemberToGroup(
            @Header("Authorization") String token,
            @Path("id") int groupId,
            @Path("email") String userEmail
    );
}
