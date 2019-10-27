package com.danapps.polytech.fragments.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danapps.polytech.R;
import com.danapps.polytech.MainActivity;
import com.danapps.polytech.faculties.FacultiesAdapter;
import com.danapps.polytech.faculties.FacultiesListItem;
import com.danapps.polytech.notes.RecyclerTouchListener;
import com.google.android.material.snackbar.Snackbar;
import com.venvw.spbstu.ruz.RuzService;
import com.venvw.spbstu.ruz.api.FacultiesApi;
import com.venvw.spbstu.ruz.models.Faculty;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeFacultyFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FacultiesListItem> listItems = new ArrayList<>();
    private FacultiesApi facultiesApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_facult, container, false);
        facultiesApi = RuzService.getInstance().getFacultiesApi();
        recyclerView = view.findViewById(R.id.notes_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences tPref = getActivity().getSharedPreferences("TimedInfo", Context.MODE_PRIVATE);

        facultiesApi.getFaculties().enqueue(new Callback<FacultiesApi.GetFacultiesResponse>() {
            @Override
            public void onResponse(Call<FacultiesApi.GetFacultiesResponse> call, Response<FacultiesApi.GetFacultiesResponse> response) {
                view.findViewById(R.id.changeFaculties_progressBar).setVisibility(View.GONE);

                listItems = new ArrayList<>();
                List<Faculty> faculties = response.body().getFaculties();
                for (int i = 0; i < faculties.size(); i++) {
                    Faculty faculty = faculties.get(i);
                    FacultiesListItem listItem = new FacultiesListItem(faculty.getName());
                    listItems.add(listItem);
                    tPref.edit().putInt(String.valueOf(i), faculty.getId()).apply();
                }

                adapter = new FacultiesAdapter(listItems, getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<FacultiesApi.GetFacultiesResponse> call, Throwable t) {
                Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                tPref.edit().putInt("FacultiesId", tPref.getInt(String.valueOf(position), 0)).apply();
                Log.e("ChangeFacultyFragment", "FacultID:" + tPref.getInt(String.valueOf(position), 0));
                ((MainActivity) getActivity()).loadFragment(5);
            }

            @Override
            public void onLongClick(View view1, int position) {

            }
        }));

        return view;
    }
}
