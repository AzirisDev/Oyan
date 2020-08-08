package com.example.oyan.ui.goals.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GoalDao {

    @Insert
    long addGoal(Goal goal);

    @Delete
    void deleteGoal(Goal goal);

    @Query("select * from goals")
    List<Goal> getAllGoals();

    @Query("select * from goals where goal_id ==:goalID")
    Goal getGoal(long goalID);

}
