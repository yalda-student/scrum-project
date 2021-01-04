package ir.scrumproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import ir.scrumproject.R;

public class CreateInvoiceActivity extends AppCompatActivity {

    private Spinner spinner;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextInputEditText codeEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText priceEditText;
    private TextInputEditText shareEditText;
    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);

        findViewsById();

    }

    private void findViewsById() {

        spinner = findViewById(R.id.member_invoice_spinner);
        radioGroup = findViewById(R.id.radioGroup2);
        codeEditText = findViewById(R.id.product_code);
        nameEditText = findViewById(R.id.product_name);
        priceEditText = findViewById(R.id.product_price);
        shareEditText = findViewById(R.id.member_share);
        actionButton = findViewById(R.id.confirm_invoice_btn);
    }

    public void radioButtonClickListener(View view) {

        switch (view.getId()) {
            case R.id.bill_btn:
                break;
            case R.id.food_btn:
                break;
            case R.id.house_btn:
                break;
            case R.id.transport_btn:
                break;
            case R.id.needing_btn:
                break;
            case R.id.other_btn:
                break;
        }
    }
}