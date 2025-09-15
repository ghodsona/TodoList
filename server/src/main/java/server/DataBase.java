package server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import shared.Model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBase {
    private List<User> users = Collections.synchronizedList(new ArrayList<>());
    private final ObjectMapper objectMapper;
    private static final File DATABASE_FILE = new File("database.json"); // نام فایل دیتابیس ما

    public DataBase() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        loadFromFile();
    }

    public List<User> getUsers() {
        return users;
    }

    private void loadFromFile() {
        if (DATABASE_FILE.exists()) {
            try {
                this.users = Collections.synchronizedList(
                        objectMapper.readValue(DATABASE_FILE, new TypeReference<ArrayList<User>>() {})
                );
                System.out.println("Database loaded successfully from " + DATABASE_FILE.getName());
            } catch (IOException e) {
                System.err.println("Could not load database from file: " + e.getMessage());
            }
        } else {
            System.out.println("No existing database file found. Starting fresh.");
        }
    }

    public synchronized void saveToFile() {
        try {
            objectMapper.writeValue(DATABASE_FILE, this.users);
        } catch (IOException e) {
            System.err.println("Could not save database to file: " + e.getMessage());
        }
    }
}