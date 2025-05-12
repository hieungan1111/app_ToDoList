package com.example.todolist.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todolist.R;
import com.example.todolist.dialog.CustomBottomSheet;

public class FragmentCalender extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calender,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo CustomBottomSheet
        if (getActivity() != null && getContext() != null) {

            final CustomBottomSheet bottomSheet = new CustomBottomSheet(getActivity(), getContext());

            bottomSheet.showBottomSheet("This is a custom Bottom Sheet message.");
        }

    }
}
