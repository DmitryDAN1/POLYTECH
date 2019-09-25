package com.danapps.polytech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.danapps.polytech.BottomMenuFragments.EventFragment;
import com.danapps.polytech.BottomMenuFragments.MenuFragment;
import com.danapps.polytech.BottomMenuFragments.NavigationFragment;
import com.danapps.polytech.BottomMenuFragments.ScheduleFragment;
import com.danapps.polytech.BottomMenuFragments.SchemeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId())
                {
                    case (R.id.events_item):
                        fragmentTransaction.replace(R.id.frame_layout, new EventFragment()).commit();
                        break;

                    case (R.id.navigation_item):
                        fragmentTransaction.replace(R.id.frame_layout, new NavigationFragment()).commit();
                        break;

                    case (R.id.schedule_item):
                        fragmentTransaction.replace(R.id.frame_layout, new ScheduleFragment()).commit();
                        break;

                    case (R.id.scheme_item):
                        fragmentTransaction.replace(R.id.frame_layout, new SchemeFragment()).commit();
                        break;

                    case (R.id.menu_item):
                        fragmentTransaction.replace(R.id.frame_layout, new MenuFragment()).commit();
                        break;
                }

                return true;
            }
        });
    }
}