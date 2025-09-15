package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("listBoardsRequest")
public class ListBoardsRequest implements Request {
    public ListBoardsRequest() {}

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleListBoardsRequest(this);
    }
}