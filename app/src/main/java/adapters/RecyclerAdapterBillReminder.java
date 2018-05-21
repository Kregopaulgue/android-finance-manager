package adapters;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.master.android_finance_manager.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import data.FinancialManagerDbHelper;
import entities.Account;
import entities.BillReminder;

import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_ACCOUNT_ID;
import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_APP;

public class RecyclerAdapterBillReminder extends RecyclerView.Adapter<RecyclerAdapterBillReminder.BillReminderViewHolder>{

    private ArrayList<BillReminder> billReminders;

    public RecyclerAdapterBillReminder(ArrayList<BillReminder> billReminders) {
        this.billReminders = billReminders;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterBillReminder.BillReminderViewHolder holder, int position) {

        BillReminder billReminderId = billReminders.get(position);
        holder.billId = billReminderId.getBillReminderId();
        holder.showingBillTitle.setText(billReminderId.getBillTitle());
        holder.showingSumOfMoneyToPay.setText(billReminderId.getSumToPay().toString());
        holder.showingDateToPay.setText(billReminderId.getDateTimeToPay());

    }

    @NonNull
    @Override
    public RecyclerAdapterBillReminder.BillReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_reminder_layout, parent, false);
        RecyclerAdapterBillReminder.BillReminderViewHolder billReminderViewHolder
                = new RecyclerAdapterBillReminder.BillReminderViewHolder(view);

        return billReminderViewHolder;
    }

    @Override
    public int getItemCount() {
        return billReminders.size();
    }

    public static class BillReminderViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener
    {
        int billId;
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

        private void addBillReminderDialog(final View view) {

            LayoutInflater li = LayoutInflater.from(view.getContext());
            View promptsView = li.inflate(R.layout.add_bill_reminder, null);

            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(view.getContext());

            mDialogBuilder.setView(promptsView);

            final EditText titleInput = promptsView.findViewById(R.id.billTitleInput);
            final EditText sumToPayInput = promptsView.findViewById(R.id.sumToPayInput);
            final Button selectBillDate = promptsView.findViewById(R.id.selectDateBill);

            selectBillDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();

                    DatePickerDialog dpd = new DatePickerDialog(v.getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    selectBillDate.setText(dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year);

                                }
                            }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                    dpd.show();


                }
            });
            mDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("EDIT BILL REMINDER",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    //Вводим текст и отображаем в строке ввода на основном экране:
                                    showingBillTitle.setText(titleInput.getText().toString());
                                    showingSumOfMoneyToPay.setText(sumToPayInput.getText().toString());
                                    showingDateToPay.setText(selectBillDate.getText().toString());

                                    FinancialManagerDbHelper dbHelper = new FinancialManagerDbHelper(view.getContext());
                                    Account mParentAccount = new Account();

                                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(CURRENT_APP, Context.MODE_PRIVATE);
                                    int parentAccountId = sharedPreferences.getInt(CURRENT_ACCOUNT_ID, 1);
                                    mParentAccount.readFromDatabase(dbHelper, parentAccountId);

                                    BillReminder billToUpdate = new BillReminder();
                                    billToUpdate.setBillReminderId(billId);
                                    billToUpdate.setBillTitle(showingBillTitle.getText().toString());
                                    billToUpdate.setSumToPay(Double.parseDouble(showingSumOfMoneyToPay.getText().toString()));
                                    billToUpdate.setIsPaid("false");
                                    billToUpdate.setParentAccount(mParentAccount);
                                    billToUpdate.updateToDatabase(dbHelper, billId);
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
