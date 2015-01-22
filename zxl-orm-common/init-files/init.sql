--更改mysql远程连接权限
mysqladmin -uroot -p password 123456
mysql -uroot -p123456
use mysql;
update user set host = '%' where user = 'root' and host='localhost';
flush privileges;

--创建系统配置表
create table core_system_config
(
  id varchar(36) not null,
  create_date date,
  modify_date date,
  config_key varchar(500),
  config_value varchar(2000),
  primary key(id)         
)engine=innodb default charset=utf8;

--插入初始化的系统配置
insert into core_system_config (id,create_date,modify_date,config_key,config_value)
values ("791acd05-8018-44f7-bb5f-785aa3000566",now(),now(),"multest.username","root");
insert into core_system_config (id,create_date,modify_date,config_key,config_value)
values ("f9a5435c-0f9f-4651-a27d-c39c33c55af9",now(),now(),"multest.password","123456");
insert into core_system_config (id,create_date,modify_date,config_key,config_value)
values ("eccef715-f237-4a5a-ae25-96f60e52d18c",now(),now(),"multest.driverClassName","com.mysql.jdbc.Driver");
insert into core_system_config (id,create_date,modify_date,config_key,config_value)
values ("d22eaa2f-667b-4c37-b19e-66bdba0ed92a",now(),now(),"multest.url","jdbc:mysql://localhost:3306/test");
