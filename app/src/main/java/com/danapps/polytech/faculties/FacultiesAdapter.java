package com.danapps.polytech.faculties;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.MainActivity;
import com.danapps.polytech.notes.NoteAdapter;
import com.danapps.polytech.notes.NoteListItem;

import java.util.List;

public class FacultiesAdapter extends RecyclerView.Adapter<FacultiesAdapter.ViewHolder>{

    private List<FacultiesListItem> listItems;
    private Context context;
    private int current;

    public FacultiesAdapter(List<FacultiesListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
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

        holder.textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = position;
            }
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
