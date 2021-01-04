package ir.scrumproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ir.scrumproject.R;
import ir.scrumproject.adapter.InvoiceAdapter;

public class InvoiceActivity extends AppCompatActivity {

    private  int groupId;
    private RecyclerView recyclerView;
    private InvoiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        groupId = Integer.parseInt(getIntent().getStringExtra("groupId"));
        recyclerView = findViewById(R.id.invoice_list);

        FloatingActionButton actionButton = findViewById(R.id.add_invoice_btn);
        actionButton.setOnClickListener(v -> startActivity(new Intent(InvoiceActivity.this, CreateInvoiceActivity.class)));
    }
}