package com.example.todolist.dao;

import com.example.todolist.model.Note;
import java.util.List;

public interface NoteDAO {
    void addNote(Note note);
    Note getNoteById(int id);
    List<Note> getAllNotesByUserId(int userId);
    void updateNote(Note note);
    void deleteNote(int id);
    List<Note> getNotesByCategoryId(String categoryId, int userId);

    public List<Note> getAllNotes();
}
