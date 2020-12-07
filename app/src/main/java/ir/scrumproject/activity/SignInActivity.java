package ir.scrumproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.scrumproject.R;
import ir.scrumproject.data.AppDatabase;
import ir.scrumproject.data.model.User;

public class SignInActivity extends AppCompatActivity {

    private TextView errorTextView;
    private TextInputEditText emailEditText, passEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private boolean isValidEmail(String value) {
        return (!TextUtils.isEmpty(value) && Patterns.EMAIL_ADDRESS.matcher(value).matches());
    }

    OnClickListener signInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            errorTextView.setVisibility(View.INVISIBLE);
            String emailText = emailEditText.getText().toString();
            String password = passEditText.getText().toString();

            if (emailText.equals(""))
                emailEditText.setError("این فیلد را پر کنید.");
            else if (password.equals(""))
                passEditText.setError("این فیلد را پر کنید.");
            else if (password.length()<8)
                passEditText.setError("رمز عبور شما باید شامل 8 کارکتر باشد");
            else {

                AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {

                    User user;
                    if (isValidEmail(emailText))
                        user = appDatabase.userDao().findUserByEmail(emailText, password);
                    else
                        user = appDatabase.userDao().findUserByUsername(emailText, password);

                    if (user != null) {
                        //go to next activity
                        runOnUiThread(() -> {
                            Toast.makeText(SignInActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        });
                    } else {
                        runOnUiThread(() -> {

                            errorTextView.setVisibility(View.VISIBLE);
                            emailEditText.setText("");
                            passEditText.setText("");
                        });
                    }
                });
            }

        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}