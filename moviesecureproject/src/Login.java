// 시큐어 코딩(Secure Coding):
// 프로그램이 정상적으로 작동하게 만드는 것뿐만 아니라, 잘못된 입력이나 해킹에도 안전하도록 코드를 작성하는 방법

// SQL 인젝션 :
// 사용자 입력 부분에 SQL 명령이나 SQL 문법을 주입하는 공격 프로그램이 사용자 입력값을 안전하게 처리하지 않고
// SQL 문장에 직접 포함할 때, 공격자가 입력값에 SQL 문법을 넣어 원래 SQL문의 구조나 실행 조건을 변경하는 공격

// SQL 인젝션 취약점 발생
// 아이디 입력 -> 'OR '1'='1' #
// 암호 -> 아무거나 입력
// SELECT m_id, user_id, m_name, m_role
//FROM member WHERE user_id = '' OR '1'='1' #' AND
// user_password = 'abc'
import java.sql.*;
import java.util.Scanner;

public class Login {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/movie_db";
        String user = "root";
        String password = "sql12345";
        Scanner scanner = new Scanner(System.in);
        try{
            Connection conn =
                    DriverManager.getConnection(url,user,password);
            System.out.println("[영화관 회원 로그인]");
            System.out.println("아이디:");
            String inputId = scanner.nextLine();
            System.out.println("비밀번호:");
            String inputPass = scanner.nextLine();

            String sql =
                    "SELECT * "+
                    "FROM member "+
                    "WHERE user_id = '"+inputId+"' "+
                    "AND user_password = '"+inputPass+"' " ;
            System.out.println("\n 실행할 sql문");
            System.out.println(sql);
            // 문장을 sql에 전달
            Statement stmt = conn.createStatement();
            // sql문장을 실행 -> 결과 받기
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {
            String memberName = rs.getString("m_name");
            String memberRole = rs.getString("m_role");

            System.out.println("\n로그인 성공!!");
            System.out.println(memberName+ "님 환영합니다");
                System.out.println("회원권한:"+memberRole);
            } else {
                System.out.println("아이디나 비밀번호가 일치하지 않습니다.");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("데이터베이스 오류입니다");
            System.out.println("오류 내용:" + e.getMessage());
        }


        finally{
            scanner.close();
        }
    }
}
