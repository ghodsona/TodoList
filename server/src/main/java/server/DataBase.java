package server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import shared.Model.Board;
import shared.Model.Task;
import shared.Model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBase {
    private List<User> users = Collections.synchronizedList(new ArrayList<>());
    private List<Board> boards = Collections.synchronizedList(new ArrayList<>());
    private List<Task> tasks = Collections.synchronizedList(new ArrayList<>());

    private final ObjectMapper objectMapper;
    private static final File DATA_DIR = new File("data");
    private static final File USERS_FILE = new File(DATA_DIR, "users.json");
    private static final File BOARDS_FILE = new File(DATA_DIR, "boards.json");
    private static final File TASKS_FILE = new File(DATA_DIR, "tasks.json");

    public DataBase() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        if (!DATA_DIR.exists()) {
            DATA_DIR.mkdirs();
        }
        loadAllData();
    }

    public List<User> getUsers() { return users; }
    public List<Board> getBoards() { return boards; }
    public List<Task> getTasks() { return tasks; }

    private void loadAllData() {
        loadFromFile(USERS_FILE, new TypeReference<ArrayList<User>>() {}, users);
        loadFromFile(BOARDS_FILE, new TypeReference<ArrayList<Board>>() {}, boards);
        loadFromFile(TASKS_FILE, new TypeReference<ArrayList<Task>>() {}, tasks);
        System.out.println("All data loaded.");
    }

    public synchronized void saveAllData() {
        saveToFile(USERS_FILE, users);
        saveToFile(BOARDS_FILE, boards);
        saveToFile(TASKS_FILE, tasks);
        System.out.println("All data saved.");
    }

    private <T> void loadFromFile(File file, TypeReference<T> type, List list) {
        if (file.exists() && file.length() > 0) {
            try {
                T loadedData = objectMapper.readValue(file, type);
                list.clear();
                list.addAll((List) loadedData);
            } catch (IOException e) {
                System.err.println("Could not load from file " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    private void saveToFile(File file, Object data) {
        try {
            objectMapper.writeValue(file, data);
        } catch (IOException e) {
            System.err.println("Could not save to file " + file.getName() + ": " + e.getMessage());
        }
    }
}