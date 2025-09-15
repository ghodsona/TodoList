package shared.Model;

import java.util.Date;
import java.util.UUID;


public class Task extends Model {
    public enum TaskStatus {
        ToDo,
        InProgress,
        Done
    }

    public enum TaskPriority {
        Low,
        Medium,
        High
    }

    private String title;
    private String description;
    private UUID boardId;
    private UUID assignedUserId;
    private TaskStatus status;
    private TaskPriority priority;
    private Date dueDate;

    public Task() {
    }

    public Task(String title, String description, UUID boardId, TaskPriority priority) {
        this.title = title;
        this.description = description;
        this.boardId = boardId;
        this.priority = priority;
        this.status = TaskStatus.ToDo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getBoardId() {
        return boardId;
    }

    public void setBoardId(UUID boardId) {
        this.boardId = boardId;
    }

    public UUID getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(UUID assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}