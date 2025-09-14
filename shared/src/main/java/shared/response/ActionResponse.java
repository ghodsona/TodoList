package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("actionResponse")
public class ActionResponse implements Response {
    private boolean success;
    private String message;

    public ActionResponse() {}

    public ActionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public void run(ResponseHandler responseHandler) {
         System.out.println("Server Response: " + message);
    }
}