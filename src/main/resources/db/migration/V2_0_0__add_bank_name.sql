alter table banks
add bank_name varchar(100) unique;

update banks
set bank_name = 'first'
where bank_id = 'e2edbcab-1164-4bd0-b5d3-6400ee624eca';

update banks
set bank_name = 'second'
where bank_id = '407bccab-4184-47e2-b1b1-9267a9ff4a53';

update banks
set bank_name = 'third'
where bank_id = 'e75bb264-e442-4d68-a873-529375fc17a5';