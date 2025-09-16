package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("logoutRequest")
public class LogoutRequest implements Request {
    public LogoutRequest() {}

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleLogoutRequest(this);
    }
}