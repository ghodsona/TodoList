package client;

import client.socket.SocketRequestSender;
import shared.Model.Task;
import shared.request.*;
import shared.response.HiResponse;
import shared.response.ResponseHandler;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

public class ClientMain implements ResponseHandler {

    private static SocketRequestSender sender;

    public static void main(String[] args) {
        try {
            sender = new SocketRequestSender();
            ResponseHandler handler = new ClientMain();

            // ۱. کارمند شنونده را استخدام و روشن میکنیم!
            // این نخ در پس زمینه فقط به پیام های سرور گوش میدهد
            ServerListener listener = new ServerListener(sender.getServerScanner(), handler);
            listener.start();

            // ۲. نخ اصلی فقط منتظر دستور کاربر میماند
            Scanner userInputScanner = new Scanner(System.in);
            System.out.println("Welcome to the Todo List Application!");
            System.out.println("Type 'help' to see a list of commands.");

            while (true) {
                System.out.print("> ");
                String input = userInputScanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye!");
                    sender.close();
                    break;
                }

                if (input.isEmpty()) {
                    continue;
                }

                handleCommand(input);
            }

        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
        } catch (Exception e) {
        }
    }

    private static void handleCommand(String input) {
        String[] parts = input.split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";
        Request request = null;

        switch (command) {
            case "register":
            case "login":
                String[] credentials = args.split("\\s+");
                if (credentials.length != 2) {
                    System.out.println("Usage: " + command + " <username> <password>");
                    return;
                }
                if (command.equals("register")) {
                    request = new RegisterRequest(credentials[0], credentials[1]);
                } else {
                    request = new LoginRequest(credentials[0], credentials[1]);
                }
                break;

            case "create_board":
                if (args.isEmpty()) {
                    System.out.println("Usage: create_board <board_name>");
                    return;
                }
                request = new CreateBoardRequest(args);
                break;

            case "list_boards":
                request = new ListBoardsRequest();
                break;

            case "add_user_to_board":
                String[] addUserArgs = args.split("\\s+");
                if (addUserArgs.length != 2) {
                    System.out.println("Usage: add_user_to_board <board_name> <username_to_add>");
                    return;
                }
                request = new AddUserToBoardRequest(addUserArgs[0], addUserArgs[1]);
                break;

            case "view_board":
                if (args.isEmpty()) {
                    System.out.println("Usage: view_board <board_name>");
                    return;
                }
                request = new ViewBoardRequest(args);
                break;

            case "add_task":
                String[] taskArgs = args.split("\\|");
                if (taskArgs.length != 3) {
                    System.out.println("Usage: add_task <title> | <description> | <priority (Low/Medium/High)>");
                    return;
                }
                try {
                    Task.TaskPriority priority = Task.TaskPriority.valueOf(taskArgs[2].trim());
                    request = new AddTaskRequest(taskArgs[0].trim(), taskArgs[1].trim(), priority);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid priority. Use one of: Low, Medium, High");
                    return;
                }
                break;

            case "list_tasks":
                request = new ListTasksRequest();
                break;

            case "update_task_status":
                String[] updateArgs = args.split("\\s+");
                if (updateArgs.length != 2) {
                    System.out.println("Usage: update_task_status <task_id> <new_status (ToDo/InProgress/Done)>");
                    return;
                }
                try {
                    UUID taskId = UUID.fromString(updateArgs[0]);
                    Task.TaskStatus status = Task.TaskStatus.valueOf(updateArgs[1]);
                    request = new UpdateTaskStatusRequest(taskId, status);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid Task ID or Status format.");
                    return;
                }
                break;

            case "delete_task":
                if (args.isEmpty()) {
                    System.out.println("Usage: delete_task <task_id>");
                    return;
                }
                try {
                    UUID taskId = UUID.fromString(args);
                    request = new DeleteTaskRequest(taskId);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid Task ID format.");
                    return;
                }
                break;

            case "help":
                printHelp();
                return;

            case "logout":
                request = new LogoutRequest();
                break;

            default:
                System.out.println("Unknown command: '" + command + "'. Type 'help' for a list of commands.");
                return;
        }

        if (request != null) {
            sender.sendRequest(request);
        }
    }

    private static void printHelp() {
        System.out.println("\n--- Available Commands ---");
        System.out.println("  register <username> <password>        - Register a new user");
        System.out.println("  login <username> <password>           - Login to your account");
        System.out.println("  logout                              - Logout from your account");
        System.out.println("  create_board <board_name>             - Create a new board");
        System.out.println("  list_boards                         - List boards you have access to");
        System.out.println("  add_user_to_board <b_name> <u_name>   - Add a user to a board you own");
        System.out.println("  view_board <board_name>               - Enter a board to view/edit tasks");
        System.out.println("\n  --- Commands inside a board ---");
        System.out.println("  add_task <title> | <desc> | <priority>  - Add a new task (Low/Medium/High)");
        System.out.println("  list_tasks                          - List all tasks in the current board");
        System.out.println("  update_task_status <task_id> <status>   - Update a task's status (ToDo/InProgress/Done)");
        System.out.println("  delete_task <task_id>                 - Delete a task from the board");
        System.out.println("\n  --- General ---");
        System.out.println("  help                                - Show this help message");
        System.out.println("  exit                                - Close the application");
        System.out.println("--------------------------\n");
    }

    @Override
    public void handleHiResponse(HiResponse hiResponse) {
        System.out.println("Received HiResponse: " + hiResponse);
    }
}