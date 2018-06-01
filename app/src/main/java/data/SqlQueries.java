package data;

public class SqlQueries {

    public static final String SQL_CREATE_TABLE_ACCOUNTS =
            "create table accounts\n" +
            "(\n" +
            "    account_id integer primary key autoincrement,\n" +
            "    amount_of_money real not null default 0.0,\n" +
            "    title text not null,\n" +
            "    account_type text not null\n" +
            ");\n";
    public static final String SQL_CREATE_TABLE_CATEGORY = "create table categories\n" +
            "(\n" +
            "    category_id integer primary key autoincrement,\n" +
            "    title text not null\n" +
            ");\n";
    public static final String SQL_CREATE_TABLE_TAGS =
            "create table tags\n" +
            "(\n" +
            "    tag_id integer primary key autoincrement,\n" +
            "    title text not null,\n" +
            "    tag_type text not null,\n" +
            "    category_id integer not null,\n" +
            "    foreign key(category_id) references categories(category_id)\n" +
            "    on delete cascade\n" +
            ");\n";
    public static final String SQL_CREATE_FINANCE_ENTRIES =
            "create table finance_entries\n" +
            "(\n" +
            "    entry_id integer primary key autoincrement,\n" +
            "    date_time text not null,\n" +
            "    entry_type text not null,\n" +
            "    comment text null,\n" +
            "    title text not null,\n" +
            "    account_id integer not null,\n" +
            "    foreign key(account_id) references accounts(account_id)\n" +
            "    on delete cascade\n" +
            ");\n";
    public static final String SQL_CREATE_TABLE_ACCRUALS = "create table accruals\n" +
            "(\n" +
            "    source text null,\n" +
            "    money_gained real not null,\n" +
            "    entry_id  integer not null,\n" +
            "    foreign key(entry_id) references finance_entries(entry_id)\n" +
            "    on delete cascade\n" +
            ");\n";
    public static final String SQL_CREATE_TABLE_EXPENSES =
            "create table expenses\n" +
            "(\n" +
            "    money_spent real not null,\n" +
            "    importance int null,\n" +
            "    entry_id  integer not null,\n" +
            "    foreign key(entry_id) references finance_entries(entry_id)\n" +
            "    on delete cascade\n" +
            ");\n";
    public static final String SQL_CREATE_TABLES_ENTRY_TAG_BINDERS =
            "create table entry_tag_binders\n" +
            "(\n" +
            "    bind_id  integer primary key autoincrement,\n" +
            "    entry_id  integer not null,\n" +
            "    tag_id  integer not null,\n" +
            "    foreign key(tag_id) references tags(tag_id)\n" +
            "    on delete cascade,\n" +
            "    foreign key(entry_id) references finance_entries(entry_id)\n" +
            "    on delete cascade\n" +
            ");\n";
    public static final String SQL_CREATE_TABLE_BILL_REMINDERS =
            "create table bill_reminders\n" +
            "(\n" +
            "    bill_id integer primary key autoincrement,\n" +
            "    sum_to_pay real not null,\n" +
            "    bill_title text not null,\n" +
            "    date_time_to_pay text not null,\n" +
            "    is_paid text,\n" +
            "    account_id integer not null,\n" +
            "    foreign key(account_id) references accounts(account_id)\n" +
            "    on delete cascade\n" +
            ");\n";
    public static final String SQL_CREATE_TABLE_CONSTRAINTS =
            "create table constraints\n" +
            "(\n" +
            "    constraint_id integer primary key autoincrement,\n" +
            "    money_limit real,\n" +
            "    date_of_begin text,\n" +
            "    date_of_end text,\n" +
            "    warning_money_border real,\n" +
            "    is_finished text,\n" +
            "    account_id integer not null,\n" +
            "    foreign key(account_id) references accounts(account_id)\n" +
            "    on delete cascade\n" +
            ");\n";
    public static final String SQL_CREATE_TABLE_GOALS =
            "create table goals\n" +
            "(\n" +
            "    goal_id integer primary key autoincrement,\n" +
            "    sum_to_reach real not null,\n" +
            "    current_sum real not null,\n" +
            "    target_title text not null,\n" +
            "    is_reached text,\n" +
            "    account_id integer not null,\n" +
            "    foreign key(account_id) references accounts(account_id)\n" +
            "    on delete cascade\n" +
            ");";


    public static final String SQL_INSERT_CATEGORY_FOOD =
            "insert into categories(title) values('FOOD');";
    public static final String SQL_INSERT_CATEGORY_SERVICE =
            "insert into categories(title) values('SERVICE');";
    public static final String SQL_INSERT_CATEGORY_APPLIANCE =
            "insert into categories(title) values('APPLIANCE');";
    public static final String SQL_INSERT_CATEGORY_CLOTH =
            "insert into categories(title) values('CLOTH');";
    public static final String SQL_INSERT_CATEGORY_ACCRUAL =
            "insert into categories(title) values('ACCRUAL');";
    public static final String SQL_INSERT_CATEGORY_OTHER =
            "insert into categories(title) values('OTHER');";

    public static final String SQL_TRIGGER_INSERT_EXPENSE =
            "create trigger deduct_amount_of_money after insert on expenses " +
                    "begin" +
                    "   UPDATE accounts SET amount_of_money = (amount_of_money - new.money_spent) WHERE account_id IN(SELECT account_id FROM finance_entries WHERE entry_id = new.entry_id);" +
                    "end;";
    public static final String SQL_TRIGGER_INSERT_ACCRUAL =
            "create trigger add_amount_of_money after insert on accruals " +
                    "begin" +
                    "   UPDATE accounts SET amount_of_money = (amount_of_money + new.money_gained) WHERE account_id IN(SELECT account_id FROM finance_entries WHERE entry_id = new.entry_id);" +
                    "end;";
    public static final String SQL_TRIGGER_DELETE_EXPENSE =
            "create trigger add_amount_of_money_del after delete on expenses " +
                    "begin" +
                    "   UPDATE accounts SET amount_of_money = (amount_of_money + old.money_spent) WHERE account_id IN(SELECT account_id FROM finance_entries WHERE entry_id = old.entry_id);" +
                    "end;";
    public static final String SQL_TRIGGER_DELETE_ACCRUAL =
            "create trigger deduct_amount_of_money_del after delete on accruals " +
                    "begin" +
                    "   UPDATE accounts SET amount_of_money = (amount_of_money - old.money_gained) WHERE account_id IN(SELECT account_id FROM finance_entries WHERE entry_id = old.entry_id);" +
                    "end;";
}