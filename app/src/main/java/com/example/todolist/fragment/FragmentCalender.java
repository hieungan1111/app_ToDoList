package com.example.todolist.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.adapter.DeadlineRecycleViewAdapter;
import com.example.todolist.dao.impl.SQLiteDeadlineDAO;
import com.example.todolist.dialog.CustomBottomSheet;
import com.example.todolist.dialog.DeadlineAddDialog;
import com.example.todolist.dialog.DeadlineEditDialog;
import com.example.todolist.model.Deadline;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentCalender extends Fragment {
    private RecyclerView recyclerView;
    private DeadlineRecycleViewAdapter adapter;
    private List<Deadline> deadlineList;
    private FloatingActionButton btnAddDeadline;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calender, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo RecyclerView cho danh sách deadline
        recyclerView = view.findViewById(R.id.deadline_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        deadlineList = new ArrayList<>();
        adapter = new DeadlineRecycleViewAdapter(deadlineList, new DeadlineRecycleViewAdapter.OnDeadlineActionListener() {
            @Override
            public void onEditDeadline(Deadline deadline, int position) {
                // Xử lý chỉnh sửa deadline (có thể mở dialog chỉnh sửa)
                DeadlineEditDialog.showEditDialog(getContext(), 1, deadline, () -> refreshDeadlineList());
            }

            @Override
            public void onDeleteDeadline(int deadlineId, int position) {
                SQLiteDeadlineDAO deadlineDAO = new SQLiteDeadlineDAO(getContext());
                deadlineDAO.deleteDeadline(deadlineId);
                adapter.removeItem(position);
            }

            @Override
            public void onStatusChanged(Deadline deadline, boolean isCompleted, int position) {
                SQLiteDeadlineDAO deadlineDAO = new SQLiteDeadlineDAO(getContext());
                deadlineDAO.updateStatus(deadline.getId(), isCompleted);
                adapter.updateItem(position, deadline);
            }
        });
        recyclerView.setAdapter(adapter);

        // Khởi tạo FloatingActionButton
        btnAddDeadline = view.findViewById(R.id.deadline_btn_add);
        btnAddDeadline.setOnClickListener(v -> {
            DeadlineAddDialog.showCustomDialog(getContext(),  this::refreshDeadlineList);
        });

        // Hiển thị CustomBottomSheet cho danh sách môn học
        if (getActivity() != null && getContext() != null) {
            final CustomBottomSheet bottomSheet = new CustomBottomSheet(getActivity(), getContext());
            bottomSheet.showBottomSheet("Danh sách môn học theo ngày");
        }

        // Làm mới danh sách deadline
        refreshDeadlineList();
    }

    private void refreshDeadlineList() {
        SQLiteDeadlineDAO deadlineDAO = new SQLiteDeadlineDAO(getContext());
        deadlineList.clear();
        List<Deadline> updatedList = deadlineDAO.getAllDeadlinesByUserId(1);
        if (updatedList != null) {
            deadlineList.addAll(updatedList);
        }
        adapter.notifyDataSetChanged();
    }
}