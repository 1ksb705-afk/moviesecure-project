import java.sql.*;
import java.util.Scanner;

public class MemberInsert{
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/movie_db";
        // MySQL 사용자 계정
        String user = "root";
        // MySQL 비밀번호
        String password = "sql12345";
        Scanner scanner =new Scanner(System.in);
        try {
            Connection conn =
                    DriverManager.getConnection(url, user, password);
            System.out.println("[영화관 회원 가입]");
            System.out.print("아이디: ");
            String inputId = scanner.nextLine();

            System.out.print("비밀번호: ");
            String inputPass = scanner.nextLine();

            System.out.print("이름: ");
            String inputName = scanner.nextLine();

            System.out.print("회원 권한: ");
            String inputRole = scanner.nextLine();

            String sql =
                    "INSERT INTO member " +
                            "(user_id, user_password, m_name, m_role) " +
                            "VALUES(?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, inputId);
            stmt.setString(2, inputPass);
            stmt.setString(3, inputName);
            stmt.setString(4, inputRole);
            System.out.println("\n실행할 SQL:");
            System.out.println(sql);
            //ResultSet rs = stmt.executeQuery(); select문 만 해당
            int result = stmt.executeUpdate();
            // 삽입/수정/삭제 실행
            // 결과행 개수를 받음
            if (result == 1) {
                System.out.println("\n 회원가입 성공");
            } else {
                System.out.println("\n 회원가입 실패");
            }
            stmt.close();
            conn.close();
        }
        catch (SQLException e) {
            System.out.println(
                    "데이터베이스 오류가 발생했습니다.");
            System.out.println(e.getMessage());
        }
        finally{
            scanner.close();
        }
    }
}