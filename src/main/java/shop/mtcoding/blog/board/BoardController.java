package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.user.User;

@CrossOrigin
@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;
    private final HttpSession session;

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable("id") Integer id, @Valid BoardRequest.UpdateDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DTO respDTO = boardService.글수정하기(reqDTO, id, sessionUser.getId());

        return "redirect:/board/" + id;
    }

    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable("id") int id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.UpdateFormDTO respDTO = boardService.업데이트글보기(id, sessionUser.getId());
        request.setAttribute("model", respDTO);
        return "board/update-form";
    }

    @GetMapping("/v2/board/{id}")
    public @ResponseBody BoardResponse.DetailDTO v2Detail(@PathVariable("id") Integer id) {

        Integer sessionUserId = 1;
        BoardResponse.DetailDTO detailDTO = boardService.글상세보기(id, sessionUserId);

        return detailDTO;
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable("id") Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Integer sessionUserId = (sessionUser == null ? null : sessionUser.getId());
        BoardResponse.DetailDTO detailDTO = boardService.글상세보기(id, sessionUserId);
        request.setAttribute("model", detailDTO);
        return "board/detail";
    }

    // localhost:8080?page=0
    // localhost:8080
    @GetMapping("/")
    public String list(HttpServletRequest request,
                       @RequestParam(required = false, value = "page", defaultValue = "0") Integer page,
                       @RequestParam(required = false, value = "keyword", defaultValue = "") String keyword) {
        System.out.println("keyword : " + keyword);
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            request.setAttribute("model", boardService.글목록보기(null, page, keyword));
        } else {
            request.setAttribute("model", boardService.글목록보기(sessionUser.getId(), page, keyword));
        }

        return "board/list";
    }

    @PostMapping("/board/save")
    public String save(@Valid BoardRequest.SaveDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DTO respDTO = boardService.글쓰기(reqDTO, sessionUser);
        return "redirect:/";
    }

    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }
}
