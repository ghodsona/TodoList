package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("hiRequest")

public class HiRequest implements Request {
    private String message;

    public HiRequest() {
        message = "Hi";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleHiRequest(this);
    }
}
