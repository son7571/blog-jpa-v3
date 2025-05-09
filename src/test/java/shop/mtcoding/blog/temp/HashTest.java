package shop.mtcoding.blog.temp;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class HashTest {

    @Test
    public void encode_test() {
        // $2a$10$dd85C7oDcouLxnR1jYVXVOWbGdfzYOw9Q.pHvpOz0j4we7LEs8i.u
        // $2a$10$XZeNexhXBAX/RzZfbUyzSuzZDLedgJo8JCsW3HXkOxz5S5jNxUh0a
        String password = "1234";

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(encPassword);
    }

    @Test
    public void decode_test() {
        // $2a$10$dd85C7oDcouLxnR1jYVXVOWbGdfzYOw9Q.pHvpOz0j4we7LEs8i.u
        // $2a$10$XZeNexhXBAX/RzZfbUyzSuzZDLedgJo8JCsW3HXkOxz5S5jNxUh0a
        String dbPassword = "$2a$10$dd85C7oDcouLxnR1jYVXVOWbGdfzYOw9Q.pHvpOz0j4we7LEs8i.u";
        String password = "1234";

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        if (dbPassword.equals(encPassword)) {
            System.out.println("비밀번호가 같아요");
        } else {
            System.out.println("비밀번호가 달라요");
        }
    }

    @Test
    public void decodeV2_test() {
        // $2a$10$dd85C7oDcouLxnR1jYVXVOWbGdfzYOw9Q.pHvpOz0j4we7LEs8i.u
        // $2a$10$XZeNexhXBAX/RzZfbUyzSuzZDLedgJo8JCsW3HXkOxz5S5jNxUh0a
        String dbPassword = "$2a$10$dd85C7oDcouLxnR1jYVXVOWbGdfzYOw9Q.pHvpOz0j4we7LEs8i.u";
        String password = "1234";

        Boolean isSame = BCrypt.checkpw(password, dbPassword);
        System.out.println(isSame);
    }
}
