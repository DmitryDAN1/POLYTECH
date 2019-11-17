package com.danapps.polytech;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
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
import androidx.fragment.app.FragmentTransaction;

import com.danapps.polytech.fragments.tabs.AboutFragment;
import com.danapps.polytech.fragments.tabs.ChangeEmailFragment;
import com.danapps.polytech.fragments.tabs.ChangeFacultyFragment;
import com.danapps.polytech.fragments.tabs.ChangeGroupFragment;
import com.danapps.polytech.fragments.tabs.ChangeNameFragment;
import com.danapps.polytech.fragments.tabs.ChangePassFragment;
import com.danapps.polytech.fragments.tabs.ChooseGroupFragment;
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

import java.util.List;

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
            new CopyrightIconsFragment(),               // 19
            new ChooseGroupFragment()                   // 20
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences tPref = getSharedPreferences("TimedInfo", MODE_PRIVATE);
        SharedPreferences sPref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences gPref = getSharedPreferences("GroupsInfo", MODE_PRIVATE);
        tPref.edit().clear().apply();
//        sPref.edit().clear().apply();
//        gPref.edit().clear().apply();
//        FirebaseAuth.getInstance().signOut();
        sPref.edit().putInt("AppVersion", getResources().getInteger(R.integer.app_version)).apply();
        Log.e("TimedInfo:", tPref.getAll().toString());
        Log.e("UserInfo:", sPref.getAll().toString());
        Log.e("GroupsInfo:", gPref.getAll().toString());

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_main, null, true);
        setContentView(view);
        checkUpdate(sPref);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.schedule_item);

        if (gPref.getInt("GroupsCount", 0) != 0) {
            loadFragment(2);
        }
        else {
            loadFragment(6);
        }


        if (mAuth.getCurrentUser() != null) {
            Log.e("FirebaseAuth", mAuth.getCurrentUser().getEmail());
        }

        if (mAuth.getCurrentUser() != null) {
            DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid());

            myRef.child("UserInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                    if (gPref.getInt("GroupsCount", 0) != 0)
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
        SharedPreferences gPref = getSharedPreferences("GroupsInfo", MODE_PRIVATE);
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

            case 20:

                if (gPref.getInt("GroupsCount", 0) != 0)
                    loadFragment(2);
                else
                    loadFragment(6);
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

    public void updateFragment(int id) {
        switch (id) {
            case 2:
                fragments[id] = new ScheduleFragment();
                break;
        }
    }

    public void loadFragment(int id) {
        currentFragment = id;
        if (id == 5)
            fm.beginTransaction().replace(R.id.frame_layout, new ChangeGroupFragment()).commit();
        else
            fm.beginTransaction()
                    .replace(R.id.frame_layout, fragments[currentFragment], String.valueOf(currentFragment))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        Log.e("Fragment", "loadFragment:" + id);
    }

    private void checkUpdate(SharedPreferences sPref){
        DatabaseReference myRef = database.getReference("AppInfo");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (Integer.valueOf(dataSnapshot.child("AppVersion").getValue().toString()) > sPref.getInt("AppVersion", 0)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Доступно новое обновление!")
                            .setMessage(dataSnapshot.child("AppUpdates").getValue().toString().replaceAll("\\n", "\n"))
                            .setPositiveButton("Обновить", (dialog, which) ->
                                    openLink(MainActivity.this, "https://play.google.com/store/apps/details?id=com.danapps.polytech"))
                            .setNegativeButton("Позже", (dialog, which) -> dialog.dismiss())
                            .create()
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openLink(Activity activity, String url) {
        final String VK_APP_PACKAGE_ID = getString(R.string.VK_APP_PACKAGE_ID);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(intent, 0);

        if (resInfo.isEmpty())
            return;

        for (ResolveInfo info: resInfo) {
            if (info.activityInfo == null)
                continue;
            if (VK_APP_PACKAGE_ID.equals(info.activityInfo.packageName)) {
                intent.setPackage(info.activityInfo.packageName);
                break;
            }
        }

        activity.startActivity(intent);
    }
}