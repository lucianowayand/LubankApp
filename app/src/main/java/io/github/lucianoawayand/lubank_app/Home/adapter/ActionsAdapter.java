package io.github.lucianoawayand.lubank_app.Home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.github.lucianoawayand.lubank_app.databinding.ActionItemBinding;
import io.github.lucianoawayand.lubank_app.Home.model.Action;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Action> actions;


    // data is passed into the constructor
    public ActionsAdapter(Context context, ArrayList<Action> actions) {
        this.context = context;
        this.actions = actions;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using View Binding
        ActionItemBinding binding = ActionItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the ViewHolder
        Action action = actions.get(position);

        holder.icon.setImageResource(action.getIcon());
        holder.title.setText(action.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, action.getTargetActivity());
            context.startActivity(intent);
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return actions.size();
    }


    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView icon;
        private final TextView title;

        public ViewHolder(@NonNull ActionItemBinding binding) {
            super(binding.getRoot());
            this.icon = binding.idActionIcon;
            this.title = binding.txtActionTitle;
        }
    }
}
