package ir.scrumproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

        signInButton.setOnClickListener(signInListener);
        forgetButton.setOnClickListener(forgetListener);
    }

    View.OnClickListener signInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            errorTextView.setVisibility(View.INVISIBLE);
            String emailText = emailEditText.getText().toString();
            String password = passEditText.getText().toString();

            if (emailText.equals(""))
                emailEditText.setError("Fill this box");
            else if (password.equals(""))
                passEditText.setError("Fill this box");
            else {

                AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {

                    User user = appDatabase.userDao().getUser(emailText, password);
                    if (user != null) {
                        //go to next activity
                        Toast.makeText(SignInActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
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



    OnClickListener forgetListener = v -> {

        startActivity(new Intent(SignInActivity.this, ForgetPassActivity.class));
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}