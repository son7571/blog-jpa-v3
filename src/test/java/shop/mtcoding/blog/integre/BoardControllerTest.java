package shop.mtcoding.blog.integre;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

//배열은 0번지만 상태 검사하면 됨
@AutoConfigureMockMvc //MockMvc 클래스가 IoC로드
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mvc;

    private String accessToken;

    @BeforeEach
    public void setup() {
        //테스트 시작전에 실행할 코드
        System.out.println("setup");
        User ssar = User.builder().id(1).username("ssar").build();
        accessToken = JwtUtil.create(ssar);
    }

    @AfterEach
    public void tearDown() {
        //테스트 후 정리할 코드
        System.out.println("tear down");
    }


    @Test
    public void save_test() throws Exception {
//given (가짜 데이터)
        BoardRequest.SaveDTO reqDTO = new BoardRequest.SaveDTO();
        reqDTO.setTitle("제목1");
        reqDTO.setContent("내용1");
        reqDTO.setIsPublic(true);

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        //when (테스트 실행)
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/board")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        //eye (결과 눈으로 검증)
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        //then (결과 코드로 검증)
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(21));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("제목1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("내용1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isPublic").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(1));

    }

    @Test
    public void list_test() throws Exception {
        // given (가짜데이터)
        String page = "1";
        String keyword = "제목1";

        // when (테스트 실행)
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/")
                        .param("keyword", keyword)
                        .param("page", page)
        );

        // eye (결과 눈으로 검증)
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then (결과 코드로 검증)
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].id").value(16));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].title").value("제목16"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].content").value("내용16"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].isPublic").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.boards[0].createdAt")
                .value(Matchers.matchesPattern("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+$")));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.prev").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.next").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.current").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.size").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.totalCount").value(11));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.totalPage").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isFirst").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isLast").value(false));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.keyword").value("제목1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.numbers", Matchers.hasSize(4)));
    }

    @Test
    public void update_test() throws Exception {
//given (가짜 데이터)
        BoardRequest.UpdateDTO reqDTO = new BoardRequest.UpdateDTO();
        reqDTO.setTitle("제목1");
        reqDTO.setContent("내용1");
        reqDTO.setIsPublic(true);

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        //when (테스트 실행)
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/board/10")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        //eye (결과 눈으로 검증)
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        //then (결과 코드로 검증)
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(10));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("제목1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("내용1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isPublic").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(1));

    }

    @Test
    public void get_board_detail_test() throws Exception {
//given (가짜 데이터)
        Integer id = 1;
//      String requestBody = "{ id : " + id + "}";
//      System.out.println(requestBody);

        //when (테스트 실행)
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/board/{id}/detail", id)
        );

        //eye (결과 눈으로 검증)
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

//        //then (결과 코드로 검증)
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(10));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("제목1"));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("내용1"));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isPublic").value(true));
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(1));

    }


}

