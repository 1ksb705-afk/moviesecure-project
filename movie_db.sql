create database movie_db;
USE movie_db;
CREATE TABLE member(
	
    m_id int PRIMARY KEY auto_increment,  
	user_id varchar(30) not null unique,
    user_password varchar(255) not null,
    m_name varchar(30) not null ,
    m_role varchar(10) not null default 'user',
    created_at datetime default CURRENT_TIMESTAMP
);
desc member;
INSERT into member(
	user_id,user_password,m_name)
	VALUES
	('hong',1234,'홍길동'),
	('kim',5678,'김유신'),
	('heo',7777,'허준'),
	('jeong',9999,'정약용');
SELECT * FROM member;

INSERT into member(
	user_id,user_password,m_name,m_role)
    VALUES ('admin', 'admin1234','관리자','ADMIN');
SELECT * FROM member;