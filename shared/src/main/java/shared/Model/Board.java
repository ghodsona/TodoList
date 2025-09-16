package shared.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Board extends Model {
    private String name;
    private UUID ownerId;
    private List<UUID> memberIds = new CopyOnWriteArrayList<>();
    public Board() {}

    public Board(String name, UUID ownerId) {
        this.name = name;
        this.ownerId = ownerId;
        this.memberIds.add(ownerId);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UUID getOwnerId() { return ownerId; }
    public void setOwnerId(UUID ownerId) { this.ownerId = ownerId; }

    public List<UUID> getMemberIds() { return memberIds; }
    public void setMemberIds(List<UUID> memberIds) { this.memberIds = memberIds; }
}