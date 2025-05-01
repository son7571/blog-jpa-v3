package shop.mtcoding.blog.reply;

import lombok.Data;

public class ReplyResponse {

    @Data
    public static class DTO {
        private Integer id;
        private String content;
        private Integer userId;
        private Integer boardId;
        private String createdAt; // 날짜는 String으로 리턴한다 (날짜 타입 공부하기 전까지)

        public DTO(Reply reply) {
            this.id = reply.getId();
            this.content = reply.getContent();
            this.userId = reply.getUser().getId();
            this.boardId = reply.getBoard().getId();
            this.createdAt = reply.getCreatedAt().toString();
        }
    }
}
