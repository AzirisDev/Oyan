package com.example.oyan.ui.goals;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oyan.MainActivity;
import com.example.oyan.R;
import com.example.oyan.ui.goals.data.Goal;

import java.util.ArrayList;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder> {

    private ArrayList<Goal> goalsList;

    public GoalsAdapter(ArrayList<Goal> goalsList) {
        this.goalsList = goalsList;
    }

    public class GoalsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton clickButton;
        TextView goalTextView;

        public GoalsViewHolder(@NonNull View itemView) {
            super(itemView);

            goalTextView = itemView.findViewById(R.id.goal_text_view);
            clickButton = itemView.findViewById(R.id.click_button);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), goalsList.get(getAdapterPosition()).getName(), Toast.LENGTH_LONG).show();
        }
    }


    @NonNull
    @Override
    public GoalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.goal_item, parent, false);

        return new GoalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GoalsViewHolder holder, int position) {
        holder.goalTextView.setText(goalsList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }
}
