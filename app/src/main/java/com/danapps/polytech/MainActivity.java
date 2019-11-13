package com.danapps.polytech;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.danapps.polytech.fragments.tabs.AboutFragment;
import com.danapps.polytech.fragments.tabs.ChangeEmailFragment;
import com.danapps.polytech.fragments.tabs.ChangeFacultyFragment;
import com.danapps.polytech.fragments.tabs.ChangeGroupFragment;
import com.danapps.polytech.fragments.tabs.ChangeNameFragment;
import com.danapps.polytech.fragments.tabs.ChangePassFragment;
import com.danapps.polytech.fragments.tabs.CopyrightIconsFragment;
import com.danapps.polytech.fragments.tabs.LicenseFragment;
import com.danapps.polytech.fragments.tabs.LinksFragment;
import com.danapps.polytech.fragments.tabs.MainAuthFragment;
import com.danapps.polytech.fragments.tabs.MenuFragment;
import com.danapps.polytech.fragments.tabs.NavigationChooseFragment;
import com.danapps.polytech.fragments.tabs.NotesFragment;
import com.danapps.polytech.fragments.tabs.RegisterEmailFragment;
import com.danapps.polytech.fragments.tabs.RegisterFinishFragment;
import com.danapps.polytech.fragments.tabs.RegisterPassFragment;
import com.danapps.polytech.fragments.tabs.ResetPassEmailFragment;
import com.danapps.polytech.fragments.tabs.ResetPassFinishFragment;
import com.danapps.polytech.fragments.tabs.ScheduleFragment;
import com.danapps.polytech.fragments.tabs.SchemeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends FragmentActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FragmentManager fm = getSupportFragmentManager();
    private int currentFragment;
    BottomNavigationView bottomNavigationView;

    private Fragment[] fragments = {
            new NotesFragment(),                        // 0
            new NavigationChooseFragment(),             // 1
            new ScheduleFragment(),                     // 2
            new SchemeFragment(),                       // 3
            new MenuFragment(),                         // 4
            new ChangeGroupFragment(),                  // 5
            new ChangeFacultyFragment(),                // 6
            new MainAuthFragment(),                     // 7
            new RegisterEmailFragment(),                // 8
            new RegisterPassFragment(),                 // 9
            new RegisterFinishFragment(),               // 10
            new ResetPassEmailFragment(),               // 11
            new ResetPassFinishFragment(),              // 12
            new ChangeNameFragment(),                   // 13
            new AboutFragment(),                        // 14
            new LicenseFragment(),                      // 15
            new ChangePassFragment(),                   // 16
            new ChangeEmailFragment(),                  // 17
            new LinksFragment(),                        // 18
            new CopyrightIconsFragment()                // 19
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sPref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_main, null, true);
        setContentView(view);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        SharedPreferences tPref = getSharedPreferences("TimedInfo", MODE_PRIVATE);
        tPref.edit().clear().apply();
//        sPref.edit().clear().apply();
//        mAuth.signOut();
        if (sPref.getInt("UserGroupId", 0) != 0)
            loadFragment(2);
        else
            loadFragment(6);
        bottomNavigationView.setSelectedItemId(R.id.schedule_item);
        Log.e("TimedInfo:", tPref.getAll().toString());
        Log.e("UserInfo:", sPref.getAll().toString());

        if (mAuth.getCurrentUser() != null) {
            Log.e("FirebaseAuth", mAuth.getCurrentUser().getEmail());
        }

        if (mAuth.getCurrentUser() != null) {
            DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid());

            myRef.child("UserInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("UserGroupId").getValue() != null)
                        sPref.edit().putInt("UserGroupId", Integer.valueOf(dataSnapshot.child("UserGroupId").getValue().toString())).apply();
                    if (dataSnapshot.child("UserGroupName").getValue() != null)
                        sPref.edit().putString("UserGroupName", dataSnapshot.child("UserGroupName").getValue().toString()).apply();

                    if (dataSnapshot.child("UserName").getValue() != null)
                        sPref.edit().putString("UserName", dataSnapshot.child("UserName").getValue().toString()).apply();
                    if (dataSnapshot.child("UserSurname").getValue() != null)
                        sPref.edit().putString("UserSurname", dataSnapshot.child("UserSurname").getValue().toString()).apply();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {
                case (R.id.notes_item):
                    loadFragment(0);
                    break;

                case (R.id.navigation_item):
                    loadFragment(1);
                    break;

                case (R.id.schedule_item):
                    if (sPref.getInt("UserGroupId", 0) != 0)
                        loadFragment(2);
                    else
                        loadFragment(6);
                    break;

                case (R.id.scheme_item):
                    loadFragment(3);
                    break;

                case (R.id.menu_item):
                    loadFragment(4);
                    break;
            }

            return true;
        });
    }

    @Override
    public void onBackPressed() {
        switch (currentFragment) {

            case 5:     // ChangeGroupFragment
                loadFragment(6);
                break;

            case 7:     // MainAuthFragment
            case 13:    // ChangeNameFragment
            case 14:    // AboutFragment
            case 16:    // ChangePassFragment
            case 17:    // ChangeEmailFragment
            case 18:    // LinksFragment
                loadFragment(4);
                break;

            case 8:     // RegisterEmailFragment
            case 10:    // RegisterFinishFragment
            case 11:    // ResetPassEmailFragment
            case 12:    // ResetPassFinishFragment
                loadFragment(7);
                break;

            case 9:     // RegisterPassFragment
                loadFragment(8);
                break;

            case 15:    // LicenseFragment
                loadFragment(14);
                break;

            case 3:     // SchemeFragment TODO: THAT WILL NOT SCHEME
            case 6:     // ChangeFacultyFragment
                if (bottomNavigationView.getSelectedItemId() == R.id.menu_item)
                    loadFragment(4);    // MenuFragment
                else
                    showExitDialog();
                break;

            case 19:
                loadFragment(15);   // LicenseFragment
                break;

            default:
                if (getSupportFragmentManager().findFragmentByTag("NoteEditFragment") != null)
                    loadFragment(0);
                else if (getSupportFragmentManager().findFragmentByTag("NavigationFragment") != null)
                    loadFragment(1);
                else
                    showExitDialog();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        finishAffinity();
        super.onDestroy();
    }

    private void showExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.exit_from_app_mainText)).setMessage(getString(R.string.exit_from_app_subText))
                .setNegativeButton(getString(R.string.No), (dialogInterface, i) -> Toast.makeText(getApplicationContext(), getString(R.string.good_choice), Toast.LENGTH_SHORT).show())
                .setPositiveButton(getString(R.string.Yes), (dialogInterface, i) -> finishAffinity());
        builder.create().show();
    }

    public void loadFragment(int id) {
        currentFragment = id;
        if (id == 5)
            fm.beginTransaction().replace(R.id.frame_layout, new ChangeGroupFragment()).commit();
        else
            fm.beginTransaction().replace(R.id.frame_layout, fragments[currentFragment], String.valueOf(currentFragment)).commit();
        Log.e("Fragment", "loadFragment:" + id);
    }
}