package shop.mtcoding.blog.integre;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.mtcoding.blog._core.util.JwtUtil;
import shop.mtcoding.blog.board.BoardRequest;
import shop.mtcoding.blog.user.User;

import static org.hamcrest.Matchers.*;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mvc;

    private String accessToken;

    @BeforeEach
    public void setUp() {
        // 테스트 시작 전에 실행할 코드
        System.out.println("setUp");
        User ssar = User.builder()
                .id(1)
                .username("ssar")
                .build();
        accessToken = JwtUtil.create(ssar);
    }

    @AfterEach
    public void tearDown() { // 끝나고 나서 마무리 함수
        // 테스트 후 정리할 코드
        System.out.println("tearDown");
    }

    @Test
    public void list_test() throws Exception {
        // given
        Integer page = 1;
        String keyword = "제목1";

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/")
                        .param("page", page.toString())
                        .param("keyword", keyword)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].id").value(16));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].title").value("제목16"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].content").value("내용16"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].isPublic").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.prev").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.next").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.current").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.size").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.totalCount").value(11));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.totalPage").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isFirst").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isLast").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.numbers", hasSize(4)));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.keyword").value("제목1"));
    }

    @Test
    public void getBoardDetail_test() throws Exception {
        // given
        Integer id = 4;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/board/{id}/detail", id)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("제목4"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("내용4"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isPublic").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isOwner").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isLove").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.loveCount").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("love"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.loveId").value(nullValue()));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replies[0].id").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replies[0].content").value("댓글3"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replies[0].username").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.replies[0].isOwner").value(false));
    }

    @Test
    public void save_test() throws Exception {
        // given
        BoardRequest.SaveDTO reqDTO = new BoardRequest.SaveDTO();
        reqDTO.setTitle("제목21");
        reqDTO.setContent("내용21");
        reqDTO.setIsPublic(true);

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/board")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(21));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("제목21"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("내용21"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isPublic").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+")));
    }

    @Test
    public void getBoardOne_test() throws Exception {
        // given
        Integer id = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/board/{id}", id)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("제목1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("내용1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isPublic").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+")));
    }

    @Test
    public void update_test() throws Exception {
        // given
        Integer id = 1;
        BoardRequest.UpdateDTO reqDTO = new BoardRequest.UpdateDTO();
        reqDTO.setTitle("제1");
        reqDTO.setContent("내1");
        reqDTO.setIsPublic(true);

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/board/{id}", id)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("제1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("내1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isPublic").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+")));
    }
}