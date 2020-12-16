package ir.scrumproject.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import ir.scrumproject.Constants;
import ir.scrumproject.R;
import ir.scrumproject.api.ApiService;
import ir.scrumproject.api.Group;
import ir.scrumproject.api.MyRetrofit;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbarChatSettings;
    private ImageView imgSelectImage;
    private TextView txtInviteLink;
    private TextView txtDeleteGroup;
    private EditText edtGroupName;
    private EditText edtGroupBio;
    private String fakeToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjQiLCJpYXQiOjE2MDgwMjc2Mzd9.Ay5dOqGwcaUZirhYvIuwdyctfviZV7TVduLqE2PVkCI";
    private int fakeGroupId = 5;
    private String groupLink;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_settings);

        edtGroupBio = findViewById(R.id.edtGroupBio);
        edtGroupName = findViewById(R.id.edtGroupName);
        txtInviteLink = findViewById(R.id.txtInviteLink);
        imgSelectImage = findViewById(R.id.imgSelectImage);
        txtDeleteGroup = findViewById(R.id.txtDeleteGroup);
        imgSelectImage.setOnClickListener(this);
        txtInviteLink.setOnClickListener(this);
        txtDeleteGroup.setOnClickListener(this);

        toolbarChatSettings = findViewById(R.id.toolbarChatSettings);
        setSupportActionBar(toolbarChatSettings);
        toolbarChatSettings.setNavigationOnClickListener(v -> {
            finish();
        });

        apiService = MyRetrofit.getApi();
        apiService.getGroupSettings(fakeToken, fakeGroupId).enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if (response.isSuccessful()) {
                    Group group = response.body();
                    groupLink = group.getLink();
                    String avatarUrl = Constants.BASE_URL + group.getAvatar();
                    edtGroupName.setText(group.getName());
                    edtGroupBio.setText(group.getBio());
                    Glide.with(ChatSettingsActivity.this).load(avatarUrl).circleCrop().into(imgSelectImage);
                } else {
                    Log.d("MyLog", "onNotSuccess: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Log.d("MyLog", "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveChatSettings:
                updateGroup();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateGroup() {
        RequestBody name =
                RequestBody.create(MediaType.parse("multipart/form-data"), edtGroupName.getText().toString());

        RequestBody bio =
                RequestBody.create(MediaType.parse("multipart/form-data"), edtGroupBio.getText().toString());

        apiService.updateGroup(fakeToken, fakeGroupId, name, bio, null).enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if (response.isSuccessful()) {
                    Log.d("MyLog", "اطلاعات گروه به روز شد");
                    finish();
                } else {
                    Log.d("MyLog", "onNotSuccess: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Log.d("MyLog", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSelectImage:
                if (hasStoragePermission()) {
                    chooseImageFromGallery();
                } else {
                    requestStoragePermission();
                }
                break;

            case R.id.txtInviteLink:
                openInviteLinkPage();
                break;

            case R.id.txtDeleteGroup:
                deleteGroup();
                break;
        }
    }

    private void deleteGroup() {
        apiService.deleteGroup(fakeToken, fakeGroupId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChatSettingsActivity.this, "گروه حذف شد", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.d("MyLog", "onNotSuccess: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("MyLog", "onFailure: " + t.getMessage());
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

    private void openInviteLinkPage() {
        Intent intent = new Intent(this, InviteLinkActivity.class);
        intent.putExtra(Constants.GROUP_LINK, groupLink);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 410:
                Uri imageUri = data.getData();
                Glide.with(this).load(imageUri).circleCrop().into(imgSelectImage);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 325:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImageFromGallery();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}