create database zWaiGamesDB collate utf8mb4_general_ci;
use zWaiGamesDB;

create table user(
	user_id int unsigned not null auto_increment primary key,
    username varchar(750) not null unique,
    password varchar(750) not null,
    IP varchar(500) not null
);


drop table user;


select * from user;