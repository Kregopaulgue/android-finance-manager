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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_reminder_layout, parent, false);
        RecyclerAdapterBillReminder.BillReminderViewHolder billReminderViewHolder = new RecyclerAdapterBillReminder.BillReminderViewHolder(view);

        return billReminderViewHolder;
    }

    @Override
    public int getItemCount() {
        return billReminders.size();
    }

    public static class BillReminderViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener
    {
        TextView billTitle;
        TextView sumOfMoneyToPay;
        TextView dateToPay;
        TextView showingBillTitle;
        TextView showingSumOfMoneyToPay;
        TextView showingDateToPay;


        public BillReminderViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.billTitle = itemView.findViewById(R.id.billTitleEntryView);
            this.dateToPay = itemView.findViewById(R.id.dateToPayView);
            this.sumOfMoneyToPay = itemView.findViewById(R.id.sumOfMoneyToPayEntryView);
            this.showingBillTitle = itemView.findViewById(R.id.showingBillTitleView);
            this.showingSumOfMoneyToPay = itemView.findViewById(R.id.showingSumOfMoneyToPayView);
            this.showingDateToPay = itemView.findViewById(R.id.showingDateToPayView);

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
