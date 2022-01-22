alter table clients add is_removed boolean DEFAULT false not null;

alter table credits add is_unused boolean DEFAULT false not null;

alter table credit_offers add is_canceled boolean DEFAULT false not null;

insert into clients (client_id,first_name,last_name,patronymic,phone_number,email,passport,is_removed)
values
('97592eb7-f025-4e47-bba2-27c1562cf2e2','Nikolay','Nikolaev','Nikolaevich','9458715796','ninini@mail.com','3612785412',true),
('ae334b1e-f328-466f-89f0-6e28ebd82ab5','Elena','Elenovna','','9457518945','elel@mail.com','3614658974',true),
('6dc9e15a-6e36-4673-a3b8-f19385acb67a','Daelin','Proudmoore','','9457578945','daelproud@mail.com','3614658474',true);

insert into credits (credit_id,bank_id,credit_limit,credit_rate,is_unused)
values
('b7fe3347-f547-4737-8dbc-b08d9f25de6c','e2edbcab-1164-4bd0-b5d3-6400ee624eca',1111111.0,16.6,true),
('764acec1-9f25-4ff6-b7ad-c6c299c67bc5','e2edbcab-1164-4bd0-b5d3-6400ee624eca',555555.0,12.2,true),
('b9b28894-c59c-45fc-bf12-896b8d89dbee','e2edbcab-1164-4bd0-b5d3-6400ee624eca',2222222.0,19.9,true);

insert into credit_offers (credit_offer_id, client_id, credit_id, credit_amount, month_count,is_canceled)
values
('46e21b03-e49b-4fa0-b2bc-1d203bf3b9ee','4209789f-3310-4d16-8074-def0d09ae7b0','29f68087-9fee-48aa-bcbe-352182348508',100000.0,12,true),
('49ee0e05-1e2f-4b6f-853e-9c6cf516ed1d','4209789f-3310-4d16-8074-def0d09ae7b0','b641dbf0-1217-4049-b909-3d85d2839c21',200000.0,8,true),
('9d13f4f7-968f-45d9-b799-0dad83c710e2','4209789f-3310-4d16-8074-def0d09ae7b0','f7bc2483-1372-4965-b706-39fc15a42ee8',20000.0,24,true);


