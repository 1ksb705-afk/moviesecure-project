import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class PasswordUtil {
    // 해시 반복 횟수
    private static final int ITERATIONS = 600000;
    // 만들어질 해시값의 길이
    private static final int KEY_LENGTH = 256;
    // 새로운 솔트를 만드는 메서드
    public static String generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        // 바이트형태의 솔트를 DB에 저장하기 쉬운 문자열로 바꿈
        return Base64.getEncoder().encodeToString(salt);
    }
    // 비밀번호를 해시값으로 바꾸는 메서드
    public static String hashPassword(
            String password,
            String salt
    ) throws Exception {
        // Base64 문자열로 된 솔트를 byte 배열로 변환
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        // PBKDF2 해시에 사용할 정보 설정
        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                saltBytes,
                ITERATIONS,
                KEY_LENGTH  );
        // PBKDF2 해시 알고리즘 준비
        SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        // 비밀번호 해시 생성
        byte[] hashBytes =
                factory.generateSecret(spec).getEncoded();
        // byte 배열을 저장 가능한 문자열로 반환
        return Base64.getEncoder()
                .encodeToString(hashBytes);
    }
    // 로그인할 때 입력한 비밀번호가 DB에 저장된 회원의 비밀번호와 같은지 확인
    public static boolean verifyPassword(
            String inputPassword,
            String savedSalt,
            String savedHash
    ) throws Exception {
        // throws Exception: 예외 발생하면 나를 호출한 곳으로 돌아감( main으로 감)
        // 아직 해시와 솔트가 저장되지 않은 회원이면 비밀번호를 확인할 수 없으므로 false 반환
        if (savedSalt == null || savedHash == null) {
            return false;
        }
        // 사용자가 입력한 비밀번호를 DB에 저장된 기존 솔트로 다시 해시한다.
        String inputHash =
                hashPassword(
                        inputPassword,
                        savedSalt );
        // 새로 만든 해시값과 DB에 저장된 해시값을 비교한다.
        // 같으면 true, 다르면 false가 반환된다.
        return inputHash.equals(savedHash);
    }

    public static void main() {
        Scanner scanner = new Scanner(System.in);
        try{
            System.out.println("비밀번호 입력:");
            String password = scanner.nextLine();

            // 새로운 솔트 생성
            String salt = generateSalt(); // PasswordUtil.generate
            // generateSalt() 실행-> 임의 솔트 생성 -> Base64 문자열 변환
            // -> salt 변수에 저장

            //  비밀번호와 솔트를 사용하여 해시 생성
            String passwordHash = hashPassword(password, salt);

            System.out.println("\n[변환 결과]");
            System.out.println("입력 비밀번호: " + password   );
            System.out.println("생성된 솔트: " + salt );
            System.out.println("비밀번호 해시: "+ passwordHash );
            System.out.println("\n로그인 확인용 비밀번호 입력: ");
            String checkPassword = scanner.nextLine();

            /* 입력한 비밀번호를 기존 솔트로 다시 해시하고
                앞에서 만든 해시값과 비교한다. */
            boolean passwordMatch=
                    verifyPassword(
                            checkPassword,
                            salt,
                            passwordHash
                    );
            // 비밀번호 비교 결과 출력
        if (passwordMatch) {
            System.out.println("비밀번호가 일치합니다. ");
        } else {
            System.out.println("비밀번호가 일치하지 않습니다.");
        }
        } catch (Exception e) {
            System.out.println("비밀번호 처리 중 오류가 발생했습니다.");
            System.out.println("오류 내용: "+e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

