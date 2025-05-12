package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.dao.impl.SQLiteUserDAO;
import com.example.todolist.model.User;


public class ChangePasswordActivity extends AppCompatActivity {
    ImageButton btn_back;
    SQLiteUserDAO db;
    EditText et_mkc,et_mkm,et_mkm1;
    Button btn;
    int userId=-1;
    User u=new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());
        db=new SQLiteUserDAO(this);
        et_mkc=findViewById(R.id.et_mkc);
        et_mkm=findViewById(R.id.et_mkm);
        et_mkm1=findViewById(R.id.et_mkm1);


        btn=findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mkc=et_mkc.getText().toString();
                String mkm=et_mkm.getText().toString();
                String nl=et_mkm1.getText().toString();
                if(validateField(mkc,mkm,nl)){
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    userId = sharedPreferences.getInt("userId", -1); // -1 là mặc định nếu không có

                    if (userId != -1) {
                        u = db.getUserById(userId);
                        if(mkc.equals(u.getPassword())){
                            boolean check =db.updatePassword(u.getId(),mkm);
                            if(check){
                                Toast.makeText(ChangePasswordActivity.this,"Cập nhật thành công",Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(ChangePasswordActivity.this,"Cập nhật thất bại",Toast.LENGTH_LONG).show();
                                et_mkc.setText("");
                                et_mkm.setText("");
                                et_mkm1.setText("");
                            }
                        }else{
                            Toast.makeText(ChangePasswordActivity.this,"Mật khẩu cũ không đúng. Vui lòng nhập lại",Toast.LENGTH_LONG).show();
                            et_mkc.setText("");
                            et_mkm.setText("");
                            et_mkm1.setText("");
                        }
                    }
                }
            }
        });

    }
    public boolean validateField(String oldPassword, String password,String confirmPassword){
        if (oldPassword.isEmpty()) {
            et_mkc.setError("Mật khẩu không được để trống");
            return false;
        }

        String p=password.trim();
        // Kiểm tra mật khẩu
        if (password.isEmpty()) {
            et_mkm.setError("Mật khẩu không được để trống");
            return false;
        }else if (password.length() > p.length()) {
            et_mkm.setError("Không được phép có khoảng trống đầu/cuối ở mật khẩu");
            return false;
        } else if (password.length() < 6) {
            et_mkm.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return false;
        }
        String p1=confirmPassword.trim();
        // Kiểm tra mật khẩu xác nhận
        if (confirmPassword.isEmpty()) {
            et_mkm1.setError("Vui lòng xác nhận mật khẩu");
            return false;
        }  else if (!confirmPassword.equals(p1)) {
            et_mkm1.setError("Không được phép có khoảng trống đầu/cuối ở mật khẩu");
            return false;
        }else if (!confirmPassword.equals(password)) {
            et_mkm1.setError("Mật khẩu xác nhận không khớp");
            return false;
        }

        return true;
    }
}