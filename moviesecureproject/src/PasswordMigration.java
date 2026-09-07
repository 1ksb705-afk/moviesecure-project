import java.sql.*;

public class PasswordMigration {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/movie_db";
        // MySQL 사용자 계정
        String user = "root";
        // MySQL 비밀번호
        String password = "sql12345";

        String sql =
                "select m_id, user_id, user_password " +
                        "from member " +
                        "where user_password_hash IS NULL";
        String updateSql =
                "update member " +
                        "set user_password_hash = ?, " +
                        "password_salt = ? " +
                        "where m_id = ? ";
        Connection conn = null;
        PreparedStatement selectStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null; // select 결과 저장

        try {
            conn =
                    DriverManager.getConnection(url, user, password);
            System.out.println("mysql 연결 성공!");
            // 해시값이 없는 회원을 조회할 개체 생성
            selectStmt = conn.prepareStatement(sql);
            // select문 실행하여 결과 받음
            rs = selectStmt.executeQuery();
            // 해시값과 salt값을 수정 (값을 넣어줌)
            updateStmt = conn.prepareStatement(updateSql);
            // 변환된 회원수
            int count = 0;
            while (rs.next()) {
                int memberId = rs.getInt("m_id");
                String userId = rs.getString("user_id");
                String pass = rs.getString("user_password");
                // 솔트 생성
                String salt = PasswordUtil.generateSalt();
                // 해시값 생성
                String passHash =
                        PasswordUtil.hashPassword(pass, salt);
                updateStmt.setString(1, passHash);
                updateStmt.setString(2, salt);
                updateStmt.setInt(3, memberId);
                // 삽입, 수정, 삭제 -> executeUpdate()
                // 결과를 행의 개수로 가져옴
                int result = updateStmt.executeUpdate();

                if (result == 1) {
                    count++;
                    System.out.println(userId + " 회원 수정 완료");
                }
            }
            System.out.println("\n 총 " + count + "명이 수정되었습니다");
        } catch (SQLException e) {
            System.out.println("데이터베이스 오류가 발생했습니다.");
            System.out.println("오류 내용: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("기타 오류입니다.");
            System.out.println("오류 내용:" + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (updateStmt != null) {
                    updateStmt.close();
                }
                if (selectStmt != null) {
                    selectStmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e){
                System.out.println("객체종료 오류입니다");
                System.out.println("오류 내용: " + e.getMessage());
            }
        }
    }
}