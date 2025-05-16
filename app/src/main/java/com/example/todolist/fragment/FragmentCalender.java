    package com.example.todolist.fragment;
    
    import static android.content.Context.MODE_PRIVATE;

    import android.app.DatePickerDialog;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.ImageButton;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;
    
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    
    import com.example.todolist.R;
    import com.example.todolist.adapter.DeadlineRecycleViewAdapter;
    import com.example.todolist.dao.impl.SQLiteDeadlineDAO;
    import com.example.todolist.dao.impl.SQLiteSubjectDAO;
    import com.example.todolist.dialog.CustomBottomSheet;
    import com.example.todolist.dialog.DeadlineAddDialog;
    import com.example.todolist.dialog.DeadlineEditDialog;
    import com.example.todolist.model.Deadline;
    import com.example.todolist.model.Subject;
    import com.google.android.material.floatingactionbutton.FloatingActionButton;

    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.List;
    import java.util.Locale;

    public class FragmentCalender extends Fragment {
        private RecyclerView recyclerView;
        private DeadlineRecycleViewAdapter adapter;
        private List<Deadline> deadlineList;
        private FloatingActionButton btnAddDeadline;
        private Spinner subjectSpinner;
        private Spinner statusSpinner;
        private TextView deadlineDateText;
        private ImageButton calendarButton;
        private List<Subject> subjectList;
        private ArrayAdapter<String> subjectSpinnerAdapter;
        private ArrayAdapter<String> statusSpinnerAdapter;
        private List<String> subjectNames;
        private String selectedDate;
        private Integer selectedStatus; // null = Tất cả, 1 = Hoàn thành, 0 = Chưa hoàn thành

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

            // Khởi tạo Spinner cho danh sách môn học
            subjectSpinner = view.findViewById(R.id.subject_spinner);
            subjectList = new ArrayList<>();
            subjectNames = new ArrayList<>();
            subjectNames.add("Tất cả");
            subjectSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subjectNames);
            subjectSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subjectSpinner.setAdapter(subjectSpinnerAdapter);
            loadSubjects();

            // Khởi tạo Spinner cho trạng thái
            statusSpinner = view.findViewById(R.id.status_spinner);
            List<String> statusNames = new ArrayList<>();
            statusNames.add("Tất cả");
            statusNames.add("Hoàn thành");
            statusNames.add("Chưa hoàn thành");
            statusSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, statusNames);
            statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            statusSpinner.setAdapter(statusSpinnerAdapter);

            // Khởi tạo TextView và ImageButton cho chọn ngày
            deadlineDateText = view.findViewById(R.id.deadline_date);
            calendarButton = view.findViewById(R.id.deadline_calendar_button);
            calendarButton.setOnClickListener(v -> showDatePicker());

            // Bắt sự kiện chọn môn học
            subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    refreshDeadlineList();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Không làm gì
                }
            });

            // Bắt sự kiện chọn trạng thái
            statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedStatus = position == 0 ? null : (position == 1 ? 1 : 0);
                    refreshDeadlineList();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Không làm gì
                }
            });

            // Khởi tạo FloatingActionButton
            btnAddDeadline = view.findViewById(R.id.deadline_btn_add);
            btnAddDeadline.setOnClickListener(v -> {
                DeadlineAddDialog.showCustomDialog(getContext(), this::refreshDeadlineList);
            });

            // Hiển thị CustomBottomSheet cho danh sách môn học
            if (getActivity() != null && getContext() != null) {
                final CustomBottomSheet bottomSheet = new CustomBottomSheet(getActivity(), getContext());
                bottomSheet.showBottomSheet("Danh sách môn học theo ngày");
            }

            // Làm mới danh sách deadline
            refreshDeadlineList();
        }

        private void loadSubjects() {
            SQLiteSubjectDAO subjectDAO = new SQLiteSubjectDAO(getContext());
            SharedPreferences prefs = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int userId = prefs.getInt("userId", -1);
            List<Subject> subjects = subjectDAO.getAllSubjectsByUserId(userId);
            if (subjects != null) {
                subjectList.clear();
                subjectList.addAll(subjects);
                subjectNames.clear();
                subjectNames.add("Tất cả");
                for (Subject subject : subjects) {
                    subjectNames.add(subject.getSubjectName());
                }
                subjectSpinnerAdapter.notifyDataSetChanged();
            }
        }

        private void showDatePicker() {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        Calendar selected = Calendar.getInstance();
                        selected.set(selectedYear, selectedMonth, selectedDay);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        selectedDate = sdf.format(selected.getTime());
                        deadlineDateText.setText(String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear));
                        refreshDeadlineList();
                    },
                    year, month, day
            );
            datePickerDialog.show();
        }

        private void refreshDeadlineList() {
            SQLiteDeadlineDAO deadlineDAO = new SQLiteDeadlineDAO(getContext());
            deadlineList.clear();
            Integer subjectId = subjectSpinner.getSelectedItemPosition() == 0 ? null : subjectList.get(subjectSpinner.getSelectedItemPosition() - 1).getId();
            SharedPreferences prefs = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int userId = prefs.getInt("userId", -1);
            List<Deadline> updatedList = deadlineDAO.getDeadlinesByFilters(userId, subjectId, selectedDate, selectedStatus);
            if (updatedList != null) {
                deadlineList.addAll(updatedList);
            }
            adapter.notifyDataSetChanged();
        }
    }