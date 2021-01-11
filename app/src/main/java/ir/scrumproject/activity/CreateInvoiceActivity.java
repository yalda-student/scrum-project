package ir.scrumproject.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import ir.scrumproject.R;
import ir.scrumproject.api.ApiService;
import ir.scrumproject.api.Invoice;
import ir.scrumproject.api.MyRetrofit;
import ir.scrumproject.api.Payer;
import ir.scrumproject.api.Share;
import ir.scrumproject.data.model.User2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateInvoiceActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioGroup radioGroupInvoice;
    private TextInputEditText edtTitleInvoice, edtDescriptionInvoice, edtCostInvoice;
    private FloatingActionButton btnCreateInvoice;
    private TextView btnAddPerson;
    private LinearLayout containerInvoice;
    private List<User2> users;
    private int groupId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        initViews();
        apiService = MyRetrofit.getApi();

        groupId = Integer.parseInt(getIntent().getStringExtra("groupId"));
        users = getIntent().getParcelableArrayListExtra("users");

        btnAddPerson.setOnClickListener(this);
        btnCreateInvoice.setOnClickListener(this);
        createView();
    }

    private void initViews() {
        radioGroupInvoice = findViewById(R.id.radioGroupInvoice);
        edtTitleInvoice = findViewById(R.id.edtTitleInvoice);
        edtCostInvoice = findViewById(R.id.edtCostInvoice);
        edtDescriptionInvoice = findViewById(R.id.edtDescriptionInvoice);
        btnCreateInvoice = findViewById(R.id.btnCreateInvoice);
        btnAddPerson = findViewById(R.id.btnAddPerson);
        containerInvoice = findViewById(R.id.containerInvoice);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnAddPerson:
                createView();
                break;

            case R.id.btnCreateInvoice:
                String title = String.valueOf(edtTitleInvoice.getText());
                int cost = Integer.parseInt(String.valueOf(edtCostInvoice.getText()));
                String description = String.valueOf(edtDescriptionInvoice.getText());
                String category = getInvoiceCategory();
                List<Share> shares = new ArrayList<>();
                List<Payer> payers = new ArrayList<>();
                for (int i = 0; i < containerInvoice.getChildCount(); i++) {
                    LinearLayout layout = (LinearLayout) containerInvoice.getChildAt(i);
                    Spinner spinnerInvoice = layout.findViewById(R.id.spinnerInvoice);
                    EditText edtValueInvoice = layout.findViewById(R.id.edtValueInvoice);
                    CheckBox chbInvoice = layout.findViewById(R.id.chbInvoice);

                    int value = Integer.parseInt(String.valueOf(edtValueInvoice.getText()));
                    User2 user = (User2) spinnerInvoice.getSelectedItem();
                    shares.add(new Share(user.getId(), 1));
                    if (chbInvoice.isChecked())
                        payers.add(new Payer(user.getId(), value));
                }

                Invoice invoice = new Invoice(title, description, category, cost, groupId, shares, payers);

                Executors.newSingleThreadExecutor().execute(() -> {
                    apiService.createInvoice(invoice).enqueue(new Callback<Invoice>() {
                        @Override
                        public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(CreateInvoiceActivity.this, "invoice created!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Log.d("MyLog", "is not ok");
                            }
                        }

                        @Override
                        public void onFailure(Call<Invoice> call, Throwable t) {
                            Log.d("MyLog", t.getMessage());
                        }
                    });
                });
                break;
        }
    }

    private String getInvoiceCategory() {
        String category = "";
        int checkedId = radioGroupInvoice.getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radioBillInvoice:
                category = "Bill";
                break;

            case R.id.radioFoodInvoice:
                category = "Food";
                break;

            case R.id.radioHouseInvoice:
                category = "House";
                break;

            case R.id.radioNeedInvoice:
                category = "Need";
                break;

            case R.id.radioOtherInvoice:
                category = "Other";
                break;

            case R.id.radioTransportInvoice:
                category = "Transport";
                break;
        }

        return category;
    }

    private void createView() {
        View view1 = getLayoutInflater().inflate(R.layout.layout_container_invoice, null);
        Spinner spinner = view1.findViewById(R.id.spinnerInvoice);
        ArrayAdapter<User2> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);
        spinner.setAdapter(adapter);
        containerInvoice.addView(view1);
    }
}