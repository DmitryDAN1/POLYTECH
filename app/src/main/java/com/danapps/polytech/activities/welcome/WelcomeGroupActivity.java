package com.danapps.polytech.activities.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.MainActivity;
import com.danapps.polytech.schedule.Faculties;
import com.danapps.polytech.schedule.Groups;
import com.danapps.polytech.schedule.Ruz;
import com.danapps.polytech.schedule.SchedulerError;
import com.danapps.polytech.schedule.model.Faculty;
import com.danapps.polytech.schedule.model.Group;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class WelcomeGroupActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("UserInfo");
    TextInputLayout groupTIL;
    AutoCompleteTextView groupET;

    List<Integer> groupIdList = new ArrayList<>();
    List<String> groupList = new ArrayList<>();
    int groupCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sPref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        myRef = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("UserInfo");
        setContentView(R.layout.activity_welcome_group);
        groupET = findViewById(R.id.welcome_group_groupET);
        groupTIL = findViewById(R.id.welcome_group_groupTIL);

        Ruz ruz = new Ruz(getBaseContext());

        ruz.newFaculties().queryFaculties(new Faculties.Listener() {
            @Override
            public void onResponseReady(List<Faculty> faculties) {
                for (int i = 0; i < faculties.size(); i++) {
                    ruz.newGroups().queryGroups(faculties.get(i), new Groups.Listener() {
                        @Override
                        public void onResponseReady(List<Group> groups) {
                            for (int j = 0; j < groups.size(); j++) {
                                groupList.add(groups.get(j).getName());
                                groupIdList.add(groups.get(j).getId());
                                groupCount++;
                            }
                        }

                        @Override
                        public void onResponseError(SchedulerError error) {
                            Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onResponseError(SchedulerError error) {
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, groupList);
        groupET.setAdapter(adapter);

        findViewById(R.id.welcome_group_nextBTN).setOnClickListener(v -> {
            if (groupET.getText().toString().isEmpty())
                groupTIL.setError(getString(R.string.welcome_group_error));
            else {
                boolean checker = false;
                for (int i = 0; i < groupCount; i++) {
                    int j = i;
                    if (groupET.getText().toString().equals(groupList.get(j))) {
                        checker = true;
                        myRef.child("UserGroupName").setValue(groupList.get(j))
                        .addOnFailureListener(e -> Snackbar.make(v, getString(R.string.connection_error), Snackbar.LENGTH_SHORT).show())
                        .addOnSuccessListener(aVoid -> {
                            myRef.child("UserGroupId").setValue(groupIdList.get(j))
                            .addOnFailureListener(e -> Snackbar.make(v, getString(R.string.connection_error), Snackbar.LENGTH_SHORT).show())
                            .addOnSuccessListener(aVoid1 -> {
                                sPref.edit().putString("UserGroupName", groupList.get(j))
                                .putInt("UserGroupId", groupIdList.get(j)).apply();
                                startActivity(new Intent(WelcomeGroupActivity.this, MainActivity.class));
                            });
                        });
                    }
                }

                if (checker)
                    groupTIL.setError(getString(R.string.group_error));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WelcomeGroupActivity.this, WelcomeStartActivity.class));
    }
}
