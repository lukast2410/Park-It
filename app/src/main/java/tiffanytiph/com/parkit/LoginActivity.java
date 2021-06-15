package tiffanytiph.com.parkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tiffanytiph.com.parkit.model.User;
import tiffanytiph.com.parkit.util.Helper;

public class LoginActivity extends AppCompatActivity {
    LinearLayout llRegisterHere;
    EditText etEmail, etPassword;
    Button loginBtn;
    TextView errLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        settingButton();
    }

    private void settingButton() {
        llRegisterHere.setOnClickListener(x -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(x -> {
            if(etEmail.getText().toString().isEmpty()) {
                etEmail.setError("Must be filled!");
            }
            if(etPassword.getText().toString().isEmpty()){
                etPassword.setError("Must be filled!");
            }

            if(!etEmail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()){
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                User user = User.userLogin(this, email, password);

                if(user == null) {
                    errLogin.setVisibility(View.VISIBLE);
                    return;
                }else errLogin.setVisibility(View.GONE);

                Helper.getInstance().setCurrentUser(user);
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void initialize() {
        llRegisterHere = (LinearLayout) findViewById(R.id.ll_register_here);
        etEmail = (EditText) findViewById(R.id.et_login_email);
        etPassword = (EditText) findViewById(R.id.et_login_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        errLogin = (TextView) findViewById(R.id.tv_authentication_failed);
    }
}