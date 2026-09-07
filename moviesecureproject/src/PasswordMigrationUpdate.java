//try-with-resources 문법을 적용
//try (자원_선언_및_생성) { ... } : try 문 소괄호 () 안에 선언된 자원(resource)들은,
// .close()를 적지 않아도 try 블록이 끝나면 자동으로 반납

/*try-with-resources를 사용하면, try (...) 괄호 안에 선언된 DB 자원 객체들이
작업 완료 후(또는 예외 발생 시) 생성된 역순으로 자동으로 .close() 됨.
따라서 변수를 null로 미리 선언하거나 finally 블록에서 일일이 닫아줄 필요가 없어
코드가 훨씬 깔끔하고 안전*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordMigrationUpgrade {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/movie_db";
        String dbUser = "root";
        String dbPassword = "sqlclass";

        String selectSql =
                "SELECT m_id, user_id, user_password " +
                        "FROM member " +
                        "WHERE user_password_hash IS NULL";

        String updateSql =
                "UPDATE member " +
                        "SET user_password_hash = ?, " +
                        "password_salt = ? " +
                        "WHERE m_id = ?";

        // 1. Connection, PreparedStatement 객체들을 try(...) 소괄호 내에 선언 및 생성
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             // 2. SELECT문 실행 결과인 ResultSet도 nested try-with-resources로 생성
             ResultSet rs = selectStmt.executeQuery())
        {

            System.out.println("MySQL 연결 성공!");

            int count = 0;

            while (rs.next()) {

                int memberId = rs.getInt("m_id");
                String userId = rs.getString("user_id");
                String password = rs.getString("user_password");

                String salt = PasswordUtil.generateSalt();

                String passwordHash = PasswordUtil.hashPassword(password, salt);

                updateStmt.setString(1, passwordHash);
                updateStmt.setString(2, salt);
                updateStmt.setInt(3, memberId);

                int result = updateStmt.executeUpdate();

                if (result == 1) {
                    count++;
                    System.out.println(userId + " 회원 변환 완료");
                }
            }

            System.out.println("\n총 " + count + "명의 비밀번호를 변환했습니다.");

        } catch (SQLException e) {
            System.out.println("데이터베이스 처리 중 오류가 발생했습니다.");
            System.out.println("오류 내용: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("비밀번호 해시 처리 중 오류가 발생했습니다.");
            System.out.println("오류 내용: " + e.getMessage());
        }
        // finally 블록 및 rs.close(), stmt.close(), conn.close() 예외 처리가 필요 없음
    }
}