package adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.master.android_finance_manager.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import data.FinancialManagerDbHelper;
import entities.Account;
import entities.Goal;

import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_ACCOUNT_ID;
import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_APP;

public class RecyclerAdapterGoal extends RecyclerView.Adapter<RecyclerAdapterGoal.GoalViewHolder>{

    private ArrayList<Goal> goals;

    public RecyclerAdapterGoal(ArrayList<Goal> goals){

        this.goals = goals;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterGoal.GoalViewHolder holder, int position) {

        Goal goal = goals.get(position);
        holder.goalId = goal.getGoalId();
        holder.showingTargetTitle.setText(goal.getTargetTitle());
        holder.showingTargetSumMoney.setText(goal.getSumToReach().toString());
        holder.showingCurrentSumOfMoney.setText(goal.getCurrentSum().toString());

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
        int goalId;
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

        private void showPopupMenu(final View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.crud_popup_menu);

            popupMenu
                    .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.editMenuItem:
                                    addGoalDialog(view);
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

        private void addGoalDialog(final View view) {

            LayoutInflater li = LayoutInflater.from(view.getContext());
            View promptsView = li.inflate(R.layout.edit_goal, null);

            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(view.getContext());

            mDialogBuilder.setView(promptsView);

            final EditText titleInput = (EditText) promptsView.findViewById(R.id.editTargetTitleInput);
            final EditText sumToReachInput = (EditText) promptsView.findViewById(R.id.editSumToReachInput);
            final EditText currentSumInput = (EditText) promptsView.findViewById(R.id.editCurrentSumInput);

            mDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("ADD GOAL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    //Вводим текст и отображаем в строке ввода на основном экране:
                                    showingTargetTitle.setText(titleInput.getText().toString());
                                    showingTargetSumMoney.setText(sumToReachInput.getText().toString());
                                    showingCurrentSumOfMoney.setText(currentSumInput.getText().toString());

                                    FinancialManagerDbHelper dbHelper = new FinancialManagerDbHelper(view.getContext());
                                    Account mParentAccount = new Account();

                                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(CURRENT_APP, Context.MODE_PRIVATE);
                                    int parentAccountId = sharedPreferences.getInt(CURRENT_ACCOUNT_ID, 1);
                                    mParentAccount.readFromDatabase(dbHelper, parentAccountId);

                                    Goal goalToUpdate = new Goal();
                                    goalToUpdate.setGoalId(goalId);
                                    goalToUpdate.setTargetTitle(showingTargetTitle.getText().toString());
                                    goalToUpdate.setSumToReach(Double.parseDouble(showingTargetSumMoney.getText().toString()));
                                    goalToUpdate.setCurrentSum(Double.parseDouble(showingCurrentSumOfMoney.getText().toString()));
                                    goalToUpdate.setParentAccount(mParentAccount);
                                    goalToUpdate.setReached("false");
                                    goalToUpdate.updateToDatabase(dbHelper, goalId);
                                }
                            })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


            AlertDialog alertDialog = mDialogBuilder.create();
            alertDialog.show();
        }
    }


}
