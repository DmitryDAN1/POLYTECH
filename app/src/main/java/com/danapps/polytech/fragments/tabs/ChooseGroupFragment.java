package com.danapps.polytech.fragments.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danapps.polytech.MainActivity;
import com.danapps.polytech.R;
import com.danapps.polytech.Schedule.ChooseGroupAdapter;
import com.danapps.polytech.Schedule.ChooseGroupListItem;
import com.danapps.polytech.notes.RecyclerTouchListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChooseGroupFragment extends Fragment {

    private ColorDrawable background = new ColorDrawable(Color.RED);
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ChooseGroupListItem> listItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_group, container, false);
        SharedPreferences gPref = getActivity().getSharedPreferences("GroupsInfo", Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.chooseGroup_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int groupNumber = viewHolder.getAdapterPosition() + 1;
                int groupsCount = gPref.getInt("GroupsCount", 0);

                for (int j = groupNumber; j < groupsCount; j++) {
                    gPref.edit().putString("GroupName" + j, gPref.getString("GroupName" + (j + 1), "None")).apply();
                    gPref.edit().putInt("GroupId" + j, gPref.getInt("GroupId" + (j + 1), 0)).apply();
                }

                gPref.edit().remove("GroupName" + groupsCount).remove("GroupId" + groupsCount).putInt("GroupsCount", groupsCount - 1).apply();
                if (gPref.getInt("CurrentGroup", 0) > 1)
                    gPref.edit().putInt("CurrentGroup", gPref.getInt("CurrentGroup", 0) - 1).apply();
                Snackbar.make(view, "Группа удалена", Snackbar.LENGTH_SHORT).show();

                UpdateRV(recyclerView, gPref);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                background.setBounds(itemView.getRight() + (int)dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());

                background.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        UpdateRV(recyclerView, gPref);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), (view1, position) -> {
            gPref.edit().putInt("CurrentGroup", (position + 1)).apply();
            ((MainActivity) getActivity()).updateFragment(2);
            ((MainActivity) getActivity()).loadFragment(2);
        }));

        view.findViewById(R.id.chooseGroup_floatingButton).setOnClickListener(v ->
                ((MainActivity) getActivity()).loadFragment(6));

        return view;
    }

    private void UpdateRV (RecyclerView recyclerView, SharedPreferences gPref) {
        int GroupsCount = gPref.getInt("GroupsCount", 0);
        listItems = new ArrayList<>();

        for (int i = 1; i <= GroupsCount; i++) {
            ChooseGroupListItem listItem = new ChooseGroupListItem(
                    gPref.getString("GroupName" + i, "None")
            );

            listItems.add(listItem);
        }

        adapter = new ChooseGroupAdapter(listItems, getContext());
        recyclerView.setAdapter(adapter);
    }


}
