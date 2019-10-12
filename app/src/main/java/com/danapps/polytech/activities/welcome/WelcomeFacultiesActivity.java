package com.danapps.polytech.activities.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.danapps.polytech.R;
import com.danapps.polytech.schedule.Faculties;
import com.danapps.polytech.schedule.Ruz;
import com.danapps.polytech.schedule.SchedulerError;
import com.danapps.polytech.schedule.model.Faculty;

import java.util.ArrayList;
import java.util.List;

public class WelcomeFacultiesActivity extends AppCompatActivity {

    ArrayAdapter<String> facAdapter;
    List<String> list;
    List<Integer> listID;
    int facSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_faculties);

        Ruz ruz = new Ruz(getBaseContext());
        AutoCompleteTextView facultiesET = findViewById(R.id.welcome_faculties_groupET);

        list = new ArrayList<String>();
        listID = new ArrayList<Integer>();

        ruz.newFaculties().queryFaculties(new Faculties.Listener() {
            @Override
            public void onResponseReady(List<Faculty> faculties) {
                facSize = faculties.size();

                for (int i = 0; i < faculties.size(); i++) {
                    list.add(faculties.get(i).getAbbr());
                    listID.add(faculties.get(i).getId());
                }
            }

            @Override
            public void onResponseError(SchedulerError error) {

            }
        });

        facAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, list);
        facultiesET.setAdapter(facAdapter);

        findViewById(R.id.welcome_faculties_nextBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < facSize; i++) {
                    if (facultiesET.getText().toString().equals(list.get(i)))
                        Log.e("Faculties", listID.get(i).toString());
                }
            }
        });

    }
}
