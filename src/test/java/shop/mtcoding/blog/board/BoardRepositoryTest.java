package shop.mtcoding.blog.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.mtcoding.blog.reply.Reply;

import java.util.List;

@Import(BoardRepository.class) // BoardRepository
@DataJpaTest // EntityManager, PC
public class BoardRepositoryTest {

    @Autowired // DI
    private BoardRepository boardRepository;

    @Test
    public void findByIdJoinUserAndReplies_test() {
        // given
        Integer boardId = 1;
        Board board = boardRepository.findByIdJoinUserAndReplies(boardId);


        for (Reply reply : board.getReplies()) {
            System.out.println("-----------------------------------Lazy Loading Select");
            System.out.println(reply.getUser().getUsername());
            System.out.println("------------------------");
        }

    }

    @Test
    public void findById_test() {
        // given
        Integer boardId = 4;
        Board board = boardRepository.findById(boardId);
    }

    @Test
    public void findByIdJoinUser() {
        // given
        Integer id = 1;

        // when
        boardRepository.findByIdJoinUser(id);
    }

    @Test
    public void findAll_test() {
        // given
        Integer userId = 1;

        // when
        List<Board> boardList = boardRepository.findAll(userId, null);

        // eye
        for (Board board : boardList) {
            System.out.print(board.getId() + ", " + board.getTitle());
            System.out.println();
        }

        // Lazy -> Board -> User(id=1)
        // Eager -> N+1  -> Board조회 -> 연관된 User 유저 수 만큼 주회
        // Eager -> Join -> 한방쿼리
//        System.out.println("--------------------");
//        boardList.get(0).getUser().getUsername();
//        System.out.println("--------------------");

    }
}
