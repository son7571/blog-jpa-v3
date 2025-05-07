package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.error.ex.Exception403;
import shop.mtcoding.blog._core.error.ex.Exception404;
import shop.mtcoding.blog.love.Love;
import shop.mtcoding.blog.love.LoveRepository;
import shop.mtcoding.blog.reply.ReplyRepository;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final LoveRepository loveRepository;
    private final ReplyRepository replyRepository;

    // TODO 과제1
    @Transactional
    public BoardResponse.DTO 글수정하기(BoardRequest.UpdateDTO reqDTO, Integer boardId, Integer sessionUserId) {
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> new Exception404("자원을 찾을 수 없습니다"));

        if (!boardPS.getUser().getId().equals(sessionUserId)) {
            throw new Exception403("권한이 없습니다");
        }

        boardPS.update(reqDTO.getTitle(), reqDTO.getContent(), reqDTO.isPublic());

        return new BoardResponse.DTO(boardPS);
    } // 더티 체킹 (상태 변경해서 update)

    // TODO 과제2
    @Transactional
    public void 글삭제(Integer id, Integer sessionUserId) {
        Board boardPS = boardRepository.findById(id)
                .orElseThrow(() -> new Exception404("자원을 찾을 수 없습니다"));
        if (!boardPS.getUser().getId().equals(sessionUserId)) {
            throw new Exception403("권한이 없습니다");
        }
        boardRepository.deleteById(id);
    }

    public BoardResponse.ListDTO 글목록보기(Integer userId, Integer page, String keyword) {
        if (userId == null) {
            Long totalCount = boardRepository.totalCount(keyword);
            List<Board> boards = boardRepository.findAll(page, keyword);
            return new BoardResponse.ListDTO(boards, page, totalCount.intValue(), keyword);
        } else {
            Long totalCount = boardRepository.totalCount(userId, keyword);
            List<Board> boards = boardRepository.findAll(userId, page, keyword);
            return new BoardResponse.ListDTO(boards, page, totalCount.intValue(), keyword);
        }
    }

    @Transactional
    public BoardResponse.DTO 글쓰기(BoardRequest.SaveDTO saveDTO, User sessionUser) {
        Board board = saveDTO.toEntity(sessionUser);
        Board boardPs = boardRepository.save(board);
        return new BoardResponse.DTO(boardPs);
    }

    @Transactional
    public BoardResponse.DetailDTO 글상세보기(Integer id, Integer userId) {
        Board boardPS = boardRepository.findByIdJoinUserAndReplies(id);


        Love love = loveRepository.findByUserIdAndBoardId(userId, id);
        Long loveCount = loveRepository.findByBoardId(id);

        Integer loveId = love == null ? null : love.getId();
        Boolean isLove = love == null ? false : true;

        BoardResponse.DetailDTO detailDTO = new BoardResponse.DetailDTO(boardPS, userId, isLove, loveCount.intValue(), loveId);
        return detailDTO;
    }

    // 규칙 4: 화면에 보이는 데이터 + 반드시 PK는 포함되어야 한다.
    public BoardResponse.DTO 글보기(int id, Integer sessionUserId) {
        Board boardPS = boardRepository.findById(id)
                .orElseThrow(() -> new Exception404("자원을 찾을 수 없습니다"));
        
        if (!boardPS.getUser().getId().equals(sessionUserId)) {
            throw new Exception403("권한이 없습니다");
        }
        return new BoardResponse.DTO(boardPS);
    }
}
