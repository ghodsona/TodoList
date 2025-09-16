package server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class ClientManager {
    private static final Map<UUID, ClientHandler> activeClients = new ConcurrentHashMap<>();

    public static void addClient(UUID userId, ClientHandler handler) {
        activeClients.put(userId, handler);
        System.out.println("ClientManager: User " + userId + " is now online. Total online: " + activeClients.size());
    }

    public static void removeClient(UUID userId) {
        if (userId != null) {
            activeClients.remove(userId);
            System.out.println("ClientManager: User " + userId + " went offline. Total online: " + activeClients.size());
        }
    }

    public static ClientHandler getClientHandler(UUID userId) {
        return activeClients.get(userId);
    }
}