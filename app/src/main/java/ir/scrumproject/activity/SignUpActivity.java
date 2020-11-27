package ir.scrumproject.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.scrumproject.R;
import ir.scrumproject.data.AppDatabase;
import ir.scrumproject.data.model.User;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText nameET, usernameET, emailET, passwordET, confirmET;
    ShapeableImageView profile;
    CheckBox agreement;
    Button register;
    TextView switchToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        bind();
        profile.setOnClickListener(view -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, 100);
        });
        switchToLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });
        register.setOnClickListener(view -> {
            String name = nameET.getText().toString().trim();
            String username = usernameET.getText().toString().trim();
            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();
            String confirm = confirmET.getText().toString().trim();
            if (name.length() == 0 || username.length() == 0 || email.length() == 0 || password.length() == 0 || confirm.length() == 0) {
                Toast.makeText(this, "همه ی موارد را کامل کنید", Toast.LENGTH_LONG).show();
                return;
            }
            if (!isEmailValid(email)) {
                Toast.makeText(this, "ایمیل شما معتبر نیست", Toast.LENGTH_LONG).show();
                return;
            }
            if (!password.equals(confirm)) {
                Toast.makeText(this, "رمز عبور و تکرار آن باید یکسان باشد", Toast.LENGTH_LONG).show();
                return;
            }
            if (!agreement.isChecked()) {
                Toast.makeText(this, "با قوانین برنامه باید موافقت کنید", Toast.LENGTH_LONG).show();
                return;
            }
            String photo = "";
            try {
                Bitmap bitmap = Bitmap.createBitmap(profile.getWidth(), profile.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                profile.draw(canvas);
                photo = getCacheDir().getAbsolutePath() + System.currentTimeMillis() + ".png";
                File file = new File(photo);
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                String ee = e + "";
            }
            User user = new User();
            user.name = name;
            user.username = username;
            user.email = email;
            user.password = password;
            user.photo = photo;
            new Thread(() -> {
                try {
                    AppDatabase.getInstance(SignUpActivity.this).userDao().insertUser(user);
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    SignUpActivity.this.finish();
                } catch (SQLiteConstraintException e) {
                    runOnUiThread(() -> {
                        String msg = e.getMessage();
                        String field = msg.split("UNIQUE constraint failed: User\\.")[1].split(" ")[0];
                        if (field.equals("email")) {
                            Toast.makeText(SignUpActivity.this, "این ایمیل قبلا ثبت شده است", Toast.LENGTH_LONG).show();
                        } else if (field.equals("username")) {
                            Toast.makeText(SignUpActivity.this, "این نام کاربری قبلا ثبت شده است", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).start();

        });
    }

    void bind() {
        nameET = findViewById(R.id.nameInput);
        usernameET = findViewById(R.id.usernameInput);
        emailET = findViewById(R.id.emailInput);
        passwordET = findViewById(R.id.passwordInput);
        confirmET = findViewById(R.id.confirmInput);
        agreement = findViewById(R.id.agreementCheckbox);
        register = findViewById(R.id.register);
        switchToLogin = findViewById(R.id.switchToLogin);
        profile = findViewById(R.id.profileImage);
    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && null != data) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profile.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}