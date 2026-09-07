use movie_db;
-- user_password_hash VARCHAR(255)
-- password_salt VARCHAR(100)
desc member;

alter table member 
add user_password_hash VARCHAR(255),
add password_salt VARCHAR(100);

desc member;
select * from member;