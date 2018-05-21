package adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.master.android_finance_manager.AccountsActivities.EditAccountActivity;
import com.example.master.android_finance_manager.R;

import java.util.ArrayList;

import entities.Account;

import static android.content.Context.MODE_PRIVATE;
import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_APP;

public class RecyclerAdapterAccount extends RecyclerView.Adapter<RecyclerAdapterAccount.AccountViewHolder>{

    private ArrayList<Account> accounts;

    public final static String CURRENT_ACCOUNT_ID = "current_account_id";

    public RecyclerAdapterAccount(ArrayList<Account> accounts){

        this.accounts = accounts;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterAccount.AccountViewHolder holder, int position) {

        Account account = accounts.get(position);
        holder.title.setText(account.getAccountTitle());
        holder.sumOfMoney.setText(account.getAmountOfMoney().toString());
        holder.selectedAccountId = account.getAccountId();

    }

    @NonNull
    @Override
    public RecyclerAdapterAccount.AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_layout, parent, false);
        RecyclerAdapterAccount.AccountViewHolder accountViewHolder = new RecyclerAdapterAccount.AccountViewHolder(view);

        return accountViewHolder;
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener
    {
        int selectedAccountId;
        TextView title;
        TextView sumOfMoney;


        public AccountViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            title = itemView.findViewById(R.id.chooseAccountTitleView);
            sumOfMoney = itemView.findViewById(R.id.chooseAccountSumOfMoneyView);
        }

        @Override
        public void onClick(View view) {
            SharedPreferences preferences = view.getContext().getSharedPreferences(CURRENT_APP, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(CURRENT_ACCOUNT_ID, this.selectedAccountId);
            editor.apply();
            Activity actToFinish = (Activity) view.getContext();
            Intent intent = new Intent();
            actToFinish.setResult(Activity.RESULT_OK, intent);
            actToFinish.finish();
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
                                    Intent intent = new Intent(view.getContext(), EditAccountActivity.class);
                                    intent.putExtra("account_id", selectedAccountId);
                                    ((Activity)view.getContext()).startActivityForResult(intent, 0);
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
