package com.example.todolist;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.todolist.dao.impl.SQLiteUserDAO;
import com.example.todolist.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    ImageButton btn_back;
    ImageView profile_image;
    EditText efullName,eDate;
    TextView eGenre, tvEmail;
    Button updateButton;
    SQLiteUserDAO db;
    FrameLayout camera;
    ImageView canadar;
    Spinner sp;
    int userId=-1;
    Uri imageUri;
    User u=new User();
    List<String> genderList = Arrays.asList("Nam", "Nữ");
    String imageUriString="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());
        init();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] options = {"Chụp ảnh", "Chọn từ thư viện"};

                new AlertDialog.Builder(EditProfileActivity.this)
                        .setTitle("Chọn ảnh")
                        .setItems(options, (dialog, which) -> {
                            if (which == 0) {
                                // Chụp ảnh
                                requestCameraPermissionIfNeeded();
                            } else {
                                // Chọn ảnh
                                openGallery();
                            }
                        })
                        .show();
            }
        });

        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        canadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        eGenre.setOnClickListener(v -> sp.performClick());
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = genderList.get(position);
                eGenre.setText(selectedGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName=efullName.getText().toString().trim();
                if(validateFields(fullName)){
                    u.setAvatarUrl(imageUriString);
                    u.setFullname(efullName.getText().toString());
                    u.setBirthday(eDate.getText().toString());
                    u.setGender(eGenre.getText().toString());
                    boolean check=db.updateUser(u);
                    if(check){
                        Toast.makeText(EditProfileActivity.this,"Cập nhật thành công",Toast.LENGTH_LONG).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("isUpdated", true);
                        setResult(RESULT_OK, resultIntent);
                        finish();

                    }else{
                        Toast.makeText(EditProfileActivity.this,"Cập nhật thất bại",Toast.LENGTH_LONG).show();
                        init();
                    }
                }
            }
        });
    }
    public void init(){
        profile_image=findViewById(R.id.profile_image);
        efullName=findViewById(R.id.et_full_name);
        tvEmail=findViewById(R.id.et_email);
        eDate=findViewById(R.id.et_date_of_birth);
        eGenre=findViewById(R.id.tv_gender);
        updateButton=findViewById(R.id.btn_update);
        camera=findViewById(R.id.camera_button);
        canadar=findViewById(R.id.calendar_icon);
        sp=findViewById(R.id.spinner_gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                genderList
        );
        sp.setAdapter(adapter);
        db=new SQLiteUserDAO(this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1); // -1 là mặc định nếu không có

        if (userId != -1) {
            u=db.getUserById(userId);
            efullName.setText(u.getFullname());
            tvEmail.setText(u.getEmail());
            if(u.getAvatarUrl()==null|| u.getAvatarUrl().isEmpty()){
                profile_image.setImageResource(R.drawable.applogo);
            }else{
                try {
                    Uri imageUri1 = Uri.parse(u.getAvatarUrl());
                    profile_image.setImageURI(imageUri1);
                    imageUriString = u.getAvatarUrl();
                } catch (Exception e) {
                    profile_image.setImageResource(R.drawable.applogo);
                    e.printStackTrace();
                }
            }
            if(u.getBirthday()!=null){
                eDate.setText(u.getBirthday());
            }
            if(u.getGender()!=null){
                eGenre.setText(u.getGender());
                int index = genderList.indexOf(u.getGender());
                if (index != -1) {
                    sp.setSelection(index);
                }
            }else{
                sp.setSelection(0);
            }
        } else {
            Log.d("EditProfile", "Chưa đăng nhập hoặc userId không tồn tại");
        }
    }
    public void showDateDialog(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog=new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                eDate.setText(""+d+"/"+m+"/"+y);
            }
        },year, month, day);
        dialog.show();
    }
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;

    private void requestCameraPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền Camera để chụp ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");

        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, 100);
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) { // Chụp ảnh
                profile_image.setImageURI(imageUri);
                imageUriString=imageUri.toString();
            } else if (requestCode == 101) { // Chọn ảnh
                Uri selectedImage = data.getData();
                try {
                    // Mở input stream từ ảnh được chọn
                    InputStream inputStream = getContentResolver().openInputStream(selectedImage);

                    // Tạo file mới trong thư mục cache của app
                    File file = new File(getCacheDir(), "avatar_" + System.currentTimeMillis() + ".jpg");
                    FileOutputStream outputStream = new FileOutputStream(file);

                    // Copy ảnh sang file mới
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }

                    outputStream.close();
                    inputStream.close();

                    // Lấy URI từ file mới
                    Uri safeUri = FileProvider.getUriForFile(
                            this,
                            getPackageName() + ".provider",
                            file
                    );

                    // Gán ảnh và lưu URI
                    profile_image.setImageURI(safeUri);
                    imageUriString = safeUri.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public boolean validateFields(String fullName) {
        // Kiểm tra email
        if (fullName.isEmpty()) {
            efullName.setError("Tên không được để trống");
            return false;
        }
        return true;
    }
}