package ir.scrumproject.activity.Main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ir.scrumproject.R;
import ir.scrumproject.adapter.GroupAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainContractImpl  mainContract = new MainContractImpl();

        Button addGroupButton = findViewById(R.id.add_group_btn);
        addGroupButton.setOnClickListener(addGroupListener);

        RecyclerView recyclerView = findViewById(R.id.group_list);
//        GroupAdapter groupAdapter = new GroupAdapter(this, mainContract.getGroupsList());
//        recyclerView.setAdapter(groupAdapter);

    }

    private View.OnClickListener addGroupListener = v -> {

    };
}