package adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.master.android_finance_manager.R;

import org.w3c.dom.Text;

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
            implements View.OnClickListener, View.OnLongClickListener
    {
        TextView targetTitle;
        TextView currentSumOfMoney;
        TextView targetSumOfMoney;
        TextView showingTargetTitle;
        TextView showingCurrentSumOfMoney;
        TextView showingTargetSumMoney;


        public GoalViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            targetTitle = itemView.findViewById(R.id.titleGoalView);
            currentSumOfMoney = itemView.findViewById(R.id.currentSumEntryView);
            targetSumOfMoney = itemView.findViewById(R.id.targetSumEntryView);
            showingTargetTitle = itemView.findViewById(R.id.showingTargetTitleView);
            showingCurrentSumOfMoney = itemView.findViewById(R.id.showingCurrentSumOfMoneyView);
            showingTargetSumMoney = itemView.findViewById(R.id.showingTargetSumOfMoneyView);

        }

        @Override
        public void onClick(View view) {
            // Context context = view.getContext();
            // article.getName()
        }

        @Override
        public boolean onLongClick(View view) {
            showPopupMenu(view);
            return true;
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.crud_popup_menu);

            popupMenu
                    .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.editMenuItem:

                                    return true;
                                case R.id.deleteMenuItem:

                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {

                @Override
                public void onDismiss(PopupMenu menu) {

                }
            });
            popupMenu.show();
        }
    }


}
