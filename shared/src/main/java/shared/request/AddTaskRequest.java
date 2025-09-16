package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.Model.Task;
import shared.response.Response;

@JsonTypeName("addTaskRequest")
public class AddTaskRequest implements Request {
    private String title;
    private String description;
    private Task.TaskPriority priority;

    public AddTaskRequest() {}
    public AddTaskRequest(String title, String description, Task.TaskPriority priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Task.TaskPriority getPriority() { return priority; }
    public void setPriority(Task.TaskPriority priority) { this.priority = priority; }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleAddTaskRequest(this);
    }
}