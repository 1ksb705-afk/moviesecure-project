import java.sql.*;


public class MemberList {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/movie_db";
        String user = "root";
        String password = "sql12345";

        try {
            Connection conn =
                    DriverManager.getConnection(url, user, password);
            String sql = "SELECT * FROM member";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("[영화관 회원 목록]");
            while (rs.next()){
                int memberId = rs.getInt("m_id");
                String userId = rs.getString("user_id");
                String memberName = rs.getString("m_name");
                String memberRole = rs.getString("m_role");

                System.out.println(memberId + "/" + userId + "/" + memberName + "/" + memberRole);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("데이터베이스 오류입니다");

            System.out.println("오류 내용:" + e.getMessage()
            );
        }
    }
}