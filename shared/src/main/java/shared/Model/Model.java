package shared.Model;

import java.util.Date;
import java.util.UUID;

public abstract class Model {
    private UUID id;
    private Date createdAt;

    public Model() {
        id = UUID.randomUUID();
        createdAt = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
