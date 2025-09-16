package server;

import server.socket.SocketResponseSender;
import shared.Model.Board;
import shared.Model.Task;
import shared.Model.User;
import shared.request.*;
import shared.response.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClientHandler extends Thread implements RequestHandler {
    private final SocketResponseSender socketResponseSender;
    private final DataBase dataBase;
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
                ClientManager.removeClient(currentUser.getId());
                System.out.println("User '" + currentUser.getUsername() + "' disconnected.");
            }
            socketResponseSender.close();
        }
    }


    private void broadcastToBoardMembers(Board board, String message, boolean excludeCurrentUser) {
        if (board == null) return;
        List<UUID> membersToNotify = new ArrayList<>(board.getMemberIds());

        for (UUID memberId : membersToNotify) {
            if (excludeCurrentUser && currentUser != null && memberId.equals(currentUser.getId())) {
                continue;
            }
            ClientHandler memberHandler = ClientManager.getClientHandler(memberId);
            if (memberHandler != null) {
                new Thread(() -> {
                    memberHandler.socketResponseSender.sendResponse(new NotificationResponse(message));
                }).start();
            }
        }
    }

    @Override
    public Response handleRegisterRequest(RegisterRequest request) {
        synchronized (dataBase) {
            for (User user : dataBase.getUsers()) {
                if (user.getUsername().equals(request.getUsername())) {
                    return new ActionResponse(false, "Username already taken!");
                }
            }
            String hashedPassword = SecurityManager.hashPassword(request.getPassword());
            User newUser = new User(request.getUsername(), hashedPassword);
            dataBase.getUsers().add(newUser);
            dataBase.saveAllData();
            System.out.println("New user registered: " + request.getUsername());
            return new ActionResponse(true, "Registration successful!");
        }
    }

    @Override
    public Response handleLoginRequest(LoginRequest request) {
        for (User user : dataBase.getUsers()) {
            if (user.getUsername().equals(request.getUsername())) {
                String hashedInputPassword = SecurityManager.hashPassword(request.getPassword());
                if (user.getPassword().equals(hashedInputPassword)) {
                    this.currentUser = user;
                    ClientManager.addClient(currentUser.getId(), this);
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
    public Response handleLogoutRequest(LogoutRequest request) {
        if (currentUser != null) {
            String username = currentUser.getUsername();
            ClientManager.removeClient(currentUser.getId());
            this.currentUser = null;
            this.currentBoard = null;
            System.out.println("User '" + username + "' logged out.");
            return new ActionResponse(true, "You have been successfully logged out.");
        } else {
            return new ActionResponse(false, "Error: You are not logged in.");
        }
    }

    @Override
    public Response handleCreateBoardRequest(CreateBoardRequest request) {
        if (currentUser == null) return new ActionResponse(false, "Error: You must be logged in.");
        synchronized (dataBase) {
            for (Board b : dataBase.getBoards()) {
                if (b.getOwnerId().equals(currentUser.getId()) && b.getName().equals(request.getBoardName())) {
                    return new ActionResponse(false, "Error: You already have a board with this name.");
                }
            }
            Board newBoard = new Board(request.getBoardName(), currentUser.getId());
            dataBase.getBoards().add(newBoard);
            dataBase.saveAllData();
            System.out.println("User '" + currentUser.getUsername() + "' created board '" + request.getBoardName() + "'");
            return new ActionResponse(true, "Board created successfully!");
        }
    }

    @Override
    public Response handleAddUserToBoardRequest(AddUserToBoardRequest request) {
        if (currentUser == null) return new ActionResponse(false, "Error: You must be logged in.");

        Board targetBoard;
        User userToAdd;

        synchronized (dataBase) {
            targetBoard = dataBase.getBoards().stream()
                    .filter(b -> b.getName().equals(request.getBoardName()) && b.getOwnerId().equals(currentUser.getId()))
                    .findFirst().orElse(null);

            if (targetBoard == null) return new ActionResponse(false, "Error: Board not found or you are not the owner.");

            userToAdd = dataBase.getUsers().stream()
                    .filter(u -> u.getUsername().equals(request.getUsernameToAdd()))
                    .findFirst().orElse(null);

            if (userToAdd == null) return new ActionResponse(false, "Error: User to add not found.");
            if (targetBoard.getMemberIds().contains(userToAdd.getId())) return new ActionResponse(false, "Error: User is already a member.");

            targetBoard.getMemberIds().add(userToAdd.getId());
            dataBase.saveAllData();
        }

        String msg = currentUser.getUsername() + " added " + userToAdd.getUsername() + " to board '" + targetBoard.getName() + "'.";
        broadcastToBoardMembers(targetBoard, msg, false);
        return new ActionResponse(true, "User successfully added to the board.");
    }

    @Override
    public Response handleAddTaskRequest(AddTaskRequest request) {
        if (currentUser == null || currentBoard == null) return new ActionResponse(false, "Error: You must be viewing a board.");
        Task newTask;
        synchronized (dataBase) {
            if (!currentBoard.getMemberIds().contains(currentUser.getId())) return new ActionResponse(false, "Access denied.");
            newTask = new Task(request.getTitle(), request.getDescription(), currentBoard.getId(), request.getPriority());
            dataBase.getTasks().add(newTask);
            dataBase.saveAllData();
        }
        String msg = currentUser.getUsername() + " added a new task: '" + newTask.getTitle() + "'";
        broadcastToBoardMembers(currentBoard, msg, true);
        return new ActionResponse(true, "Task '" + request.getTitle() + "' added successfully.");
    }

    @Override
    public Response handleHiRequest(HiRequest hiRequest) { return new HiResponse(); }
    @Override
    public Response handleListBoardsRequest(ListBoardsRequest request) {
        if (currentUser == null) return new ActionResponse(false, "Error: You must be logged in.");
        List<ListBoardsResponse.BoardInfo> userBoardsInfo = new ArrayList<>();
        for (Board board : dataBase.getBoards()) {
            if (board.getMemberIds().contains(currentUser.getId())) {
                userBoardsInfo.add(new ListBoardsResponse.BoardInfo(board.getId(), board.getName()));
            }
        }
        return new ListBoardsResponse(userBoardsInfo);
    }
    @Override
    public Response handleViewBoardRequest(ViewBoardRequest request) {
        if (currentUser == null) return new ActionResponse(false, "Error: You must be logged in.");
        Board targetBoard = dataBase.getBoards().stream()
                .filter(b -> b.getName().equals(request.getBoardName()) && b.getMemberIds().contains(currentUser.getId()))
                .findFirst().orElse(null);
        if (targetBoard == null) {
            this.currentBoard = null;
            return new ViewBoardResponse(false, "Board not found or you don't have access.");
        }
        this.currentBoard = targetBoard;
        System.out.println("User '" + currentUser.getUsername() + "' is now viewing board '" + currentBoard.getName() + "'.");
        return new ViewBoardResponse(true, "You are now viewing board: '" + targetBoard.getName() + "'.");
    }
    @Override
    public Response handleListTasksRequest(ListTasksRequest request) {
        if (currentUser == null || currentBoard == null) return new ActionResponse(false, "Error: You must be viewing a board.");
        List<Task> boardTasks = dataBase.getTasks().stream()
                .filter(task -> task.getBoardId().equals(currentBoard.getId()))
                .collect(Collectors.toList());
        return new ListTasksResponse(boardTasks);
    }
    @Override
    public Response handleUpdateTaskStatusRequest(UpdateTaskStatusRequest request) {
        if (currentUser == null || currentBoard == null) return new ActionResponse(false, "Error: You must be viewing a board.");
        Task targetTask;
        synchronized (dataBase) {
            targetTask = dataBase.getTasks().stream().filter(t -> t.getId().equals(request.getTaskId())).findFirst().orElse(null);
            if (targetTask == null || !targetTask.getBoardId().equals(currentBoard.getId())) return new ActionResponse(false, "Task not found or access denied.");
            targetTask.setStatus(request.getNewStatus());
            dataBase.saveAllData();
        }
        String msg = currentUser.getUsername() + " updated task '" + targetTask.getTitle() + "' to " + request.getNewStatus();
        broadcastToBoardMembers(currentBoard, msg, true);
        return new ActionResponse(true, "Task status updated successfully.");
    }
    @Override
    public Response handleDeleteTaskRequest(DeleteTaskRequest request) {
        if (currentUser == null || currentBoard == null) return new ActionResponse(false, "Error: You must be viewing a board.");
        Task targetTask;
        synchronized (dataBase) {
            targetTask = dataBase.getTasks().stream().filter(t -> t.getId().equals(request.getTaskId())).findFirst().orElse(null);
            if (targetTask == null || !targetTask.getBoardId().equals(currentBoard.getId())) return new ActionResponse(false, "Task not found or access denied.");
            dataBase.getTasks().removeIf(task -> task.getId().equals(request.getTaskId()));
            dataBase.saveAllData();
        }
        String msg = currentUser.getUsername() + " deleted task: '" + targetTask.getTitle() + "'";
        broadcastToBoardMembers(currentBoard, msg, true);
        return new ActionResponse(true, "Task deleted successfully.");
    }
}