package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.adapter.NoteAdapter;
import com.example.todolist.dao.impl.SQLiteNoteDAO;
import com.example.todolist.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_NOTE = 1;

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddNote;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private SQLiteNoteDAO dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        dbHelper = new SQLiteNoteDAO(this);

        // Lấy tất cả các ghi chú từ cơ sở dữ liệu
        noteList = dbHelper.getAllNotes();
        noteAdapter = new NoteAdapter(noteList, new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(NoteActivity.this, AddNoteActivity.class);

                intent.putExtra("is_editing", true);
                intent.putExtra("note_id", note.getId());
                intent.putExtra("note_title", note.getTitle());
                intent.putExtra("note_content", note.getContent());
                startActivityForResult(intent, REQUEST_ADD_NOTE);

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onNoteLongClick(Note note) {
                // Khi giữ lâu để xóa
                new AlertDialog.Builder(NoteActivity.this)
                        .setTitle("Xóa ghi chú")
                        .setMessage("Bạn có chắc chắn muốn xóa ghi chú này không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            dbHelper.deleteNote(note.getId()); // Xóa khỏi DB
                            noteList.remove(note);             // Xóa khỏi danh sách
                            noteAdapter.notifyDataSetChanged();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        recyclerView.setAdapter(noteAdapter);

        fabAddNote = findViewById(R.id.fabAddNote);
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, REQUEST_ADD_NOTE);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_NOTE && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("note_title");
            String content = data.getStringExtra("note_content");
            boolean isEditing = data.getBooleanExtra("is_editing", false);

            if (isEditing) {
                int noteId = data.getIntExtra("note_id", -1);
                if (noteId != -1) {
                    Note updatedNote = dbHelper.getNoteById(noteId); // Viết hàm này
                    updatedNote.setTitle(title);
                    updatedNote.setContent(content);
                    updatedNote.setUpdateAt(new Date());
                    dbHelper.updateNote(updatedNote);
                }
            } else {
                Note note = new Note();
                note.setTitle(title);
                note.setContent(content);
                note.setUserId(1);
                note.setCreateAt(new Date());
                note.setUpdateAt(new Date());
                note.setCategoryId(null);
                note.setThemeColor(null);
                note.setFontColor(null);
                note.setFontSize(20);
                note.setHidden(false);

                // Lưu ghi chú vào cơ sở dữ liệu
                dbHelper.addNote(note);
            }

            // Lấy lại danh sách ghi chú từ cơ sở dữ liệu để cập nhật RecyclerView
            noteList.clear();
            noteList.addAll(dbHelper.getAllNotes());

            // Cập nhật adapter để hiển thị ghi chú mới
            noteAdapter.notifyDataSetChanged();
        }
    }
}