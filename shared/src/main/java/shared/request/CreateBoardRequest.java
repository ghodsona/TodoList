package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("createBoardRequest")
public class CreateBoardRequest implements Request {
    private String boardName;

    public CreateBoardRequest() {}
    public CreateBoardRequest(String boardName) { this.boardName = boardName; }

    public String getBoardName() { return boardName; }
    public void setBoardName(String boardName) { this.boardName = boardName; }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleCreateBoardRequest(this);
    }
}