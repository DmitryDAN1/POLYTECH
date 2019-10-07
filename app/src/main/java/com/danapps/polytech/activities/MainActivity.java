package com.danapps.polytech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.danapps.polytech.fragments.tabs.MenuFragment;
import com.danapps.polytech.fragments.tabs.NavigationChooseFragment;
import com.danapps.polytech.fragments.tabs.NavigationFragment;
import com.danapps.polytech.fragments.tabs.ScheduleFragment;
import com.danapps.polytech.fragments.tabs.SchemeFragment;
import com.danapps.polytech.fragments.todo.HomeworkFragment;
import com.danapps.polytech.fragments.todo.NotesFragment;
import com.danapps.polytech.fragments.todo.TodoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm = getSupportFragmentManager();

    private final NotesFragment notesFragment = new NotesFragment();
    private final NavigationChooseFragment navigationChooseFragment = new NavigationChooseFragment();
    private final NavigationFragment navigationFragment = new NavigationFragment();
    private final ScheduleFragment scheduleFragment = new ScheduleFragment();
    private final SchemeFragment schemeFragment = new SchemeFragment();
    private final MenuFragment menuFragment = new MenuFragment();
    private final HomeworkFragment homeworkFragment = new HomeworkFragment();
    private final TodoFragment todoFragment = new TodoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getBaseContext(), getString(R.string.welcome), Toast.LENGTH_SHORT).show();

        BottomNavigationView topNavigationView = findViewById(R.id.notes_top_navigation_view);
        fm.beginTransaction().replace(R.id.frame_layout, notesFragment).commit();
        topNavigationView.setVisibility(View.VISIBLE);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case (R.id.notes_item):
                        fm.beginTransaction().replace(R.id.frame_layout, notesFragment).commit();
                        topNavigationView.setVisibility(View.VISIBLE);
                        break;

                    case (R.id.navigation_item):
                        fm.beginTransaction().replace(R.id.frame_layout, navigationChooseFragment).commit();
                        topNavigationView.setVisibility(View.GONE);
                        break;

                    case (R.id.schedule_item):
                        fm.beginTransaction().replace(R.id.frame_layout, scheduleFragment).commit();
                        topNavigationView.setVisibility(View.GONE);
                        break;

                    case (R.id.scheme_item):
                        fm.beginTransaction().replace(R.id.frame_layout, schemeFragment).commit();
                        topNavigationView.setVisibility(View.GONE);
                        break;

                    case (R.id.menu_item):
                        fm.beginTransaction().replace(R.id.frame_layout, menuFragment).commit();
                        topNavigationView.setVisibility(View.GONE);
                        break;
                }

                return true;
            }
        });

        topNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.notes_notes_item):
                        fm.beginTransaction().replace(R.id.frame_layout, notesFragment).commit();
                        break;
                    case (R.id.notes_homework_item):
                        fm.beginTransaction().replace(R.id.frame_layout, homeworkFragment).commit();
                        break;
                    case (R.id.notes_todo_item):
                        fm.beginTransaction().replace(R.id.frame_layout, todoFragment).commit();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.exit_from_app_mainText)).setMessage(getString(R.string.exit_from_app_subText))
                .setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), getString(R.string.good_choice), Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}