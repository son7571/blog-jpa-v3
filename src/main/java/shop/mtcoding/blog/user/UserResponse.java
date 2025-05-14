package shop.mtcoding.blog.user;

import lombok.Builder;
import lombok.Data;

public class UserResponse {

    @Data
    public static class TokenDTO {
        private String accessToken;
        private String refreshToken;

        @Builder
        public TokenDTO(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    // RestAPI 규칙 : 2 DTO에 민감정보 빼기, 날짜는 String으로!! (날짜 공부하기전까지)
    @Data
    public static class DTO {
        private Integer id;
        private String username;
        //      private String password;
        private String email;
        private String createdAt;

        public DTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.createdAt = user.getCreatedAt().toString();
        }
    }

}
