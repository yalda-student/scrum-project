package ir.scrumproject.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ir.scrumproject.Constants;
import ir.scrumproject.R;

public class InviteLinkActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbarInviteLink;
    TextView txtGroupLink;
    TextView txtCopyLink;
    TextView txtShareLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_link);

        txtGroupLink = findViewById(R.id.txtGroupLink);
        txtCopyLink = findViewById(R.id.txtCopyLink);
        txtShareLink = findViewById(R.id.txtShareLink);
        toolbarInviteLink = findViewById(R.id.toolbarInviteLink);
        toolbarInviteLink.setNavigationOnClickListener(v -> {
            finish();
        });

        txtCopyLink.setOnClickListener(this);
        txtShareLink.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.GROUP_LINK)) {
            String groupLink = intent.getStringExtra(Constants.GROUP_LINK);
            txtGroupLink.setText(Constants.BASE_URL + groupLink);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtCopyLink:
                copyToClipBoard(txtGroupLink.getText());
                Toast.makeText(this, "لینک کپی شد", Toast.LENGTH_SHORT).show();
                break;

            case R.id.txtShareLink:
                shareText(txtGroupLink.getText());
                break;

        }
    }

    private void shareText(CharSequence text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "لینک عضویت در گروه");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }

    private void copyToClipBoard(CharSequence charSequence) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, charSequence);
        clipboard.setPrimaryClip(clip);
    }
}