package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import java.util.UUID;

@JsonTypeName("listBoardsResponse")
public class ListBoardsResponse implements Response {

    public static class BoardInfo {
        public UUID boardId;
        public String boardName;

        public BoardInfo() {} // برای Jackson
        public BoardInfo(UUID boardId, String boardName) {
            this.boardId = boardId;
            this.boardName = boardName;
        }
    }

    private List<BoardInfo> boards;

    public ListBoardsResponse() {}
    public ListBoardsResponse(List<BoardInfo> boards) { this.boards = boards; }

    public List<BoardInfo> getBoards() { return boards; }
    public void setBoards(List<BoardInfo> boards) { this.boards = boards; }

    @Override
    public void run(ResponseHandler responseHandler) {
        System.out.println("--- Your Boards ---");
        if (boards == null || boards.isEmpty()) {
            System.out.println("You have no boards.");
        } else {
            for (BoardInfo board : boards) {
                System.out.println("ID: " + board.boardId + " | Name: " + board.boardName);
            }
        }
        System.out.println("-------------------");
    }
}