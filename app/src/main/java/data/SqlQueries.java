package data;

public class SqlQueries {

    public static final String SQL_CREATE_TABLES = "create table accounts\n" +
            "(\n" +
            "    account_id integer primary key autoincrement,\n" +
            "    amount_of_money real not null default 0.0,\n" +
            "    title text not null,\n" +
            "    account_type text not null\n" +
            ");\n" +
            "create table categories\n" +
            "(\n" +
            "    category_id integer primary key autoincrement,\n" +
            "    title text default null\n" +
            ");\n" +
            "create table tags\n" +
            "(\n" +
            "    tag_id integer primary key autoincrement,\n" +
            "    title text,\n" +
            "    tag_type text,\n" +
            "    tategory_id integer not null,\n" +
            "    foreign key(category_id) references categories(category_id)\n" +
            "    on delete cascade\n" +
            ");\n" +
            "create table finance_entries\n" +
            "(\n" +
            "    entry_id integer primary key autoincrement,\n" +
            "    date_time text,\n" +
            "    entry_type text,\n" +
            "    comment text null,\n" +
            "    account_id integer not null,\n" +
            "    foreign key(account_id) references accounts(account_id)\n" +
            "    on delete cascade\n" +
            ");\n" +
            "create table accruals\n" +
            "(\n" +
            "    source text,\n" +
            "    money_gained real,\n" +
            "    entry_id  integer not null,\n" +
            "    account_id  integer not null\n" +
            ");\n" +
            "create table expenses\n" +
            "(\n" +
            "    money_spent real,\n" +
            "    entry_id  integer not null,\n" +
            "    account_id  integer not null\n" +
            ");\n" +
            "create table entry_tag_binders\n" +
            "(\n" +
            "    bind_id  integer primary key autoincrement,\n" +
            "    entry_id  integer not null,\n" +
            "    account_id  integer not null,\n" +
            "    tag_id  integer not null,\n" +
            "    category_id  integer not null,\n" +
            "    foreign key(tag_id) references tags(tag_id)\n" +
            "    on delete cascade,\n" +
            "    foreign key(entry_id) references finance_entries(entry_id)\n" +
            "    on delete cascade,\n" +
            "    foreign key(account_id) references accounts(account_id)\n" +
            "    on delete cascade,\n" +
            "    foreign key(category_id) references categories(category_id)\n" +
            "    on delete cascade\n" +
            ");\n" +
            "create table bill_reminders\n" +
            "(\n" +
            "    bill_id integer primary key autoincrement,\n" +
            "    sum_to_pay real,\n" +
            "    bill_description text,\n" +
            "    date_time_to_pay text,\n" +
            "    account_id integer not null,\n" +
            "    foreign key(account_id) references accounts(account_id)\n" +
            "    on delete cascade\n" +
            ");\n" +
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
            ");\n" +
            "create table goals\n" +
            "(\n" +
            "    goal_id integer primary key autoincrement,\n" +
            "    sum_to_reach real,\n" +
            "    current_sum real,\n" +
            "    target_description text,\n" +
            "    is_reached boolean,\n" +
            "    account_id integer not null,\n" +
            "    foreign key(account_id) references accounts(account_id)\n" +
            "    on delete cascade\n" +
            ");";
}