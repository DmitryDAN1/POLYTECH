package com.danapps.polytech.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.danapps.polytech.fragments.tabs.ChangeGroupFragment;
import com.danapps.polytech.fragments.tabs.MenuFragment;
import com.danapps.polytech.fragments.tabs.NavigationChooseFragment;
import com.danapps.polytech.fragments.tabs.ScheduleFragment;
import com.danapps.polytech.fragments.tabs.SchemeFragment;
import com.danapps.polytech.fragments.tabs.NotesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends FragmentActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FragmentManager fm = getSupportFragmentManager();

    private int currentFragment;
    private Fragment[] fragments = {
            new NotesFragment(),
            new NavigationChooseFragment(),
            new ScheduleFragment(),
            new SchemeFragment(),
            new MenuFragment(),
            new ChangeGroupFragment(),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_main, null, true);
        setContentView(view);
        SharedPreferences sPref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        LoadFragment(0);

        if (mAuth.getCurrentUser() == null) {
            if (!sPref.getString("UserLogin", "0").equals("0") && !sPref.getString("UserPass", "0").equals("0")) {
                mAuth.signInWithEmailAndPassword(sPref.getString("UserLogin", "0"),
                        sPref.getString("UserPass", "0"))
                        .addOnSuccessListener(authResult -> Snackbar.make(view, "Вы успешно авторизовались!", Snackbar.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Snackbar.make(view, "Неудачная попытка авторизации!", Snackbar.LENGTH_SHORT).show());
            }
            else
                Snackbar.make(view, "Вы не авторизованы!", Snackbar.LENGTH_LONG).show();
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {
                case (R.id.notes_item):
                    LoadFragment(0);
                    break;

                case (R.id.navigation_item):
                    LoadFragment(1);
                    break;

                case (R.id.schedule_item):
                    if (sPref.getInt("UserGroupId", 0) != 0) {
                        LoadFragment(2);
                    }
                    else {
                        LoadFragment(5);
                    }
                    break;

                case (R.id.scheme_item):
                    LoadFragment(3);
                    break;

                case (R.id.menu_item):
                    LoadFragment(4);
                    break;
            }

            return true;
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.exit_from_app_mainText)).setMessage(getString(R.string.exit_from_app_subText))
                .setNegativeButton(getString(R.string.No), (dialogInterface, i) -> Toast.makeText(getApplicationContext(), getString(R.string.good_choice), Toast.LENGTH_SHORT).show())
                .setPositiveButton(getString(R.string.Yes), (dialogInterface, i) -> finishAffinity());
        builder.create().show();
    }

    public void LoadFragment(int ID) {
        currentFragment = ID;
        fm.beginTransaction().replace(R.id.frame_layout, fragments[currentFragment], String.valueOf(currentFragment)).commit();
    }
}