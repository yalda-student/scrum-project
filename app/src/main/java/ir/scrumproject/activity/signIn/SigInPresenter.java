package ir.scrumproject.activity.signIn;

import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ir.scrumproject.data.model.User2;
import ir.scrumproject.retrofit.ApiClient;
import ir.scrumproject.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  12/17/2020.
 */
public class SigInPresenter {

    private SignInActivity context;

    public SigInPresenter(SignInActivity context) {
        this.context = context;
    }

    public User2 getCurrentUser(String token) {

        final User2[] user = new User2[1];

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<User2> responseCall = apiInterface.getCurrentUser(token);
        responseCall.enqueue(new Callback<User2>() {
            @Override
            public void onResponse(Call<User2> call, Response<User2> response) {

                if (response.isSuccessful()) {
                    user[0] = response.body();
                    Log.d("TAG", "onResponse: " + user[0].toString());
                } else {
                    Log.d("TAG", "onResponse: " + response.message());
                    user[0] = null;
                }
            }

            @Override
            public void onFailure(Call<User2> call, Throwable t) {

                Log.d("TAG", "onFailure: " + t) ;
                context.runOnUiThread(() -> Toast.makeText(context, "اتصال اینترنت خود را چک کنید.", Toast.LENGTH_SHORT).show());
            }
        });

        if (user[0] == null)
            Log.d("TAG", "getCurrentUser: null");
        else
            Log.d("TAG", "getCurrentUser: not-null");


        return user[0];
    }
}
