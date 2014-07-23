create table weibo_user(
	id int unsigned not null auto_increment primary key,
	sid varchar(50) UNIQUE KEY,
	sex int,
	province int,
	city int,
	location varchar(50),
	description varchar(200),
	followers_count int,
	friends_count int,
	statuses_count int,
	head varchar(100),
	name varchar(50),
	nick varchar(50),
	src int,
	creattime int
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

create table weibo_info(
	id int unsigned not null auto_increment primary key,
	sid varchar(50) UNIQUE KEY,
	uid varchar(50),
	source varchar(300),
	image varchar(100),
	geo_lat varchar(50),
	geo_lon varchar(50),
	timestamp int,
	head varchar(100),
	name varchar(50),
	nick varchar(50),
	text varchar(300),
	src int,
	creattime int
	foreign key(name) references weibo_user(name) on delete cascade
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

select count(weibo_info.id) as count from weibo_info