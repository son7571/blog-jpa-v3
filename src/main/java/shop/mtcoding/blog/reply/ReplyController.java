package shop.mtcoding.blog.reply;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.util.Resp;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@RestController
public class ReplyController {
    private final ReplyService replyService;
    private final HttpSession session;

    @DeleteMapping("/s/api/reply/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        replyService.댓글삭제(id, sessionUser.getId());
        return Resp.ok(null);
    }

    @PostMapping("/s/api/reply")
    public ResponseEntity<?> save(@Valid @RequestBody ReplyRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ReplyResponse.DTO respDTO = replyService.댓글쓰기(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }
}