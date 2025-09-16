package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("notificationResponse")
public class NotificationResponse implements Response {
    private String message;

    public NotificationResponse() {}
    public NotificationResponse(String message) { this.message = message; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public void run(ResponseHandler responseHandler) {
        System.out.println("\n!!! [Notification] " + message + "\n> ");
    }
}