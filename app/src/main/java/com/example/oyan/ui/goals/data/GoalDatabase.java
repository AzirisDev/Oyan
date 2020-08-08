package com.example.oyan.ui.goals.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Goal.class}, version = 1)
public abstract class GoalDatabase extends RoomDatabase {
    public abstract GoalDao getGoalDAO();
}
