package com.danapps.polytech.fragments.tabs;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.MainActivity;
import com.danapps.polytech.notes.NoteAdapter;
import com.danapps.polytech.notes.NoteListItem;
import com.danapps.polytech.notes.RecyclerTouchListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotesFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<NoteListItem> listItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        SharedPreferences sPref = Objects.requireNonNull(getActivity()).getSharedPreferences("NotesInfo", Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.notes_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

                builder.setTitle("Удаление заметки")
                        .setMessage("Вы уверены, что хотите удалить заметку?")
                        .setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                UpdateRV(recyclerView, sPref);
                            }
                        })
                        .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int noteNumber = viewHolder.getAdapterPosition() + 1;
                                int notesCount = sPref.getInt("NotesCount", 0);

                                for (int j = noteNumber; j < notesCount; j++) {
                                    sPref.edit().putString("Title" + j, sPref.getString("Title" + (j + 1), "None")).apply();
                                    sPref.edit().putString("Subtitle" + j, sPref.getString("Subtitle" + (j + 1), "None")).apply();
                                }

                                sPref.edit().remove("Title" + notesCount).remove("Subtitle" + notesCount).putInt("NotesCount", notesCount - 1).apply();

                                UpdateRV(recyclerView, sPref);
                            }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        };


        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        UpdateRV(recyclerView, sPref);

        view.findViewById(R.id.notes_notes_addBTN).setOnClickListener(v -> {
            BottomSheetDialog dialogB = new BottomSheetDialog(Objects.requireNonNull(getActivity()));
            LayoutInflater inflater1 = LayoutInflater.from(getContext());
            @SuppressLint("InflateParams") View window_add_note = inflater1.inflate(R.layout.window_add_note, null);
            dialogB.setContentView(window_add_note);
            dialogB.show();

            TextView errorTV = window_add_note.findViewById(R.id.notes_errorTV);
            EditText titleET = window_add_note.findViewById(R.id.notes_titleET);
            EditText subtitleET = window_add_note.findViewById(R.id.notes_subtitleET);

            window_add_note.findViewById(R.id.notes_saveBTN).setOnClickListener(v1 -> {
                if (titleET.getText().toString().isEmpty() && subtitleET.getText().toString().isEmpty())
                    dialogB.hide();
                else {
                    if (!titleET.getText().toString().isEmpty() && subtitleET.getText().toString().isEmpty())
                        errorTV.setText(getString(R.string.notes_error_subtitle));
                    else if (titleET.getText().toString().isEmpty() && !subtitleET.getText().toString().isEmpty())
                        errorTV.setText(getString(R.string.notes_error_title));
                    else {
                        int NotesCount = sPref.getInt("NotesCount", 0);
                        sPref.edit().putString(("Title" + (NotesCount + 1)), titleET.getText().toString()).apply();
                        sPref.edit().putString(("Subtitle" + (NotesCount + 1)), subtitleET.getText().toString()).apply();
                        sPref.edit().putInt("NotesCount", (NotesCount + 1)).apply();
                        UpdateRV(recyclerView, sPref);
                        dialogB.hide();
                    }
                }
            });

        });


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                NoteEditFragment noteEditFragment = new NoteEditFragment();
                sPref.edit().putInt("CurrentNote", (position + 1)).apply();
                Objects.requireNonNull(getFragmentManager()).beginTransaction().replace(R.id.frame_layout, noteEditFragment).commit();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), String.valueOf(position+10), Toast.LENGTH_SHORT).show();
            }
        }));







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
