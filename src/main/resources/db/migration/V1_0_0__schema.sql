create table if not exists clients (
client_id UUID primary key,
first_name VARCHAR(55) not null,
last_name VARCHAR(55) not null,
patronymic VARCHAR(55),
phone_number VARCHAR(10) not null unique,
email VARCHAR(100) not null unique,
passport VARCHAR(10) not null unique
);

create table if not exists banks(
bank_id UUID primary key
);

create table if not exists credits (
credit_id UUID primary key,
bank_id UUID foreign key REFERENCES banks(bank_id),
credit_limit DECIMAL(12,2) not null,
credit_rate DECIMAL(4,2) not null
);

create table if not exists credit_offers (
credit_offer_id UUID primary key,
client_id UUID foreign key references clients(client_id),
credit_id UUID foreign key references credits(credit_id),
credit_amount DECIMAL not null,
month_count int not null
);

create table if not exists payments(
payment_id UUID primary key,
date DATE not null,
credit_offer_id UUID foreign key references credit_offers(credit_offer_id),
amount DECIMAL not null,
main_part DECIMAL not null,
percent_part DECIMAL not null
);

