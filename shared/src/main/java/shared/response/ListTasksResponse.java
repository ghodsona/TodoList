package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.Model.Task;
import java.util.List;

@JsonTypeName("listTasksResponse")
public class ListTasksResponse implements Response {
    private List<Task> tasks;

    public ListTasksResponse() {}
    public ListTasksResponse(List<Task> tasks) { this.tasks = tasks; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    @Override
    public void run(ResponseHandler responseHandler) {
        System.out.println("\n--- Tasks in this Board ---");
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("No tasks found in this board.");
        } else {
            for (Task task : tasks) {
                System.out.println("-------------------------");
                System.out.println("  Title: " + task.getTitle());
                System.out.println("  Description: " + task.getDescription());
                System.out.println("  Status: " + task.getStatus());
                System.out.println("  Priority: " + task.getPriority());
                System.out.println("  Created At: " + task.getCreatedAt());
                System.out.println("  ID: " + task.getId());
            }
        }
        System.out.println("-------------------------\n");
    }
}