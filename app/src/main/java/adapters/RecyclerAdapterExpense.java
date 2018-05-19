package adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.master.android_finance_manager.R;

import java.util.ArrayList;

import entities.Expense;

public class RecyclerAdapterExpense extends RecyclerView.Adapter<RecyclerAdapterExpense.ExpenseViewHolder>{

    private ArrayList<Expense> expenses;

    public RecyclerAdapterExpense(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {

        Expense expenseId = expenses.get(position);
        holder.title.setText(expenseId.getTitle());
        holder.sumOfMoney.setText(expenseId.getMoneySpent().toString());
        holder.date.setText(expenseId.getEntryDate());

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
    {
        TextView title;
        TextView sumOfMoney;
        TextView date;


        public ExpenseViewHolder(View itemView) {
            super(itemView);

        }
    }
}
