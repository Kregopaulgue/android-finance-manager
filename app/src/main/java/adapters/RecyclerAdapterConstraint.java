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
import entities.Accrual;
import entities.BillReminder;
import entities.Constraint;
import entities.Goal;

import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_ACCOUNT_ID;
import static com.example.master.android_finance_manager.FinanceManagerActivity.CURRENT_APP;

public class RecyclerAdapterConstraint extends RecyclerView.Adapter<RecyclerAdapterConstraint.ConstraintViewHolder>{

    private ArrayList<Constraint> constraints;

    public RecyclerAdapterConstraint(ArrayList<Constraint> constraints) {
        this.constraints = constraints;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterConstraint.ConstraintViewHolder holder, int position) {

        Constraint constraint = constraints.get(position);
        holder.constraintId = constraint.getConstraintId();
        holder.showingConstraintLimit.setText(constraint.getMoneyLimit().toString());
        holder.showingFirstBorderWarning.setText(constraint.getWarningMoneyBorder().toString());
        holder.showingDateBegin.setText(constraint.getDateOfBegin());
        holder.showingDateEnd.setText(constraint.getDateOfEnd());

    }

    @NonNull
    @Override
    public RecyclerAdapterConstraint.ConstraintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.constraint_layout, parent, false);
        RecyclerAdapterConstraint.ConstraintViewHolder constraintViewHolder = new RecyclerAdapterConstraint.ConstraintViewHolder(view);

        return constraintViewHolder;
    }

    @Override
    public int getItemCount() {
        return constraints.size();
    }

    public static class ConstraintViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener
    {
        int constraintId;
        TextView constraintLimit;
        TextView firstBorderWarning;
        TextView dateBegin;
        TextView dateEnd;

        TextView showingConstraintLimit;
        TextView showingFirstBorderWarning;
        TextView showingDateBegin;
        TextView showingDateEnd;



        public ConstraintViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.constraintLimit = itemView.findViewById(R.id.constraintLimitView);
            this.firstBorderWarning = itemView.findViewById(R.id.firstBorderWarningView);
            this.dateBegin = itemView.findViewById(R.id.dateBeginView);
            this.dateEnd = itemView.findViewById(R.id.dateEndView);

            this.showingConstraintLimit = itemView.findViewById(R.id.showingLimitView);
            this.showingFirstBorderWarning = itemView.findViewById(R.id.showingFirstBorderView);
            this.showingDateBegin = itemView.findViewById(R.id.showingBeginDateView);
            this.showingDateEnd = itemView.findViewById(R.id.showingEndDateView);
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
                                    addConstraintDialog(view);
                                    return true;
                                case R.id.deleteMenuItem:
                                    FinancialManagerDbHelper dbHelper = new FinancialManagerDbHelper(view.getContext());
                                    Constraint constraint = new Constraint();
                                    constraint.readFromDatabase(dbHelper, constraintId);
                                    constraint.deleteFromDatabase(dbHelper);
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

        private void addConstraintDialog(final View view) {

            LayoutInflater li = LayoutInflater.from(view.getContext());
            View promptsView = li.inflate(R.layout.add_constr, null);

            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(view.getContext());

            mDialogBuilder.setView(promptsView);

            final EditText constraintMoneyLimit = promptsView.findViewById(R.id.constraintLimitInput);
            final EditText firstBorder = promptsView.findViewById(R.id.firstConstraintBorderInput);
            final Button selectDateBegin = promptsView.findViewById(R.id.selectBeginDateButt);
            final Button selectDateEnd = promptsView.findViewById(R.id.selectEndDateButt);

            selectDateBegin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Calendar now = Calendar.getInstance();
                    final Calendar c = Calendar.getInstance();

                    DatePickerDialog dpd = new DatePickerDialog(v.getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    selectDateBegin.setText(dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year);

                                }
                            }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                    dpd.show();


                }
            });

            selectDateEnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();

                    DatePickerDialog dpd = new DatePickerDialog(v.getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    selectDateEnd.setText(dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year);

                                }
                            }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                    dpd.show();


                }
            });

            mDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("ADD CONSTRAINT",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    showingConstraintLimit.setText(constraintMoneyLimit.getText().toString());
                                    showingFirstBorderWarning.setText(showingFirstBorderWarning.getText().toString());
                                    showingDateBegin.setText(showingDateBegin.getText().toString());
                                    showingDateEnd.setText(showingDateEnd.getText().toString());

                                    Constraint constraint = new Constraint();

                                    FinancialManagerDbHelper dbHelper = new FinancialManagerDbHelper(view.getContext());
                                    Account mParentAccount = new Account();

                                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(CURRENT_APP, Context.MODE_PRIVATE);
                                    int parentAccountId = sharedPreferences.getInt(CURRENT_ACCOUNT_ID, 1);
                                    mParentAccount.readFromDatabase(dbHelper, parentAccountId);

                                    constraint.setMoneyLimit(Double.parseDouble(constraintMoneyLimit.getText().toString()));
                                    constraint.setWarningMoneyBorder(Double.parseDouble(firstBorder.getText().toString()));
                                    constraint.setDateOfBegin(selectDateBegin.getText().toString());
                                    constraint.setDateOfEnd(selectDateEnd.getText().toString());
                                    constraint.setIsFinished("false");
                                    constraint.setConstraintId(constraintId);
                                    constraint.setParentAccount(mParentAccount);
                                    constraint.updateToDatabase(dbHelper, constraintId);
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