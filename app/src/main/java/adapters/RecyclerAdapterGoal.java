package adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.master.android_finance_manager.R;

import java.util.ArrayList;

import entities.Goal;

public class RecyclerAdapterGoal extends RecyclerView.Adapter<RecyclerAdapterGoal.GoalViewHolder>{

    private ArrayList<Goal> goals;

    public RecyclerAdapterGoal(ArrayList<Goal> goals){

        this.goals = goals;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterGoal.GoalViewHolder holder, int position) {

        Goal goal = goals.get(position);
        holder.targetTitle.setText(goal.getTargetTitle());
        holder.targetSumOfMoney.setText(goal.getSumToReach().toString());
        //holder.targetDate.setText(goal.?);

    }

    @NonNull
    @Override
    public RecyclerAdapterGoal.GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_layout, parent, false);
        RecyclerAdapterGoal.GoalViewHolder goalViewHolder = new RecyclerAdapterGoal.GoalViewHolder(view);

        return goalViewHolder;
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder
    {
        TextView targetTitle;
        TextView currentSumOfMoney;
        TextView targetSumOfMoney;
        TextView targetDate;


        public GoalViewHolder(View itemView) {
            super(itemView);

        }
    }


}
