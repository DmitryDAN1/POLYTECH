package com.danapps.polytech.fragments.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.danapps.polytech.R;

import java.util.Objects;

public class NoteEditFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_edit, container, false);

        SharedPreferences sPref = Objects.requireNonNull(getActivity()).getSharedPreferences("NotesInfo", Context.MODE_PRIVATE);
        int note = sPref.getInt("CurrentNote", 0);

        EditText titleET = view.findViewById(R.id.notes_edit_TitleET);
        EditText subtitleET = view.findViewById(R.id.notes_edit_SubtitleET);

        titleET.setText(sPref.getString("Title" + note, "Title"));
        subtitleET.setText(sPref.getString("Subtitle" + note, "Subtitle"));

        view.findViewById(R.id.notes_edit_backBTN).setOnClickListener(v -> {
            NotesFragment notesFragment = new NotesFragment();
            Objects.requireNonNull(getFragmentManager()).beginTransaction().replace(R.id.frame_layout, notesFragment).commit();
        });

        view.findViewById(R.id.notes_edit_applyBTN).setOnClickListener(v -> {
            NotesFragment notesFragment = new NotesFragment();
            sPref.edit().putString("Title" + note, titleET.getText().toString()).apply();
            sPref.edit().putString("Subtitle" + note, subtitleET.getText().toString()).apply();
            Objects.requireNonNull(getFragmentManager()).beginTransaction().replace(R.id.frame_layout, notesFragment).commit();
        });

        return view;
    }

}
