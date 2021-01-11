package ir.scrumproject.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ir.scrumproject.R;
import ir.scrumproject.adapter.InvoiceAdapter;
import ir.scrumproject.data.model.User2;

public class InvoiceActivity extends AppCompatActivity {

    private int groupId;
    private ArrayList<User2> users;
    private RecyclerView recyclerView;
    private InvoiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        groupId = Integer.parseInt(getIntent().getStringExtra("groupId"));
        users = getIntent().getParcelableArrayListExtra("users");
        recyclerView = findViewById(R.id.invoice_list);

        FloatingActionButton actionButton = findViewById(R.id.add_invoice_btn);
        actionButton.setOnClickListener(v -> {
            Intent intent = new Intent(InvoiceActivity.this, CreateInvoiceActivity.class);
            intent.putExtra("groupId", String.valueOf(groupId));
            intent.putParcelableArrayListExtra("users", users);
            startActivity(intent);
        });
    }
}