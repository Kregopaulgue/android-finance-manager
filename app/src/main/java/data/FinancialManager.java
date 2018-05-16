package data;

import android.provider.BaseColumns;

public class FinancialManager {

    private FinancialManager() { }

    public static final class Account implements BaseColumns {

        public final static String TABLE_NAME = "accounts";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ACCOUNT_ID = "account_id";
        public final static String COLUMN_AMOUNT_OF_MONEY = "amount_of_money";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_ACCOUNT_TYPE = "account_type";
    }

    public static final class FinancialEntry implements BaseColumns {

        public static final String TABLE_NAME = "finance_expenses";

        public static final String _ID = BaseColumns._ID;
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_ENTRY_ID = "entry_id";
        public final static String COLUMN_DATE_TIME = "date_time";
        public final static String COLUMN_ENTRY_TYPE = "entry_type";
        public final static String COLUMN_COMMENT = "comment";
        public final static String COLUMN_ACCOUNT_ID = "account_id";
    }

    public final static class Category implements BaseColumns {

        public final static String TABLE_NAME = "categories";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CATEGORY_ID = "category_id";
        public final static String COLUMN_TITLE = "title";
    }

    public final static class Tag implements BaseColumns {

        public final static String TABLE_NAME = "tags";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TAG_ID = "tag_id";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_TAG_TYPE = "tag_type";
        public final static String COLUMN_CATEGORY_ID = "category_id";
    }

    public final static class Accrual implements BaseColumns {

        public final static String TABLE_NAME = "accruals";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_SOURCE = "source";
        public final static String COLUMN_MONEY_GAINED = "money_gained";
        public final static String COLUMN_ENTRY_ID = "entry_id";
    }

    public final static class Expense implements BaseColumns {

        public final static String TABLE_NAME = "expenses";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_MONEY_SPENT = "money_spent";
        public final static String COLUMN_ENTRY_ID = "entry_id";
    }

    public final static class EntryTagBinder implements BaseColumns {

        public final static String TABLE_NAME = "entry_tag_binders";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_BIND_ID = "bind_id";
        public final static String COLUMN_ENTRY_ID = "entry_id";
        public final static String COLUMN_ACCOUNT_ID = "account_id";
        public final static String COLUMN_TAG_ID = "tag_id";
        public final static String COLUMN_CATEGORY_ID = "category_id";
    }

    public final static class BillReminder implements BaseColumns {

        public final static String TABLE_NAME = "bill_reminders";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_BILL_ID = "bill_id";
        public final static String COLUMN_SUM_TO_PAY = "sum_to_pay";
        public final static String COLUMN_BILL_DESCRIPTION = "bill_description";
        public final static String COLUMN_DATE_TIME_TO_PAY = "date_time_to_pay";
        public final static String COLUMN_ACCOUNT_ID = "account_id";
    }

    public final static class Constraint implements BaseColumns {

        public final static String TABLE_NAME = "constraints";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CONSTRAINT_ID = "constraint_id";
        public final static String COLUMN_MONEY_LIMIT = "money_limit";
        public final static String COLUMN_DATE_OF_BEGIN = "date_of_begin";
        public final static String COLUMN_DATE_OF_END = "date_of_end";
        public final static String COLUMN_WARNING_MONEY_BORDER = "warning_money_border";
        public final static String COLUMN_IS_CRITICAL = "is_critical";
        public final static String COLUMN_ACCOUNT_ID = "account_id";
    }

    public final static class Goal implements BaseColumns {

        public final static String TABLE_NAME = "goals";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_GOAL_ID = "goal_id";
        public final static String COLUMN_SUM_TO_REACH = "sum_to_reach";
        public final static String COLUMN_CURRENT_SUM = "current_sum";
        public final static String COLUMN_TARGET_DESCRIPTION = "target_description";
        public final static String COLUMN_IS_REACHED = "is_reached";
        public final static String COLUMN_ACCOUNT_ID = "account_id";
    }
}