package com.example.todolist;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.dao.impl.SQLiteUserDAO;
import com.example.todolist.model.User;
import android.util.Log;

public class LoginActivity extends AppCompatActivity {
    ImageButton backButton;
    TextView registerText,forgotPassword;
    Button loginButton;
    EditText emailEditText,passwordEditText;
    SQLiteUserDAO db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Nút quay lại
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,WelcomeActivity.class);
                startActivity(intent);
            }
        });

        // Chuyển sang trang Đăng ký
        registerText = findViewById(R.id.registerText);
        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        forgotPassword = findViewById(R.id.forgotPasswordText);
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        init();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailEditText.getText().toString().trim();
                String password=passwordEditText.getText().toString();
                if(validateFields(email,password)){
                    User u=db.findUser(email);
                    if(u!=null){
                        if(password.equals(u.getPassword())){
                            Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("userId", u.getId());
                            editor.apply();
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this,"Mật khẩu không đúng",Toast.LENGTH_LONG).show();
                            passwordEditText.setText("");
                        }
                    }else{
                        Toast.makeText(LoginActivity.this,"Email không tồn tại trong hệ thống",Toast.LENGTH_LONG).show();
                        passwordEditText.setText("");
                        emailEditText.setText("");
                    }
                }
            }
        });
    }
    public void init(){
        loginButton=findViewById(R.id.loginButton);
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        db=new SQLiteUserDAO(this);
    }
    private boolean validateFields(String email, String password) {
        // Kiểm tra email
        if (email.isEmpty()) {
            emailEditText.setError("Email không được để trống");
            return false;
        }
        // Kiểm tra mật khẩu
        if (password.isEmpty()) {
            passwordEditText.setError("Mật khẩu không được để trống");
            return false;
        }
        return true;
    }
}
