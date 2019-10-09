package com.danapps.polytech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.danapps.polytech.fragments.tabs.MenuFragment;
import com.danapps.polytech.fragments.tabs.NavigationChooseFragment;
import com.danapps.polytech.fragments.tabs.NavigationFragment;
import com.danapps.polytech.fragments.tabs.ScheduleFragment;
import com.danapps.polytech.fragments.tabs.SchemeFragment;
import com.danapps.polytech.fragments.tabs.NotesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm = getSupportFragmentManager();

    private final NotesFragment notesFragment = new NotesFragment();
    private final NavigationChooseFragment navigationChooseFragment = new NavigationChooseFragment();
    private final NavigationFragment navigationFragment = new NavigationFragment();
    private final ScheduleFragment scheduleFragment = new ScheduleFragment();
    private final SchemeFragment schemeFragment = new SchemeFragment();
    private final MenuFragment menuFragment = new MenuFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getBaseContext(), getString(R.string.welcome), Toast.LENGTH_SHORT).show();

        fm.beginTransaction().replace(R.id.frame_layout, notesFragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case (R.id.notes_item):
                        fm.beginTransaction().replace(R.id.frame_layout, notesFragment).commit();
                        break;

                    case (R.id.navigation_item):
                        fm.beginTransaction().replace(R.id.frame_layout, navigationChooseFragment).commit();
                        break;

                    case (R.id.schedule_item):
                        fm.beginTransaction().replace(R.id.frame_layout, scheduleFragment).commit();
                        break;

                    case (R.id.scheme_item):
                        fm.beginTransaction().replace(R.id.frame_layout, schemeFragment).commit();
                        break;

                    case (R.id.menu_item):
                        fm.beginTransaction().replace(R.id.frame_layout, menuFragment).commit();
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