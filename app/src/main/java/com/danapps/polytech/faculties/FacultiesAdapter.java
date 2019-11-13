package com.danapps.polytech.faculties;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;

import java.util.List;

public class FacultiesAdapter extends RecyclerView.Adapter<FacultiesAdapter.ViewHolder>{

    private List<FacultiesListItem> listItems;

    public FacultiesAdapter(List<FacultiesListItem> listItems) {
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public FacultiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faculties_list_item, parent, false);

        return new FacultiesAdapter.ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultiesAdapter.ViewHolder holder, int position) {
        FacultiesListItem listItem = listItems.get(position);
        holder.textViewTitle.setText(listItem.getName());

        holder.textViewTitle.setOnClickListener(v -> {
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.facultiesListName);
        }
    }
}
