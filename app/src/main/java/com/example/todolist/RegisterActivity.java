package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

import com.example.todolist.dao.impl.SQLiteUserDAO;
import com.example.todolist.model.User;
import com.example.todolist.sendEmail.EmailSender;

public class RegisterActivity extends AppCompatActivity {
    ImageButton backButton;
    EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    Button registerButton;
    TextView login;
    SQLiteUserDAO db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();
        login.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if(validateFields(username, email, password, confirmPassword)){
                    if(checkVerify(email)){
                        boolean check=true;
                        User u=null;
                        String code="";
                        while(check){
                            code=generateCode();
                            u=db.findCode(code);
                            if(u==null){
                                check=false;
                            }
                        }
                        User user=new User(username,email,code,0,password);
                        db.addUser(user);
                        EmailSender.sendEmail(email, "Mã xác minh tài khoản", "Mã xác minh của bạn là: " + code);
                        showVerifyCodeDialog(email);
                    }else{
                        Toast.makeText(RegisterActivity.this,"Email đã tồn tại",Toast.LENGTH_LONG).show();
                        usernameEditText.setText("");
                        emailEditText.setText("");
                        passwordEditText.setText("");
                        confirmPasswordEditText.setText("");
                    }
                }
            }
        });
    }

    public void init(){
        usernameEditText=findViewById(R.id.usernameEditText);
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        confirmPasswordEditText=findViewById(R.id.confirmPasswordEditText);
        registerButton=findViewById(R.id.registerButton);
        login=findViewById(R.id.goToLoginText);
        db=new SQLiteUserDAO(this);
    }
    public static String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Sinh ra số từ 100000 đến 999999
        return String.valueOf(code);
    }
    public boolean checkVerify(String email){
        User u=null;
        u=db.findUser(email);
        if(u==null) return true;
        return false;
    }

    private boolean validateFields(String username, String email, String password, String confirmPassword) {
        // Kiểm tra tên người dùng
        if (username.isEmpty()) {
            usernameEditText.setError("Tên người dùng không được để trống");
            return false;
        }

        // Kiểm tra email
        if (email.isEmpty()) {
            emailEditText.setError("Email không được để trống");
            return false;
        } else if (!isValidEmail(email)) {
            emailEditText.setError("Email không hợp lệ");
            return false;
        }
        String p=password.trim();
        // Kiểm tra mật khẩu
        if (password.isEmpty()) {
            passwordEditText.setError("Mật khẩu không được để trống");
            return false;
        }else if (password.length() > p.length()) {
            passwordEditText.setError("Không được phép có khoảng trống đầu/cuối ở mật khẩu");
            return false;
        } else if (password.length() < 6) {
            passwordEditText.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return false;
        }
        String p1=confirmPassword.trim();
        // Kiểm tra mật khẩu xác nhận
        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Vui lòng xác nhận mật khẩu");
            return false;
        }  else if (!confirmPassword.equals(p1)) {
            confirmPasswordEditText.setError("Không được phép có khoảng trống đầu/cuối ở mật khẩu");
            return false;
        }else if (!confirmPassword.equals(password)) {
            confirmPasswordEditText.setError("Mật khẩu xác nhận không khớp");
            return false;
        }

        return true;
    }

    // Phương thức kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@gmail\\.com$";
        return email.matches(emailPattern);
    }

    // Hiển thị Dialog nhập mã xác minh
    private void showVerifyCodeDialog(String email) {
        // Tạo một LinearLayout để chứa các view
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Tạo một EditText để nhập mã xác minh
        final EditText verifyCodeEditText = new EditText(this);

        // Tạo TextView để hiển thị thời gian đếm ngược
        final TextView countdownTextView = new TextView(this);
        countdownTextView.setText("Thời gian còn lại: 60s");  // Hiển thị thời gian ban đầu

        // Thêm EditText và TextView vào layout
        layout.addView(verifyCodeEditText);
        layout.addView(countdownTextView);

        // Tạo Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập Mã Xác Minh");
        builder.setView(layout);

        builder.setPositiveButton("Xác nhận", null);

        // Hiển thị Dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Thực hiện xác nhận mã khi nhấn nút xác nhận
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(v -> {
            String verifyCode = verifyCodeEditText.getText().toString().trim();
            if (!verifyCode.isEmpty()) {
                handleVerifyCode(email, verifyCode, dialog);  // Truyền dialog để đóng khi mã đúng
            } else {
                Toast.makeText(RegisterActivity.this, "Vui lòng nhập mã xác minh", Toast.LENGTH_SHORT).show();
            }
        });

        // Đếm ngược 60 giây
        startTimer(dialog, countdownTextView);
    }


    // Phương thức xử lý mã xác minh
    private void handleVerifyCode(String email, String verifyCode, AlertDialog dialog) {
        User u=db.checkVerify(email,verifyCode);
        if(u!=null){
            db.updateIsVerified(u.getId());
            dialog.dismiss();
            Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Mã xác minh không đúng! Vui lòng nhập lại.", Toast.LENGTH_SHORT).show();
        }
    }

    // Bắt đầu đếm ngược 60 giây
    private void startTimer(AlertDialog dialog, TextView countdownTextView) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int timeLeft = 60; 

            @Override
            public void run() {
                if (timeLeft > 0) {
                    // Giảm thời gian và cập nhật TextView
                    timeLeft--;
                    countdownTextView.setText("Thời gian còn lại: " + timeLeft + "s");

                    handler.postDelayed(this, 1000);  // Chạy lại sau 1 giây
                } else {
                    // Nếu hết thời gian, tự động đóng dialog và quay lại màn hình đăng ký
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Thời gian nhập mã xác minh đã hết. Đăng kí tài khoản thất bại", Toast.LENGTH_LONG).show();
                        usernameEditText.setText("");
                        emailEditText.setText("");
                        passwordEditText.setText("");
                        confirmPasswordEditText.setText("");
                    }
                }
            }
        };
        handler.post(runnable);  // Bắt đầu chạy
    }

}
