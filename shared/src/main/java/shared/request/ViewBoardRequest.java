package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("viewBoardRequest")
public class ViewBoardRequest implements Request {
    private String boardName;

    public ViewBoardRequest() {}
    public ViewBoardRequest(String boardName) { this.boardName = boardName; }

    public String getBoardName() { return boardName; }
    public void setBoardName(String boardName) { this.boardName = boardName; }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleViewBoardRequest(this);
    }
}