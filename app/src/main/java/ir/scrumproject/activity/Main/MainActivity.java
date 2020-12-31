package ir.scrumproject.activity.Main;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.scrumproject.R;
import ir.scrumproject.activity.CreateGroupActivity;
import ir.scrumproject.activity.signIn.SignInActivity;
import ir.scrumproject.adapter.GroupAdapter;
import ir.scrumproject.api.Group;
import ir.scrumproject.retrofit.ApiClient;
import ir.scrumproject.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MainContractImpl mainContract;
    private GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainContract = new MainContractImpl(this);

        Button addGroupButton = findViewById(R.id.add_group_btn);
        addGroupButton.setOnClickListener(addGroupListener);

        recyclerView = findViewById(R.id.group_list);

        loadData();

    }

    private void loadData() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Group>> responseCall = apiInterface.getUserGroups(getUserToken());

        responseCall.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                groupAdapter = new GroupAdapter(MainActivity.this, response.body());
                groupAdapter.notifyDataSetChanged();
                runOnUiThread(() -> recyclerView.setAdapter(groupAdapter));
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "اتصال اینترنت خود را چک کنید.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private final View.OnClickListener addGroupListener = v -> startActivity(new Intent(MainActivity.this, CreateGroupActivity.class));

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_exit) {
            exitDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        groupAdapter.notifyDataSetChanged();
    }

    private String getUserToken() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loginPref", MODE_PRIVATE);
        return sharedPreferences.getString("token", "tokenDef");
    }

    void exitDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null, false);

        Button saveButton = view.findViewById(R.id.dialog_positive_btn);
        saveButton.setOnClickListener(v -> {
            mainContract.exitApp(getUserToken());
            dialog.dismiss();
        });

        Button cancelButton = view.findViewById(R.id.dialog_negative_btn);
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);
        dialog.show();
    }
}