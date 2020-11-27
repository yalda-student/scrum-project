package ir.scrumproject.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.scrumproject.R;
import ir.scrumproject.data.AppDatabase;
import ir.scrumproject.data.dao.UserDao;

public class ForgetPassCompleteActivity extends AppCompatActivity {
    Button btnChangePassword;
    EditText edtNewPass;
    TextView txtEmail;
    AppDatabase db;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_complete);

        btnChangePassword = findViewById(R.id.btnChangePassword);
        edtNewPass = findViewById(R.id.edtNewPass);
        txtEmail = findViewById(R.id.txtEmail);
        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        handleIntent(getIntent());

        btnChangePassword.setOnClickListener(v -> {
            String email = txtEmail.getText().toString();
            String newPass = edtNewPass.getText().toString().trim();
            CountDownLatch latch = new CountDownLatch(1);
            Executors.newSingleThreadExecutor().execute(() -> {
                userDao.changePass(email, newPass);
                latch.countDown();
            });
            try {
                latch.await();
                Toast.makeText(this, getString(R.string.pass_change_success), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgetPassCompleteActivity.this, SignUpActivity.class));
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            String email = appLinkData.getLastPathSegment();
            txtEmail.setText(email);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}