package com.danapps.polytech.fragments.tabs;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danapps.polytech.R;
import com.danapps.polytech.faculties.FacultiesAdapter;
import com.danapps.polytech.faculties.FacultiesListItem;
import com.danapps.polytech.notes.RecyclerTouchListener;
import com.danapps.polytech.schedule.Faculties;
import com.danapps.polytech.schedule.Ruz;
import com.danapps.polytech.schedule.SchedulerError;
import com.danapps.polytech.schedule.model.Faculty;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ChangeFacultFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FacultiesListItem> listItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_facult, container, false);
        Ruz ruz = new Ruz(getContext());
        Faculties faculties = ruz.newFaculties();
        recyclerView = view.findViewById(R.id.notes_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        faculties.queryFaculties(new Faculties.Listener() {
            @Override
            public void onResponseReady(List<Faculty> faculties) {
                listItems = new ArrayList<>();

                for (int i = 0; i < faculties.size(); i++) {
                    Faculty faculty = faculties.get(i);
                    FacultiesListItem listItem = new FacultiesListItem(faculty.getName());
                    listItems.add(listItem);
                }

                adapter = new FacultiesAdapter(listItems, getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onResponseError(SchedulerError error) {
                Snackbar.make(view, error.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view1, int position) {
                Snackbar.make(view, String.valueOf(position), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view1, int position) {

            }
        }));

        return view;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.facultiesListName);
        }
    }
}
