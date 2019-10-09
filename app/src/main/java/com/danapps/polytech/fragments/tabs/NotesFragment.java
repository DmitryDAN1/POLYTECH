package com.danapps.polytech.fragments.tabs;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.danapps.polytech.R;
import com.danapps.polytech.notes.NoteAdapter;
import com.danapps.polytech.notes.NoteListItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<NoteListItem> listItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        SharedPreferences sPref = Objects.requireNonNull(getActivity()).getSharedPreferences("NotesInfo", Context.MODE_PRIVATE);

        recyclerView = view.findViewById(R.id.notes_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        UpdateRV(recyclerView, sPref);

        view.findViewById(R.id.notes_notes_addBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog dialogB = new BottomSheetDialog(Objects.requireNonNull(getActivity()));
                LayoutInflater inflater1 = LayoutInflater.from(getContext());
                @SuppressLint("InflateParams") View window_add_note = inflater1.inflate(R.layout.window_add_note, null);
                dialogB.setContentView(window_add_note);
                dialogB.show();

                TextView errorTV = window_add_note.findViewById(R.id.notes_errorTV);
                EditText titleET = window_add_note.findViewById(R.id.notes_titleET);
                EditText subtitleET = window_add_note.findViewById(R.id.notes_subtitleET);

                window_add_note.findViewById(R.id.notes_saveBTN).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (titleET.getText().toString().isEmpty() && subtitleET.getText().toString().isEmpty())
                            dialogB.hide();
                        else {
                            if (!titleET.getText().toString().isEmpty() && subtitleET.getText().toString().isEmpty())
                                errorTV.setText(getString(R.string.notes_error_subtitle));
                            else if (titleET.getText().toString().isEmpty() && !subtitleET.getText().toString().isEmpty())
                                errorTV.setText(getString(R.string.notes_error_title));
                            else {
                                int NotesCount = sPref.getInt("NotesCount", 0);
                                sPref.edit().putString(("Title" + String.valueOf(NotesCount + 1)), titleET.getText().toString()).apply();
                                sPref.edit().putString(("Subtitle" + String.valueOf(NotesCount + 1)), subtitleET.getText().toString()).apply();
                                sPref.edit().putInt("NotesCount", (NotesCount + 1)).apply();
                                UpdateRV(recyclerView, sPref);
                                dialogB.hide();
                            }
                        }
                    }
                });

            }
        });


        return view;
    }

    private void UpdateRV (RecyclerView recyclerView, SharedPreferences sPref) {
        int NotesCount = sPref.getInt("NotesCount", 0);
        listItems = new ArrayList<>();

        for (int i = 1; i <= NotesCount; i++) {
            NoteListItem listItem = new NoteListItem(
                    sPref.getString("Title" + i, "None"),
                    sPref.getString("Subtitle" + i, "None")

            );

            listItems.add(listItem);
        }

        adapter = new NoteAdapter(listItems, getContext());
        recyclerView.setAdapter(adapter);
    }

}