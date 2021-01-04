package ir.scrumproject.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import ir.scrumproject.R;
import ir.scrumproject.api.Group;
import ir.scrumproject.retrofit.ApiClient;
import ir.scrumproject.retrofit.ApiInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton avatarImageView;
    private TextInputEditText nameText;
    private TextInputEditText maxText;
    private TextInputEditText bioEditText;
    private TextInputEditText welcomeText;
    private ProgressBar progressBar;
    private MultipartBody.Part imageBody;
    private Uri imageUri;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Toolbar toolbar = findViewById(R.id.toolbarCreateGP);
        avatarImageView = findViewById(R.id.imgSelectImage);
        nameText = findViewById(R.id.group_name);
        maxText = findViewById(R.id.groupSizeText);
        bioEditText = findViewById(R.id.groupBioText);
        welcomeText = findViewById(R.id.groupMsgText);
        FloatingActionButton createButton = findViewById(R.id.create_gp_fab);
        progressBar = findViewById(R.id.progressCreate);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        avatarImageView.setOnClickListener(this);
        createButton.setOnClickListener(this);

        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.create_gp_fab) {
            createGroup();
        } else if (id == R.id.imgSelectImage) {
            if (hasStoragePermission()) {
                chooseImageFromGallery();
            } else {
                requestStoragePermission();
            }
        }
    }

    private void createGroup() {
        progressBar.setVisibility(View.VISIBLE);

        String token = getUserToken();
        Log.d("TOKEN", "createGroup: " + token);
        String name = nameText.getText().toString();
        String bio = bioEditText.getText().toString();
        String size = maxText.getText().toString();
        String msg = welcomeText.getText().toString();

        if (imageUri != null) {
            String realUri = getRealPathFromURI(imageUri);
            File file = new File(realUri);
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), file);
            imageBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
        }

        RequestBody nameBody = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody sizeBody = RequestBody.create(MediaType.parse("multipart/form-data"), size);
        RequestBody bioBody = RequestBody.create(MediaType.parse("multipart/form-data"), bio);
        RequestBody msgBody = RequestBody.create(MediaType.parse("multipart/form-data"), msg);

        apiInterface.createGroup(token, nameBody, bioBody, sizeBody, msgBody, imageBody).enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: " + token);
                    runOnUiThread(()->{ Toast.makeText(CreateGroupActivity.this, "گروه ساخته شد.", Toast.LENGTH_SHORT).show();
                        finish();});

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Log.d("MyLog", "onFailure: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 325);
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 410);
    }

    private boolean hasStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private String getUserToken() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loginPref", MODE_PRIVATE);
        return sharedPreferences.getString("token", "tokenDef");
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 410) {
            if (data != null) {
                imageUri = data.getData();
                Glide.with(this)
                        .load(imageUri)
                        .circleCrop()
                        .into(avatarImageView);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}