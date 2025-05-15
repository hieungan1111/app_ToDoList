package com.example.todolist.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.todolist.DemActivity;
import com.example.todolist.AlarmActivity;
import com.example.todolist.NoteActivity;
import com.example.todolist.R;
import com.example.todolist.dao.impl.SQLiteUserDAO;
import com.example.todolist.model.User;

public class FragmentHome extends Fragment {
    int userId=-1;
    SQLiteUserDAO db;
    CardView cardCountdown;

    CardView alarmButton, noteButton;

    TextView username_text;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username_text=view.findViewById(R.id.username_text);
        db=new SQLiteUserDAO(requireContext());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1); // -1 là mặc định nếu không có
        if (userId != -1) {
            User u=db.getUserById(userId);
            username_text.setText(u.getFullname());
        } else {
            Log.d("FragmentHome", "Chưa đăng nhập hoặc userId không tồn tại");
        }

        cardCountdown=view.findViewById(R.id.cardCountdown);
        cardCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(requireContext(), DemActivity.class);
                startActivity(intent);
            }
        });

        alarmButton = view.findViewById(R.id.alarm_card);
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang AddAlarmActivity
                Intent intent = new Intent(getActivity(), AlarmActivity.class);
                startActivity(intent);
            }
        });

        noteButton = view.findViewById(R.id.cardNote);
        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                startActivity(intent);
            }
        });

    }
}
