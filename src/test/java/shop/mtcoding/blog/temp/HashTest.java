package shop.mtcoding.blog.temp;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class HashTest {

    @Test
    public void encode_test() {
        //$2a$10$FijxQcyFcb1kAid2tpXS2urJ8CrcjhSoPBCMMJsXQhMRPBrVLX3mO
        //$2a$10$3oNRexRKiPBk3NI42H7M6.ri9fkwgVa4Xae/cmHiili8FoBFfFbi6
        String password = "1234";

        String encpassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(encpassword);
    }

    @Test
    public void decode_test() {
        //$2a$10$FijxQcyFcb1kAid2tpXS2urJ8CrcjhSoPBCMMJsXQhMRPBrVLX3mO
        //$2a$10$3oNRexRKiPBk3NI42H7M6.ri9fkwgVa4Xae/cmHiili8FoBFfFbi6
        String dbpassword = "$2a$10$FijxQcyFcb1kAid2tpXS2urJ8CrcjhSoPBCMMJsXQhMRPBrVLX3mO";
        String password = "1234";

        String encpassword = BCrypt.hashpw(password, BCrypt.gensalt());
        if (dbpassword.equals(encpassword)) {
            System.out.println("비밀번호가 같아요");
        } else {
            System.out.println("비밀번호가 달라요");
        }
    }

    @Test
    public void decodeV2_test() {
        //$2a$10$FijxQcyFcb1kAid2tpXS2urJ8CrcjhSoPBCMMJsXQhMRPBrVLX3mO
        //$2a$10$3oNRexRKiPBk3NI42H7M6.ri9fkwgVa4Xae/cmHiili8FoBFfFbi6
        String dbpassword = "$2a$10$FijxQcyFcb1kAid2tpXS2urJ8CrcjhSoPBCMMJsXQhMRPBrVLX3mO";
        String password = "1234";

        Boolean isSame = BCrypt.checkpw(password, dbpassword);
        System.out.println(isSame);
    }
}
