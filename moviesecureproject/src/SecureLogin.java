import java.sql.*;
import java.util.Scanner;


public class SecureLogin {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/movie_db";
        String user = "root";
        String password = "sql12345";
        Scanner scanner = new Scanner(System.in);
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("[영화관 회원 로그인]");
            System.out.println("아이디:");
            String inputId = scanner.nextLine();
            System.out.println("비밀번호:");
            String inputPass = scanner.nextLine();
            String sql =
                    "select m_id, user_id, m_name, m_role, " +
                            "user_password_hash, password_salt " +
                            "from member " +
                            "where user_id = ? " ;


            PreparedStatement stmt = conn.prepareStatement(sql);
            // 첫번째 ?에 아이디 입력
            stmt.setString(1, inputId);

            System.out.println("\n 실행할 sql문");
            System.out.println(sql);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String saveHash = rs.getString("user_password_hash");
                String saveSalt = rs.getString("password_salt");

                boolean passwordCorrect =
                        PasswordUtil.verifyPassword(
                                inputPass, saveSalt, saveHash);
                if (passwordCorrect) {
                    String memberName = rs.getString("m_name");
                    String memberRole = rs.getString("m_role");

                    System.out.println("\n로그인 성공!!");
                    System.out.println(memberName + "님 환영합니다");
                    System.out.println("회원권한:" + memberRole);
                } else {
                    System.out.println("아이디나 비밀번호가 일치하지 않습니다.");
                }
                rs.close();
                stmt.close();
                conn.close();
            }
            } catch(SQLException e){
                System.out.println("데이터베이스 오류입니다");
                System.out.println("오류 내용:" + e.getMessage());
            } catch(Exception e){
                System.out.println("비밀번호 확인 중 오류입니다");
                System.out.println("오류 내용:" + e.getMessage());
            }
            finally{
                scanner.close();
            }
        }
    }

