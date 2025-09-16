package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("listTasksRequest")
public class ListTasksRequest implements Request {
    public ListTasksRequest() {}

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleListTasksRequest(this);
    }
}