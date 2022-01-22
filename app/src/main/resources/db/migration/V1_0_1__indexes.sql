create index if not exists credit_bank_id on credits (bank_id);
create index if not exists credit_offer_credit_client on credit_offers (client_id,credit_id);
create index if not exists payment_credit_offer on payments (credit_offer_id);
