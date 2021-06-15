package tiffanytiph.com.parkit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import tiffanytiph.com.parkit.model.User;

public class RegisterActivity extends AppCompatActivity {
    LinearLayout llLoginHere;
    EditText etName, etEmail, etPhone, etPassword, etConfirm;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
        settingButton();
    }

    private void settingButton() {
        llLoginHere.setOnClickListener(x -> {
            finish();
        });

        registerBtn.setOnClickListener(x -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String password = etPassword.getText().toString();
            String confirm = etConfirm.getText().toString();
            boolean success = true;

            if (name.isEmpty()) {
                etName.setError("Must be filled!");
                success = false;
            }
            if (!email.endsWith(".com") || email.indexOf('@') == -1 || email.indexOf('@') != email.lastIndexOf('@')
                    || email.indexOf('@') + 1 == email.lastIndexOf('.')) {
                etEmail.setError("Wrong email format!");
                success = false;
            }
            if (phone.length() < 11 || phone.length() > 13) {
                etPhone.setError("Phone number must be between 11 and 13!");
                success = false;
            }
            if (password.length() < 6) {
                etPassword.setError("Password must be greater than 5!");
                success = false;
            }
            if (!confirm.equals(password)) {
                etConfirm.setError("Not same with password!");
                success = false;
            }
            if (!success) return;

            User.insertUser(this, new User(name, email, password, phone));
            finish();
        });
    }

    private void initialize() {
        llLoginHere = (LinearLayout) findViewById(R.id.ll_login_here);
        etName = (EditText) findViewById(R.id.et_register_name);
        etEmail = (EditText) findViewById(R.id.et_register_email);
        etPhone = (EditText) findViewById(R.id.et_register_phone);
        etPassword = (EditText) findViewById(R.id.et_register_password);
        etConfirm = (EditText) findViewById(R.id.et_register_confirm);
        registerBtn = (Button) findViewById(R.id.btn_register);
    }
}