package ir.scrumproject.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.scrumproject.R;
import ir.scrumproject.activity.Main.MainActivity;
import ir.scrumproject.activity.signIn.SignInActivity;
import ir.scrumproject.data.AppDatabase;
import ir.scrumproject.data.model.User;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText nameET, usernameET, emailET, passwordET, confirmET;
    ShapeableImageView profile;
    CheckBox agreement;
    Button register;
    TextView switchToLogin;

    String name,username,email,password;
    Bitmap bitmap;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        bind();
        profile.setOnClickListener(view -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 100);

        });
        switchToLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });
        register.setOnClickListener(view -> {
            name = nameET.getText().toString().trim();
            username = usernameET.getText().toString().trim();
            email = emailET.getText().toString().trim();
            password = passwordET.getText().toString().trim();
            String confirm = confirmET.getText().toString().trim();
            if (name.length() == 0 || username.length() == 0 || email.length() == 0 || password.length() == 0 || confirm.length() == 0) {
                Toast.makeText(this, "همه ی موارد را کامل کنید", Toast.LENGTH_LONG).show();
                return;
            }
            if(!isEmailValid(email)){
                Toast.makeText(this, "ایمیل شما معتبر نیست", Toast.LENGTH_LONG).show();
                return;
            }
            if(password.length()<8){
                Toast.makeText(this, "رمز عبور باید 8 رقم باشد", Toast.LENGTH_LONG).show();
                return;
            }
            if(!password.equals(confirm)){
                Toast.makeText(this, "رمز عبور و تکرار آن باید یکسان باشد", Toast.LENGTH_LONG).show();
                return;
            }
            if(!agreement.isChecked()){
                Toast.makeText(this, "با قوانین برنامه باید موافقت کنید", Toast.LENGTH_LONG).show();
                return;
            }
            register.setEnabled(false);
            String photo = "";
            try {
                bitmap = Bitmap.createBitmap(profile.getWidth(), profile.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                profile.draw(canvas);
                photo = getCacheDir().getAbsolutePath()+System.currentTimeMillis()+".png";
                File file = new File(photo);
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            }catch (Exception e){
                String ee = e +"";
            }
            user.name = name;
            user.username = username;
            user.email = email;
            user.password = password;
            user.photo = photo;
            new Thread(new Runnable() {
                public void run() {
                    String res = sendData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(res.length()>0){
                                Toast.makeText(SignUpActivity.this, res, Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                SignUpActivity.this.finish();
                            }
                            register.setEnabled(true);
                        }
                    });
                }
            }).start();


        });
    }

    String attachmentName = "avatar";
    String attachmentFileName = "avatar.jpg";
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    private String sendData(){
        try{
            HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL("http://217.79.184.183:5010/user").openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            JSONObject object = new JSONObject();
            object.put("name",name);
            object.put("username",username);
            object.put("password",password);
            object.put("email",email);
            OutputStreamWriter wr= new OutputStreamWriter(httpUrlConnection.getOutputStream());
            wr.write(object.toString());
            wr.flush();
            wr.close();
            int a = httpUrlConnection.getResponseCode();
            String response = "";
            try{
                response = new Scanner(httpUrlConnection.getInputStream()).useDelimiter("\\A").next();
            }catch (Exception ee){
                response = new Scanner(httpUrlConnection.getErrorStream()).useDelimiter("\\A").next();
            }
            if(a!=200){
                return new JSONObject(response).getString("error");
            }
            JSONObject roj = new JSONObject(response);
            String token = roj.getString("token");
            user.token = token;
            sendAvatar(token);
            AppDatabase.getInstance(SignUpActivity.this).userDao().insertUser(user);
            return "";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    private void sendAvatar(String token){
        try{
            HttpURLConnection httpUrlConnection = null;
            URL url = new URL("http://217.79.184.183:5010/user/me");
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("PATCH");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty("Authorization",token);
            httpUrlConnection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + this.boundary);
            DataOutputStream request = new DataOutputStream(
                    httpUrlConnection.getOutputStream());

            request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    this.attachmentName + "\";filename=\"" +
                    this.attachmentFileName + "\"" + this.crlf);
            request.writeBytes(this.crlf);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap.recycle();
            request.write(byteArray);
            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary +
                    this.twoHyphens + this.crlf);
            request.flush();
            request.close();
            int a = httpUrlConnection.getResponseCode();
            String response = "";
            try{
                response = new Scanner(httpUrlConnection.getInputStream()).useDelimiter("\\A").next();
            }catch (Exception ee){
                response = new Scanner(httpUrlConnection.getErrorStream()).useDelimiter("\\A").next();
            }
            if(a!=200){

            }
        }catch (Exception e){
            String aa = e + "";
        }
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
}