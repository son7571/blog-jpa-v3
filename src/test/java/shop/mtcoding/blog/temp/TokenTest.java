package shop.mtcoding.blog.temp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import shop.mtcoding.blog.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class TokenTest {

    @Test
    public void create_test() {
        User user = User.builder()
                .id(1)
                .username("ssar")
                .password("$2a$10$dd85C7oDcouLxnR1jYVXVOWbGdfzYOw9Q.pHvpOz0j4we7LEs8i.u")
                .email("ssar@nate.com")
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        String jwt = JWT.create()
                .withSubject("blogv3") //크게중요하지 않고 아무거나 적어도 됨
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) //중요함 시간에대해서 따로 공부할예정
                .withClaim("id", user.getId()) //사용자를 식별할수있는 내용을 적는데 중요한내용인 PW는 적지않음
                .withClaim("username", user.getUsername())
                .sign(Algorithm.HMAC256("metacoding"));

        // 198 156 236 87 42 53 186 254 56 151 169 7 107 178 5 197 147 172 56 100 145 97 133 14 17 46 135 193 73 199 201 144
        // xpzsVyo1uv44l6kHa7IFxZOsOGSRYYUOES6HwUnHyZA
        System.out.println(jwt);
    }

    @Test
    public void verity_test() {
        // 2025.05.09.11.50분까지 유효
        String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJibG9ndjMiLCJpZCI6MSwiZXhwIjoxNzQ2NzU5ODg0LCJ1c2VybmFtZSI6InNzYXIifQ.X7_MF6bZkXkAljyU4ZBamm-ijpzRLUz62U0rc8U8390";

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256("metacoding")).build().verify(jwt);
        Integer id = decodedJWT.getClaim("id").asInt();
        String username = decodedJWT.getClaim("username").asString();

        System.out.println(id);
        System.out.println(username);

        User user = User.builder()
                .id(id)
                .username(username)
                .build();
    }

}
