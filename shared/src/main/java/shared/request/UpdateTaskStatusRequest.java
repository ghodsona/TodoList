package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.Model.Task; import shared.response.Response;
import java.util.UUID;

@JsonTypeName("updateTaskStatusRequest")
public class UpdateTaskStatusRequest implements Request {
    private UUID taskId;
    private Task.TaskStatus newStatus;

    public UpdateTaskStatusRequest() {}
    public UpdateTaskStatusRequest(UUID taskId, Task.TaskStatus newStatus) {
        this.taskId = taskId;
        this.newStatus = newStatus;
    }

    public UUID getTaskId() { return taskId; }
    public void setTaskId(UUID taskId) { this.taskId = taskId; }
    public Task.TaskStatus getNewStatus() { return newStatus; }
    public void setNewStatus(Task.TaskStatus newStatus) { this.newStatus = newStatus; }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleUpdateTaskStatusRequest(this);
    }
}