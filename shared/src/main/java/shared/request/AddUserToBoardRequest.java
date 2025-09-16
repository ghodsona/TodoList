package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;
import java.util.UUID;

@JsonTypeName("addUserToBoardRequest")
public class AddUserToBoardRequest implements Request {
    private String boardName;
    private String usernameToAdd;

    public AddUserToBoardRequest() {}
    public AddUserToBoardRequest(String boardName, String usernameToAdd) {
        this.boardName = boardName;
        this.usernameToAdd = usernameToAdd;
    }

    public String getBoardName() { return boardName; }
    public void setBoardName(String boardName) { this.boardName = boardName; }
    public String getUsernameToAdd() { return usernameToAdd; }
    public void setUsernameToAdd(String usernameToAdd) { this.usernameToAdd = usernameToAdd; }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleAddUserToBoardRequest(this);
    }
}