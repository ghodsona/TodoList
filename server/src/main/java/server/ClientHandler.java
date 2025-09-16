package server;

import server.socket.SocketResponseSender;
import shared.Model.Board;
import shared.Model.User;
import shared.request.*; // استفاده از * برای وارد کردن تمام کلاس‌های درخواست
import shared.response.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientHandler extends Thread implements RequestHandler {
    private SocketResponseSender socketResponseSender;
    private DataBase dataBase;
    private User currentUser = null;
    private Board currentBoard = null;

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

    @Override
    public Response handleAddUserToBoardRequest(AddUserToBoardRequest request) {
        if (currentUser == null) {
            return new ActionResponse(false, "Error: You must be logged in.");
        }

        String boardName = request.getBoardName();
        String usernameToAdd = request.getUsernameToAdd();

        Board targetBoard = null;
        for (Board board : dataBase.getBoards()) {
            if (board.getName().equals(boardName) && board.getOwnerId().equals(currentUser.getId())) {
                targetBoard = board;
                break;
            }
        }

        if (targetBoard == null) {
            return new ActionResponse(false, "Error: Board with name '" + boardName + "' not found for you.");
        }

        if (!targetBoard.getOwnerId().equals(currentUser.getId())) {
            return new ActionResponse(false, "Error: Only the board owner can add users.");
        }

        User userToAdd = null;
        for (User user : dataBase.getUsers()) {
            if (user.getUsername().equals(usernameToAdd)) {
                userToAdd = user;
                break;
            }
        }

        if (userToAdd == null) {
            return new ActionResponse(false, "Error: User '" + usernameToAdd + "' not found.");
        }

        if (targetBoard.getMemberIds().contains(userToAdd.getId())) {
            return new ActionResponse(false, "Error: User '" + usernameToAdd + "' is already a member of this board.");
        }

        if (currentUser.getId().equals(userToAdd.getId())) {
            return new ActionResponse(false, "Error: You are the owner and already a member.");
        }

        targetBoard.getMemberIds().add(userToAdd.getId());
        dataBase.saveAllData();
        System.out.println("User '" + currentUser.getUsername() + "' added user '" + usernameToAdd + "' to board '" + targetBoard.getName() + "'");
        return new ActionResponse(true, "User '" + usernameToAdd + "' successfully added to the board.");
    }

    // در فایل: server/src/main/java/server/ClientHandler.java

    @Override
    public Response handleViewBoardRequest(ViewBoardRequest request) {
        if (currentUser == null) {
            return new ActionResponse(false, "Error: You must be logged in.");
        }

        String boardName = request.getBoardName();
        Board targetBoard = null;

        for (Board board : dataBase.getBoards()) {
            if (board.getName().equals(boardName) && board.getMemberIds().contains(currentUser.getId())) {
                targetBoard = board;
                break;
            }
        }

        if (targetBoard == null) {
            this.currentBoard = null;
            return new ViewBoardResponse(false, "Board '" + boardName + "' not found or you don't have access.");
        }

        this.currentBoard = targetBoard;
        System.out.println("User '" + currentUser.getUsername() + "' is now viewing board '" + currentBoard.getName() + "'.");
        return new ViewBoardResponse(true, "You are now viewing board: '" + targetBoard.getName() + "'.");
    }
}