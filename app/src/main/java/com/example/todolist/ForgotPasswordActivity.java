package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.dao.impl.SQLiteUserDAO;
import com.example.todolist.model.User;
import com.example.todolist.sendEmail.EmailSender;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {
    ImageButton backButton;
    EditText emailEditText;
    Button button;
    SQLiteUserDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        db=new SQLiteUserDAO(this);
        emailEditText=findViewById(R.id.emailEditText);
        button=findViewById(R.id.Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailEditText.getText().toString();
                User u=db.findUser(email);
                if(u!=null){
                    String password=generateCode();
                    db.updatePassword(u.getId(),password);
                    EmailSender.sendEmail(email,"Cấp lại mật khẩu","Mật khẩu của bạn là:"+password);
                    Toast.makeText(ForgotPasswordActivity.this,"Đã gửi email!",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ForgotPasswordActivity.this,"Email không tồn tại trong hệ thống",Toast.LENGTH_LONG).show();
                    emailEditText.setText("");
                }
            }
        });

    }
    public static String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Sinh ra số từ 100000 đến 999999
        return String.valueOf(code);
    }
}