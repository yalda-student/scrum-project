package ir.scrumproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import ir.scrumproject.R;
import ir.scrumproject.api.Group;
import ir.scrumproject.retrofit.ApiClient;
import ir.scrumproject.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity {

    private CollapsingToolbarLayout toolBarLayout;
    private int groupId;
    private ir.scrumproject.api.Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        groupId = Integer.parseInt(getIntent().getStringExtra("GroupId"));
        getGroupById();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarLayout = findViewById(R.id.toolbar_layout);

    }

    private void getGroupById() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ir.scrumproject.api.Group> responseCall = apiInterface.getGroupById(groupId);

        responseCall.enqueue(new Callback<ir.scrumproject.api.Group>() {
            @Override
            public void onResponse(Call<ir.scrumproject.api.Group> call, Response<ir.scrumproject.api.Group> response) {
                group = response.body();
                toolBarLayout.setTitle(group.getName());
            }

            @Override
            public void onFailure(Call<ir.scrumproject.api.Group> call, Throwable t) {
                runOnUiThread(() -> Toast.makeText(GroupActivity.this, "اتصال اینترنت خود را چک کنید.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(GroupActivity.this, ChatSettingsActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_invoice) {
            Intent intent = new Intent(GroupActivity.this, InvoiceActivity.class);
            intent.putExtra("groupId", String.valueOf(groupId));
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_addMember) addMember();
        return super.onOptionsItemSelected(item);
    }

    private void addMember() {

    }
}