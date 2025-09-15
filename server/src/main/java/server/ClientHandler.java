package server;

import server.socket.SocketResponseSender;
import shared.Model.Board;
import shared.Model.User;
import shared.request.*; // استفاده از * برای وارد کردن تمام کلاس‌های درخواست
import shared.response.ActionResponse;
import shared.response.HiResponse;
import shared.response.ListBoardsResponse;
import shared.response.Response;

import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread implements RequestHandler {
    private SocketResponseSender socketResponseSender;
    private DataBase dataBase;
    private User currentUser = null;

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
            if (currentUser != null) {
                System.out.println("User '" + currentUser.getUsername() + "' disconnected.");
            } else {
                System.out.println("A client disconnected.");
            }
            socketResponseSender.close();
        }
    }

    @Override
    public Response handleHiRequest(HiRequest hiRequest) {
        return new HiResponse();
    }

    @Override
    public Response handleRegisterRequest(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        for (User user : dataBase.getUsers()) {
            if (user.getUsername().equals(username)) {
                return new ActionResponse(false, "Username already taken!");
            }
        }
        String hashedPassword = SecurityManager.hashPassword(registerRequest.getPassword());
        User newUser = new User(username, hashedPassword);
        dataBase.getUsers().add(newUser);
        dataBase.saveAllData();
        System.out.println("New user registered and saved: " + username);
        return new ActionResponse(true, "Registration successful!");
    }

    @Override
    public Response handleLoginRequest(LoginRequest loginRequest) {
        for (User user : dataBase.getUsers()) {
            if (user.getUsername().equals(loginRequest.getUsername())) {
                String hashedInputPassword = SecurityManager.hashPassword(loginRequest.getPassword());
                if (user.getPassword().equals(hashedInputPassword)) {
                    this.currentUser = user;
                    System.out.println("User '" + currentUser.getUsername() + "' logged in.");
                    return new ActionResponse(true, "Login successful! Welcome back.");
                } else {
                    return new ActionResponse(false, "Incorrect password.");
                }
            }
        }
        return new ActionResponse(false, "User not found.");
    }

    @Override
    public Response handleCreateBoardRequest(CreateBoardRequest createBoardRequest) {
        if (currentUser == null) {
            return new ActionResponse(false, "Error: You must be logged in to create a board.");
        }
        String boardName = createBoardRequest.getBoardName();
        if (boardName == null || boardName.trim().isEmpty()) {
            return new ActionResponse(false, "Error: Board name cannot be empty.");
        }
        for (Board existingBoard : dataBase.getBoards()) {
            if (existingBoard.getOwnerId().equals(currentUser.getId()) && existingBoard.getName().equals(boardName)) {
                return new ActionResponse(false, "Error: You already have a board with the name '" + boardName + "'.");
            }
        }
        Board newBoard = new Board(boardName, currentUser.getId());
        dataBase.getBoards().add(newBoard);
        dataBase.saveAllData();

        System.out.println("User '" + currentUser.getUsername() + "' created a new board: '" + boardName + "'");
        return new ActionResponse(true, "Board '" + boardName + "' created successfully!");
    }


    @Override
    public Response handleListBoardsRequest(ListBoardsRequest request) {
        if (currentUser == null) {
            return new ActionResponse(false, "Error: You must be logged in to see your boards.");
        }

        List<ListBoardsResponse.BoardInfo> userBoardsInfo = new ArrayList<>();

        for (Board board : dataBase.getBoards()) {
            if (board.getMemberIds().contains(currentUser.getId())) {
                userBoardsInfo.add(new ListBoardsResponse.BoardInfo(board.getId(), board.getName()));
            }
        }

        return new ListBoardsResponse(userBoardsInfo);
    }
}