package ir.scrumproject.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import ir.scrumproject.Constants;
import ir.scrumproject.R;
import ir.scrumproject.data.AppDatabase;
import ir.scrumproject.data.dao.UserDao;
import ir.scrumproject.forget.MailSender;

public class ForgetPassActivity extends AppCompatActivity {
    EditText edtEmail;
    Button btnSendEmail;
    AppDatabase db;
    UserDao userDao;
    int count = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        edtEmail = findViewById(R.id.edtEmail);
        btnSendEmail = findViewById(R.id.btnSendEmail);

        btnSendEmail.setOnClickListener(v -> {
            String address = edtEmail.getText().toString();
            if (!isValidEmail(address)) {
                Toast.makeText(this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isUserRegister(address)) {
                Toast.makeText(this, getString(R.string.register_first), Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                MailSender mailSender = new MailSender(Constants.EMAIL_USERNAME, Constants.EMAIL_PASSWORD);
                try {
                    mailSender.sendMail(getString(R.string.change_pass), getString(R.string.body, address), Constants.EMAIL_USERNAME, address);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Toast.makeText(this, getString(R.string.check_your_email), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private boolean isValidEmail(String value) {
        return (!TextUtils.isEmpty(value) && Patterns.EMAIL_ADDRESS.matcher(value).matches());
    }

    private boolean isUserRegister(String email) {
        CountDownLatch latch = new CountDownLatch(1);
        Executors.newSingleThreadExecutor().execute(() -> {
            count = userDao.registerCount(email);
            latch.countDown();
        });

        try {
            latch.await();
            return count > 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}