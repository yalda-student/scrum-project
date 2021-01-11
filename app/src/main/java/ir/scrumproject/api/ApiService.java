package ir.scrumproject.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @GET("group/{id}")
    Call<Group> getGroupSettings(
            @Header("Authorization") String token,
            @Path("id") int groupId
    );

    @DELETE("group/{id}")
    Call<Void> deleteGroup(
            @Header("Authorization") String token,
            @Path("id") int groupId
    );

    @PATCH("group/{id}")
    @Multipart
    Call<Group> updateGroup(
            @Header("Authorization") String token,
            @Path("id") int groupId,
            @Part("name") RequestBody name,
            @Part("bio") RequestBody bio,
            @Part MultipartBody.Part avatar
    );

    @POST("expense")
    Call<Invoice> createInvoice(
            @Body Invoice invoice
    );
}
