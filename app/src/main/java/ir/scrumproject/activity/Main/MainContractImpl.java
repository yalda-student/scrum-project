package ir.scrumproject.activity.Main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import ir.scrumproject.activity.signIn.SignInActivity;
import ir.scrumproject.api.Group;
import ir.scrumproject.retrofit.ApiClient;
import ir.scrumproject.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  12/13/2020.
 */
public class MainContractImpl implements MainContract {

    private Activity activity;

    public MainContractImpl(Activity activity) {
        this.activity = activity;
    }


    @Override
    public void exitApp(String token) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Void> responseCall = apiInterface.logout(token);

        responseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    saveUserState("tokenDef");
                    activity.runOnUiThread(() -> {
                        Intent intent = new Intent(activity, SignInActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                        Toast.makeText(activity, "بازم به ما سر بزنید :)", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Log.d("TAG", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                activity.runOnUiThread(() -> Toast.makeText(activity, "اتصال اینترنت خود را چک کنید.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void saveUserState(String token) {

        Log.d("TAG", "saveUserState: " + token);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("loginPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }
}
