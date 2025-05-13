package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextNoteTitle, editTextNoteContent;
    private ImageButton buttonSaveNote, buttonBack;
    private boolean isEditing = false;
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextNoteTitle = findViewById(R.id.editTextNoteTitle);
        editTextNoteContent = findViewById(R.id.editTextNoteContent);

        buttonSaveNote = findViewById(R.id.buttonSaveNote);

        buttonBack = findViewById(R.id.buttonBack);

        // Thiết lập sự kiện click cho nút quay lại
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kết thúc Activity hiện tại (quay lại Activity trước đó)
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        isEditing = intent.getBooleanExtra("is_editing", false);

        if (isEditing) {
            noteId = intent.getIntExtra("note_id", -1);
            String title = intent.getStringExtra("note_title");
            String content = intent.getStringExtra("note_content");

            editTextNoteTitle.setText(title);
            editTextNoteContent.setText(content);
        }

        buttonSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextNoteTitle.getText().toString().trim();
                String content = editTextNoteContent.getText().toString().trim();

                if (!title.isEmpty() && !content.isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("note_title", title);
                    resultIntent.putExtra("note_content", content);

                    if (isEditing) {
                        resultIntent.putExtra("is_editing", true);
                        resultIntent.putExtra("note_id", noteId);
                    }

                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}