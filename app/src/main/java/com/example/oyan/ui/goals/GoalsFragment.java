package com.example.oyan.ui.goals;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.oyan.R;
import com.example.oyan.ui.goals.data.Goal;
import com.example.oyan.ui.goals.data.GoalDatabase;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

;

public class GoalsFragment extends Fragment {

    private RecyclerView recyclerView;
    private GoalsAdapter recyclerAdapter;
    private ArrayList<Goal> goalsList;
    private TextInputLayout newGoalEditText;
    private ImageButton addImageButton;

    private View root;
    private String deletedGoal = null;

    private GoalDatabase goalsDatabase;
    MediaPlayer doneSound = null;

    private String text;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        goalsList = new ArrayList<>();

        databaseStaff();

        root = inflater.inflate(R.layout.fragment_goals, container, false);

        addGoalUI();

        recyclerViewStaff();

        constructMessage();

        return root;

    }

    public void constructMessage() {

        text = null;

        if (goalsList.size() >= 1) {
            String goal1 = goalsList.get(0).getName();
            if (goal1 != null) {
                text += "\n" + goal1;
            }
        }

        if (goalsList.size() >= 2) {
            String goal2 = goalsList.get(1).getName();
            if (goal2 != null) {
                text += "\n" + goal2;
            }
        }

        if (goalsList.size() >= 3) {
            String goal3 = goalsList.get(2).getName();
            if (goal3 != null) {
                text += "\n" + goal3;
            }
        }

    }

    private void recyclerViewStaff() {
        recyclerView = root.findViewById(R.id.goals_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new GoalsAdapter(goalsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleDeletionCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerAdapter);
    }

    private void addGoalUI() {
        newGoalEditText = root.findViewById(R.id.newGoalEditText);
        addImageButton = root.findViewById(R.id.addGoalButton);
        doneSound = MediaPlayer.create(getContext(), R.raw.gamelan_hit_soft);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoal();
            }
        });
    }

    private void databaseStaff() {
        goalsDatabase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                GoalDatabase.class,
                "GoalsDb"
        ).build();

        new GetAllGoalsAsyncTask().execute();
    }

    private void addGoal() {
        String newGoal = newGoalEditText.getEditText().getText().toString().trim();
        Goal goal = new Goal(newGoal);

        if (newGoal.isEmpty()) {
            newGoalEditText.setError("Please input your goal");
        } else if (newGoal.length() > 30) {
            newGoalEditText.setError("Goal length have to be less than 30");
        } else {
            createGoal(goal.getName());
            newGoalEditText.getEditText().setText("");
            newGoalEditText.setErrorEnabled(true);
            recyclerAdapter.notifyDataSetChanged();
            constructMessage();
        }
    }

    private void createGoal(String name) {
        new CreateGoalAsyncTask().execute(new Goal(0, name));
    }

    private void deleteGoal(final int position) {
        deletedGoal = goalsList.get(position).getName();

        Toast.makeText(getContext(), deletedGoal + " is gone!", Toast.LENGTH_LONG).show();
        doneSound.start();

        new DeleteGoalAsyncTask().execute(goalsList.get(position));
        goalsList.remove(position);

    }


    ItemTouchHelper.SimpleCallback simpleDeletionCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                deleteGoal(position);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(getContext(), c, recyclerView, viewHolder,
                    dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark))
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private class GetAllGoalsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            goalsList.addAll(goalsDatabase.getGoalDAO().getAllGoals());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            constructMessage();
            recyclerAdapter.notifyDataSetChanged();
        }
    }


    private class CreateGoalAsyncTask extends AsyncTask<Goal, Void, Void> {
        @Override
        protected Void doInBackground(Goal... goals) {

            long id = goalsDatabase.getGoalDAO().addGoal(goals[0]);

            Goal goal = goalsDatabase.getGoalDAO().getGoal(id);

            if (goal != null) {
                goalsList.add(goal);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            constructMessage();

            recyclerAdapter.notifyDataSetChanged();
        }
    }

    private class DeleteGoalAsyncTask extends AsyncTask<Goal, Void, Void> {

        @Override
        protected Void doInBackground(Goal... goals) {
            goalsDatabase.getGoalDAO().deleteGoal(goals[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            constructMessage();

            recyclerAdapter.notifyDataSetChanged();
        }
    }

    public String getData(){
        return text;
    }
}