package com.example.todolist.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todolist.ChangePasswordActivity;
import com.example.todolist.EditProfileActivity;
import com.example.todolist.LoginActivity;
import com.example.todolist.R;
import com.example.todolist.dao.impl.SQLiteUserDAO;
import com.example.todolist.model.User;

public class FragmentProfile extends Fragment {
    private ActivityResultLauncher<Intent> editProfileLauncher;
    int userId=-1;
    ImageView profile_image;
    TextView username,logout;
    Button edit_button;
    LinearLayout personalInfo,change_password;
    SQLiteUserDAO db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profile_image=view.findViewById(R.id.profile_image);
        username=view.findViewById(R.id.username);
        edit_button=view.findViewById(R.id.edit_button);
        logout=view.findViewById(R.id.logout_button);
        db=new SQLiteUserDAO(requireContext());

        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getBooleanExtra("isUpdated", false)) {
                            loadUserData();
                        }
                    }
                }
        );

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1); // -1 là mặc định nếu không có
        if (userId != -1) {
            loadUserData();
        } else {
            Log.d("FragmentProfile", "Chưa đăng nhập hoặc userId không tồn tại");
        }

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), EditProfileActivity.class);
                editProfileLauncher.launch(intent);
            }
        });

        personalInfo = view.findViewById(R.id.personal_info);
        personalInfo.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            editProfileLauncher.launch(intent);
        });

        change_password= view.findViewById(R.id.change_password);
        change_password.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("userId");
                editor.apply();
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public void loadUserData(){
        User u=db.getUserById(userId);
        System.out.println(u);
        username.setText(u.getFullname());
        if(u.getAvatarUrl()==null|| u.getAvatarUrl().isEmpty()){
            profile_image.setImageResource(R.drawable.applogo);
        }else{
            try {
                Uri imageUri = Uri.parse(u.getAvatarUrl());
                profile_image.setImageURI(imageUri);
            } catch (Exception e) {
                profile_image.setImageResource(R.drawable.applogo);
                e.printStackTrace();
            }
        }
    }
}
