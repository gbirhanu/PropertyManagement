package com.dsd.machinerental;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {
    private EditText mNameField, mPhoneField,mStatusField;
Button mCopy;
    private String mName,JSON_STRING,state;
    private String mPhone,status;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mCopy = findViewById(R.id.textCopy);
        mNameField = findViewById(R.id.name);
        mPhoneField = findViewById(R.id.phone);
        mStatusField = findViewById(R.id.status);
        mName=getIntent().getStringExtra("name");
        mPhone = getIntent().getStringExtra("phone");
        status = getIntent().getStringExtra("statuis");
        if (status.equals("1")){
            state = getString(R.string.activate_service_txt);
        }else {
            state = getString(R.string.not_activated_txt);
        }
        mNameField.setText(mName);
        mPhoneField.setText(mPhone);
        mStatusField.setText(state);
mCopy.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Phone", mPhoneField.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(ProfileActivity.this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
    }
});
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
