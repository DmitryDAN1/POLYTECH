package com.danapps.polytech.Schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;

import java.util.List;

public class ChooseGroupAdapter extends RecyclerView.Adapter<ChooseGroupAdapter.ViewHolder> {

    private List<ChooseGroupListItem> listItems;
    private Context context;

    public ChooseGroupAdapter(List<ChooseGroupListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.groups_list_item, parent, false);

        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChooseGroupListItem listItem = listItems.get(position);

        holder.textViewTitle.setText(listItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.groupsListItem_Text);
        }
    }
}

