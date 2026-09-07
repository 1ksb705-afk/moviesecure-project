CREATE DATABASE movie_db;
USE movie_db;
CREATE TABLE member (
    m_id INT AUTO_INCREMENT PRIMARY KEY, #회원번호 
    user_id VARCHAR(30) NOT NULL UNIQUE, #로그인 아이디
    user_password VARCHAR(255) NOT NULL, #로그인 비밀번호
    m_name VARCHAR(30) NOT NULL, #회원 이름
    m_role VARCHAR(10) NOT NULL DEFAULT 'USER', #회원 권한
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP #가입날짜와 시간 
);
desc member;
INSERT INTO member(
		user_id, user_password, m_name)
        values
       ('hong','1234','홍길동'),
       ('kim','5678','김유신'),
       ('heo','7777','허준'),
       ("jeong", '9999', '정약용');
SELECT * from member;

INSERT INTO member(
		user_id, user_password, m_name, m_role)
        VALUES  ('admin', 'admin1234', '관리자', 'ADMIN');
        
SELECT * from member; 