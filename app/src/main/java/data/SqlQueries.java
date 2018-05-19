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
            "    title text default null\n" +
            ");\n";
    public static final String SQL_CREATE_TABLE_TAGS =
            "create table tags\n" +
            "(\n" +
            "    tag_id integer primary key autoincrement,\n" +
            "    title text,\n" +
            "    tag_type text,\n" +
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
            "    sum_to_pay real,\n" +
            "    bill_title text,\n" +
            "    date_time_to_pay text,\n" +
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
            "    is_critical boolean,\n" +
            "    account_id integer not null,\n" +
            "    foreign key(account_id) references accounts(account_id)\n" +
            "    on delete cascade\n" +
            ");\n";
    public static final String SQL_CREATE_TABLE_GOALS =
            "create table goals\n" +
            "(\n" +
            "    goal_id integer primary key autoincrement,\n" +
            "    sum_to_reach real,\n" +
            "    current_sum real,\n" +
            "    target_title text,\n" +
            "    is_reached boolean,\n" +
            "    account_id integer not null,\n" +
            "    foreign key(account_id) references accounts(account_id)\n" +
            "    on delete cascade\n" +
            ");";
}