package server;

import shared.Model.User;

import java.util.UUID;

public class Authentication {
    DataBase dataBase;

    public Authentication(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public User getUserById(UUID uuid) {
        for (User user : dataBase.getUsers()) {
            if (user.getId().equals(uuid)) {
                return user;
            }
        }
        return null;
    }
}
