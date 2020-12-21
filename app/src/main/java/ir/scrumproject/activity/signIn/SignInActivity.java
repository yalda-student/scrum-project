package ir.scrumproject.activity.signIn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ir.scrumproject.R;
import ir.scrumproject.activity.ForgetPassActivity;
import ir.scrumproject.activity.Main.MainActivity;
import ir.scrumproject.activity.SignUpActivity;
import ir.scrumproject.data.model.User2;
import ir.scrumproject.retrofit.ApiClient;
import ir.scrumproject.retrofit.ApiInterface;
import ir.scrumproject.retrofit.response.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private TextView errorTextView;
    private TextInputEditText emailEditText, passEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUserState();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            setContentView(R.layout.activity_sign_in);

            emailEditText = findViewById(R.id.email_signIn);
            passEditText = findViewById(R.id.pass_signIn);
            errorTextView = findViewById(R.id.wrong_info_txt);
            Button signInButton = findViewById(R.id.buttonSignIn);
            Button forgetButton = findViewById(R.id.forgetBtn);
            Button signUpButton = findViewById(R.id.create_acc_btn);

            signInButton.setOnClickListener(signInListener);
            forgetButton.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this, ForgetPassActivity.class)));
            signUpButton.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));

        }, 700);

    }

    private boolean isValidEmail(String value) {
        return (!TextUtils.isEmpty(value) && Patterns.EMAIL_ADDRESS.matcher(value).matches());
    }

    private void saveUserState(String token) {

        Log.d("TAG", "saveUserState: " + token);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loginPref", MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    private void getUserState() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loginPref", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "tokenDef");

        Log.d("TAG", "getUserState: " + token);

        if (!token.equals("tokenDef")) getCurrentUser(token);
    }

    public void getCurrentUser(String token) {

        final User2[] user = new User2[1];

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<User2> responseCall = apiInterface.getCurrentUser(token);

        responseCall.enqueue(new Callback<User2>() {
            @Override
            public void onResponse(Call<User2> call, Response<User2> response) {

                if (response.isSuccessful()) {
                    user[0] = response.body();
                    Log.d("TAG", "onResponse: " + user[0].toString());

                    runOnUiThread(() -> {
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    Log.d("TAG", "onResponse: " + response.message());
                    user[0] = null;
                }
            }

            @Override
            public void onFailure(Call<User2> call, Throwable t) {
                runOnUiThread(() -> Toast.makeText(SignInActivity.this, "اتصال اینترنت خود را چک کنید.", Toast.LENGTH_SHORT).show());
            }
        });


    }

    private final OnClickListener signInListener = v -> {

        errorTextView.setVisibility(View.INVISIBLE);
        String emailText = emailEditText.getText().toString().trim();
        String password = passEditText.getText().toString().trim();

        if (emailText.equals(""))
            emailEditText.setError("این فیلد را پر کنید.");
        else if (password.equals(""))
            passEditText.setError("این فیلد را پر کنید.");
        else if (password.length() < 8)
            passEditText.setError("رمز عبور شما باید شامل 8 کارکتر باشد");
        else {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<LoginResponse> responseCall;
                if (isValidEmail(emailText))
                    responseCall = apiInterface.loginUserWithEmail(emailText, password);
                else
                    responseCall = apiInterface.loginUserWithUsername(emailText, password);

                responseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        Log.d("TAG", "onResponse: " + response.message());
                        if (response.isSuccessful()) {

                            LoginResponse loginResponse = response.body();
                            String token = loginResponse.getToken();
                            saveUserState(token);

                            runOnUiThread(() -> {
                                Toast.makeText(SignInActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            });

                        } else {
                            Log.d("TAG", "onResponse: " + response.toString());
                            runOnUiThread(() -> {
                                errorTextView.setVisibility(View.VISIBLE);
                                emailEditText.setText("");
                                passEditText.setText("");
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                        runOnUiThread(() -> Toast.makeText(SignInActivity.this, "اتصال اینترنت خود را چک کنید.", Toast.LENGTH_SHORT).show());
                    }
                });
            });

        }
    };

}