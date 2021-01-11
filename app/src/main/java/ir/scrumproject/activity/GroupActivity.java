package ir.scrumproject.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import ir.scrumproject.R;
import ir.scrumproject.adapter.MemberAdapter;
import ir.scrumproject.api.Group;
import ir.scrumproject.api.Member;
import ir.scrumproject.data.model.User2;
import ir.scrumproject.retrofit.ApiClient;
import ir.scrumproject.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MemberAdapter adapter;
    private CollapsingToolbarLayout toolBarLayout;
    private int groupId;
    private List<Member> members;
    private ir.scrumproject.api.Group group;

    //Todo: addMember mthod
    //Todo: showMember in list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        groupId = Integer.parseInt(getIntent().getStringExtra("GroupId"));
        getGroupById();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarLayout = findViewById(R.id.toolbar_layout);
        recyclerView = findViewById(R.id.member_list);
    }

    private void getGroupById() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ir.scrumproject.api.Group> responseCall = apiInterface.getGroupById(groupId);

        responseCall.enqueue(new Callback<ir.scrumproject.api.Group>() {
            @Override
            public void onResponse(Call<ir.scrumproject.api.Group> call, Response<ir.scrumproject.api.Group> response) {

                if (response.isSuccessful()) {
                    group = response.body();
                    toolBarLayout.setTitle(group.getName());
                    members = response.body().getMembers();
                    adapter = new MemberAdapter(GroupActivity.this, members);
                    runOnUiThread(() -> recyclerView.setAdapter(adapter));
                } else
                    runOnUiThread(() -> Toast.makeText(GroupActivity.this, response.message(), Toast.LENGTH_SHORT).show());
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
            ArrayList<User2> users = new ArrayList();
            for (Member member : members) {
                users.add(member.getUser());
            }

            Intent intent = new Intent(GroupActivity.this, InvoiceActivity.class);
            intent.putExtra("groupId", String.valueOf(groupId));
            intent.putParcelableArrayListExtra("users", users);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_addMember) addMember();
        return super.onOptionsItemSelected(item);
    }

    private void addMember() {

        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_add_member, null, false);

        EditText emailEditText = view.findViewById(R.id.textInputLayout5);
//        if (email.equals(""))
//            emailEditText.setError("ایمیل را وارد کنید.");

        Button saveButton = view.findViewById(R.id.dialog_positive);
        saveButton.setOnClickListener(v -> {
        String email = emailEditText.getText().toString().trim();
            sendRequest(email);
            dialog.dismiss();
            showKeyboard();
        });

        Button cancelButton = view.findViewById(R.id.dialog_negative_btn);
        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
            hideKeyboard();
        });

        dialog.setContentView(view);
        dialog.create();
        dialog.show();
        showKeyboard();
    }

    private void sendRequest(String email) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Group> responseCall = apiInterface.addMemberToGroup(getUserToken(), groupId, email);

        responseCall.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {

                if (response.isSuccessful())
                    adapter.setMemberList(response.body().getMembers());
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                runOnUiThread(() -> Toast.makeText(GroupActivity.this, "اتصال اینترنت خود را چک کنید.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {

    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private String getUserToken() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loginPref", MODE_PRIVATE);
        return sharedPreferences.getString("token", "tokenDef");
    }
}