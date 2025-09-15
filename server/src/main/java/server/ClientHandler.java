package server;

import server.socket.SocketResponseSender;
import shared.Model.User;
import shared.request.HiRequest;
import shared.request.LoginRequest;
import shared.request.RegisterRequest;
import shared.request.RequestHandler;
import shared.response.ActionResponse;
import shared.response.HiResponse;
import shared.response.Response;

public class ClientHandler extends Thread implements RequestHandler {
    private SocketResponseSender socketResponseSender;
    private DataBase dataBase;

    public ClientHandler(SocketResponseSender socketResponseSender, DataBase dataBase) {
        this.dataBase = dataBase;
        this.socketResponseSender = socketResponseSender;
    }


    @Override
    public void run() {
        try {
            while (true) {
                Response response = socketResponseSender.getRequest().run(this);
                socketResponseSender.sendResponse(response);
            }
        } catch (Exception e) {
            socketResponseSender.close();
        }
    }

    @Override
    public Response handleHiRequest(HiRequest hiRequest) {
        System.out.println("naisbdaVGFYDVS");
        return new HiResponse();
    }

    @Override
    public Response handleLoginRequest(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String plainPassword = loginRequest.getPassword();

        for (User user : dataBase.getUsers()) {
            if (user.getUsername().equals(username)) {
                String hashedInputPassword = SecurityManager.hashPassword(plainPassword);

                if (user.getPassword().equals(hashedInputPassword)) {
                    return new ActionResponse(true, "Login successful! Welcome back.");
                } else {
                    return new ActionResponse(false, "Incorrect password.");
                }
            }
        }

        return new ActionResponse(false, "User not found.");
    }

    @Override
    public Response handleRegisterRequest(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();

        for (User user : dataBase.getUsers()) {
            if (user.getUsername().equals(username)) {
                return new ActionResponse(false, "Username already taken!");
            }
        }

        User newUser = new User(username, SecurityManager.hashPassword(password));
        dataBase.getUsers().add(newUser);
        dataBase.saveToFile();

        System.out.println("New user registered: " + username);

        return new ActionResponse(true, "Registration successful!");
    }
}
