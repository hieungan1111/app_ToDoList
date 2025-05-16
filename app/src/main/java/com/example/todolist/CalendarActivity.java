// CalendarActivity.java
package com.example.todolist;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todolist.fragment.FragmentCalender;

public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar); // file XML chứa FrameLayout để đặt Fragment

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FragmentCalender())
                    .commit();
        }
    }
}
