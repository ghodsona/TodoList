package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;
import java.util.UUID;

@JsonTypeName("deleteTaskRequest")
public class DeleteTaskRequest implements Request {
    private UUID taskId;

    public DeleteTaskRequest() {}
    public DeleteTaskRequest(UUID taskId) {
        this.taskId = taskId;
    }

    public UUID getTaskId() { return taskId; }
    public void setTaskId(UUID taskId) { this.taskId = taskId; }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleDeleteTaskRequest(this);
    }
}