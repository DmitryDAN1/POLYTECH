package com.danapps.polytech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.danapps.polytech.fragments.tabs.EventFragment;
import com.danapps.polytech.fragments.tabs.MenuFragment;
import com.danapps.polytech.fragments.tabs.NavigationFragment;
import com.danapps.polytech.fragments.tabs.ScheduleFragment;
import com.danapps.polytech.fragments.tabs.SchemeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm = getSupportFragmentManager();

    private final EventFragment eventFragment = new EventFragment();
    private final NavigationFragment navigationFragment = new NavigationFragment();
    private final ScheduleFragment scheduleFragment = new ScheduleFragment();
    private final SchemeFragment schemeFragment = new SchemeFragment();
    private final MenuFragment menuFragment = new MenuFragment();
    private Fragment currentFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getBaseContext(), "Добро пожаловать!", Toast.LENGTH_SHORT).show();

        fm.beginTransaction().add(R.id.frame_layout, navigationFragment, "2").hide(navigationFragment).commit();
        fm.beginTransaction().add(R.id.frame_layout, scheduleFragment, "3").hide(scheduleFragment).commit();
        fm.beginTransaction().add(R.id.frame_layout, schemeFragment, "4").hide(schemeFragment).commit();
        fm.beginTransaction().add(R.id.frame_layout, menuFragment, "5").hide(menuFragment).commit();
        fm.beginTransaction().add(R.id.frame_layout, eventFragment, "1").commit();
        currentFragment = eventFragment;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case (R.id.events_item):
                        fm.beginTransaction().hide(currentFragment).show(eventFragment).commit();
                        currentFragment = eventFragment;
                        break;

                    case (R.id.navigation_item):
                        fm.beginTransaction().hide(currentFragment).show(navigationFragment).commit();
                        currentFragment = navigationFragment;
                    break;

                    case (R.id.schedule_item):
                        fm.beginTransaction().hide(currentFragment).show(scheduleFragment).commit();
                        currentFragment = scheduleFragment;
                        break;

                    case (R.id.scheme_item):
                        fm.beginTransaction().hide(currentFragment).show(schemeFragment).commit();
                        currentFragment = schemeFragment;
                        break;

                    case (R.id.menu_item):
                        fm.beginTransaction().hide(currentFragment).show(menuFragment).commit();
                        currentFragment = menuFragment  ;
                        break;
                }

                return true;
            }
        });
    }
}