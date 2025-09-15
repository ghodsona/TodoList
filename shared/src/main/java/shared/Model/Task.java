package shared.Model;

import java.util.Date;
import java.util.UUID;

public class Task extends Model {
    private String title;
    private String description;
    private UUID boardId;

    public Task() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public UUID getBoardId() { return boardId; }
    public void setBoardId(UUID boardId) { this.boardId = boardId; }
}