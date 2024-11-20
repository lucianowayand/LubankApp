package io.github.lucianoawayand.lubank_app.Home.adapter;

import android.content.Context;
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
    private ItemClickListener mClickListener;


    // data is passed into the constructor
    public ActionsAdapter(Context context, ArrayList<Action> actions) {
        this.context = context;
        this.actions = actions;
    }

    // inflates the row layout from xml when needed
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
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return actions.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView icon;
        private final TextView title;

        public ViewHolder(@NonNull ActionItemBinding binding) {
            super(binding.getRoot());
            this.icon = binding.idActionIcon;
            this.title = binding.txtActionTitle;

            // Set click listener for the item view
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
