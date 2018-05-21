package adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.master.android_finance_manager.EntriesActivities.EditEntryActivity;
import com.example.master.android_finance_manager.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import entities.Expense;

public class RecyclerAdapterExpense extends RecyclerView.Adapter<RecyclerAdapterExpense.ExpenseViewHolder>
{

    private ArrayList<Expense> expenses;

    public RecyclerAdapterExpense(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {

        Expense expense = expenses.get(position);
        holder.expenseId = expense.getEntryId();
        holder.title.setText(expense.getTitle());
        holder.sumOfMoney.setText(expense.getMoneySpent().toString());
        holder.date.setText(expense.getEntryDate());

    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_layout, parent, false);
        ExpenseViewHolder expenseViewHolder = new ExpenseViewHolder(view);

        return expenseViewHolder;
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener
    {
        int expenseId;
        TextView title;
        TextView sumOfMoney;
        TextView date;
        TextView labelTitle;
        TextView labelSumOfMoney;
        TextView labelDate;

        public ExpenseViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            labelTitle = itemView.findViewById(R.id.titleEntryView);
            labelSumOfMoney = itemView.findViewById(R.id.moneyEntryView);
            labelDate = itemView.findViewById(R.id.dateEntryView);
            title = itemView.findViewById(R.id.showingTitleView);
            sumOfMoney = itemView.findViewById(R.id.showingSumOfMoneyView);
            date = itemView.findViewById(R.id.showingDateView);

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
                            // Toast.makeText(PopupMenuDemoActivity.this,
                            // item.toString(), Toast.LENGTH_LONG).show();
                            // return true;
                            switch (item.getItemId()) {

                                case R.id.editMenuItem:
                                    Intent intent = new Intent(view.getContext(), EditEntryActivity.class);
                                    intent.putExtra("ENTRY_TYPE", "EXPENSE");
                                    intent.putExtra("ENTRY_ID", expenseId);
                                    view.getContext().startActivity(intent);
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
