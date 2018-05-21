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
import entities.Constraint;

public class RecyclerAdapterConstraint extends RecyclerView.Adapter<RecyclerAdapterConstraint.ConstraintViewHolder>{

    private ArrayList<Constraint> constraints;

    public RecyclerAdapterConstraint(ArrayList<Constraint> constraints) {
        this.constraints = constraints;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterConstraint.ConstraintViewHolder holder, int position) {

        Constraint constraintId = constraints.get(position);
        holder.showingConstraintLimit.setText(constraintId.getMoneyLimit().toString());
        holder.showingFirstBorderWarning.setText(constraintId.getWarningMoneyBorder().toString());
        holder.showingDateBegin.setText(constraintId.getDateOfBegin());
        holder.showingDateEnd.setText(constraintId.getDateOfEnd());

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