package fr.zankia.android.chat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NamePickerActivity extends AppCompatActivity {
    public static final int PERMISSION_ACCOUNT = 2;

    EditText mNameEditText, mEmailEditText;
    Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namepicker);

        mNameEditText = findViewById(R.id.nameEditText);
        mEmailEditText = findViewById(R.id.emailEditText);
        mSubmitButton = findViewById(R.id.sendButton);

        fillDefaultEmail();

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveUserData()) {
                    goToMainActivity();
                }
            }
        });
    }

    private void goToMainActivity() {
        this.startActivity(new Intent(this, MainActivity.class));
    }

    private boolean saveUserData() {
        String name  = mNameEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            return false;
        }

        UserStorage.saveUserInfo(this, name, email);
        return true;
    }

    private void fillDefaultEmail() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.GET_ACCOUNTS)) {
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.GET_ACCOUNTS},
                        PERMISSION_ACCOUNT
                );
            }
        } else {
            mEmailEditText.setText(Utils.getDefaultUserEmail(this));
        }
    }
}
