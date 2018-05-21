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

import java.util.ArrayList;

import entities.Accrual;

public class RecyclerAdapterAccrual extends RecyclerView.Adapter<RecyclerAdapterAccrual.AccrualViewHolder>{

    private ArrayList<Accrual> accruals;

    public RecyclerAdapterAccrual(ArrayList<Accrual> accruals){

        this.accruals = accruals;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterAccrual.AccrualViewHolder holder, int position) {

        Accrual accrual = accruals.get(position);
        holder.accrualId = accrual.getEntryId();
        holder.showingTitle.setText(accrual.getTitle());
        holder.showingSumOfMoney.setText(accrual.getMoneyGained().toString());
        holder.showingDate.setText(accrual.getEntryDate());

    }

    @NonNull
    @Override
    public RecyclerAdapterAccrual.AccrualViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accrual_layout, parent, false);
        RecyclerAdapterAccrual.AccrualViewHolder accrualViewHolder = new RecyclerAdapterAccrual.AccrualViewHolder(view);

        return accrualViewHolder;
    }

    @Override
    public int getItemCount() {
        return accruals.size();
    }

    public static class AccrualViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener
    {
        int accrualId;
        TextView title;
        TextView sumOfMoney;
        TextView date;
        TextView showingTitle;
        TextView showingSumOfMoney;
        TextView showingDate;


        public AccrualViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleAccrualView);
            sumOfMoney = itemView.findViewById(R.id.moneyGainedView);
            date = itemView.findViewById(R.id.dateAccrualView);
            showingTitle = itemView.findViewById(R.id.showingTitleAccrualView);
            showingSumOfMoney = itemView.findViewById(R.id.showingMoneyGainedView);
            showingDate = itemView.findViewById(R.id.showingDateAccrualView);
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
                                    Intent intent = new Intent(view.getContext(), EditEntryActivity.class);
                                    intent.putExtra("ENTRY_TYPE", "EXPENSE");
                                    intent.putExtra("ENTRY_ID", accrualId);
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
