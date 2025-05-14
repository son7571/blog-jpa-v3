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
import shop.mtcoding.blog.user.User;
import shop.mtcoding.blog.user.UserRequest;

import static org.hamcrest.Matchers.matchesPattern;

// 컨트롤러 통합 테스트
@AutoConfigureMockMvc // MockMvc 클래스가 IoC에 로드 | RestTemplate -> 자바스크립트의 fetch와 동일, 진짜 환경에 요청 가능
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
public class UserControllerTest {

    @Autowired
    private ObjectMapper om; // json <-> java Object 변환 해주는 객체. IoC에 objectMapper가 이미 떠있음
    @Autowired
    private MockMvc mvc; // 가짜 환경에서 fetch 요청하는 클래스 | RestTemplate -> 자바스크립트의 fetch와 동일, 진짜 환경에 요청 가능

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
    public void join_test() throws Exception { // 이 메서드를 호출한 주체에게 예외 위임 -> 지금은 jvm 이다
        // given -> 가짜 데이터
        UserRequest.JoinDTO reqDTO = new UserRequest.JoinDTO();
        reqDTO.setEmail("haha@nate.com");
        reqDTO.setPassword("1234");
        reqDTO.setUsername("haha");

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody); // {"username":"haha","password":"1234","email":"haha@nate.com"}

        // when -> 테스트 실행
        ResultActions actions = mvc.perform( // 주소가 틀리면 터지고, json 아닌거 넣으면 터지고, 타입이 달라도 터지고. 따라서 미리 터진다고 알려줌
                MockMvcRequestBuilders
                        .post("/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // eye -> 결과 눈으로 검증
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody); // {"status":200,"msg":"성공","body":{"id":4,"username":"haha","email":"haha@nate.com","createdAt":"2025-05-13 11:45:23.604577"}}

        // then -> 결과를 코드로 검증 // json의 최상위 객체를 $ 표기한다
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("haha"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.email").value("haha@nate.com"));
    }

    @Test
    public void login_test() throws Exception {
        // given
        UserRequest.LoginDTO reqDTO = new UserRequest.LoginDTO();
        reqDTO.setUsername("ssar");
        reqDTO.setPassword("1234");

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then (jwt 길이만 검증) -> 길이 변환 가능. 패턴만 확인
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.accessToken",
                matchesPattern("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$")));
    }

    @Test
    public void update_test() throws Exception {
        // given
        UserRequest.UpdateDTO reqDTO = new UserRequest.UpdateDTO();
        reqDTO.setEmail("ssar@gmail.com");
        reqDTO.setPassword("1234");

        String requestBody = om.writeValueAsString(reqDTO);
//        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/user")
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
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.username").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.email").value("ssar@gmail.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+")));
    }

    @Test
    public void checkUsernameAvailable_test() throws Exception {
        // given
        String username = "ssar";

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/check-username-available/{username}", username)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.available").value(false));
    }
}