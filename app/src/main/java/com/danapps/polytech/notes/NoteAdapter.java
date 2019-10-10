package com.danapps.polytech.notes;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.danapps.polytech.R;
import com.danapps.polytech.activities.StartActivity;
import com.danapps.polytech.fragments.tabs.NoteEditFragment;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<NoteListItem> listItems;
    private Context context;

    public NoteAdapter(List<NoteListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list_item, parent, false);

        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteListItem listItem = listItems.get(position);

        holder.textViewTitle.setText(listItem.getTitle());
        holder.textViewSubtitle.setText(listItem.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewSubtitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.titleTextView);
            textViewSubtitle = itemView.findViewById(R.id.subtitleTextView);
        }
    }
}

