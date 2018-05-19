package adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.master.android_finance_manager.R;

import java.util.ArrayList;

import entities.BillReminder;

public class RecyclerAdapterBillReminder extends RecyclerView.Adapter<RecyclerAdapterBillReminder.BillReminderViewHolder>{

    private ArrayList<BillReminder> billReminders;

    public RecyclerAdapterBillReminder(ArrayList<BillReminder> billReminders) {
        this.billReminders = billReminders;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterBillReminder.BillReminderViewHolder holder, int position) {

        BillReminder billReminderId = billReminders.get(position);
        holder.billTitle.setText(billReminderId.getBillTitle());
        holder.sumOfMoneyToPay.setText(billReminderId.getSumToPay().toString());
        holder.dateToPay.setText(billReminderId.getDateTimeToPay());

    }

    @NonNull
    @Override
    public RecyclerAdapterBillReminder.BillReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_layout, parent, false);
        RecyclerAdapterBillReminder.BillReminderViewHolder billReminderViewHolder = new RecyclerAdapterBillReminder.BillReminderViewHolder(view);

        return billReminderViewHolder;
    }

    @Override
    public int getItemCount() {
        return billReminders.size();
    }

    public static class BillReminderViewHolder extends RecyclerView.ViewHolder
    {
        TextView billTitle;
        TextView sumOfMoneyToPay;
        TextView dateToPay;


        public BillReminderViewHolder(View itemView) {
            super(itemView);

        }
    }
    
}
