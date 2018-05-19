package adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.master.android_finance_manager.R;

import java.util.ArrayList;

import entities.Accrual;

public class RecyclerAdapterAccrual extends RecyclerView.Adapter<RecyclerAdapterAccrual.AccrualViewHolder>{

    private ArrayList<Accrual> accruals;

    public RecyclerAdapterAccrual(ArrayList<Accrual> accruals){

        this.accruals = accruals;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterAccrual.AccrualViewHolder holder, int position) {

        Accrual expenseId = accruals.get(position);
        holder.title.setText(expenseId.getSource());
        holder.sumOfMoney.setText(expenseId.getMoneyGained().toString());
        holder.date.setText(expenseId.getEntryDate());

    }

    @NonNull
    @Override
    public RecyclerAdapterAccrual.AccrualViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_layout, parent, false);
        RecyclerAdapterAccrual.AccrualViewHolder accrualViewHolder = new RecyclerAdapterAccrual.AccrualViewHolder(view);

        return accrualViewHolder;
    }

    @Override
    public int getItemCount() {
        return accruals.size();
    }

    public static class AccrualViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView sumOfMoney;
        TextView date;


        public AccrualViewHolder(View itemView) {
            super(itemView);

        }
    }

}
