alter table user add column xfz_no varchar(255) DEFAULT null;
alter table user add column zwy_no varchar(255) DEFAULT null;
alter table user add column zwy_level varchar(255) DEFAULT null;
alter table schedule_info_template add column serial_number varchar(20) default NULL;
alter table schedule_info add column user_leave varchar(255) DEFAULT null;