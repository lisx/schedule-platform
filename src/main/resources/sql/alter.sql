alter table user add column xfz_no varchar(20) DEFAULT null;
alter table user add column zwy_no varchar(20) DEFAULT null;
alter table user add column zwy_level varchar(20) DEFAULT null;
alter table user add column is_leave varchar(2) not null DEFAULT '0';
alter table user add column staff_code varchar(20) DEFAULT null;
alter table schedule_info_template add column serial_number varchar(20) default NULL;
alter table schedule_info add column user_leave varchar(20) DEFAULT null;