package client;

import client.socket.SocketRequestSender;
import shared.Model.Task;
import shared.request.*;
import shared.response.HiResponse;
import shared.response.Response;
import shared.response.ResponseHandler;
import java.io.IOException;
import java.util.UUID;

public class ClientMain implements ResponseHandler {
    public static void main(String[] args) throws IOException {
        SocketRequestSender sender = new SocketRequestSender();
        ResponseHandler handler = new ClientMain();

        sender.sendRequest(new RegisterRequest("test", "pass")).run(handler);
        sender.sendRequest(new LoginRequest("test", "pass")).run(handler);
        sender.sendRequest(new CreateBoardRequest("Final Project")).run(handler);

        System.out.println("Viewing board 'Final Project'...");
        sender.sendRequest(new ViewBoardRequest("Final Project")).run(handler);

        System.out.println("Adding a new task...");
        Response addTaskRes = sender.sendRequest(
                new AddTaskRequest("Implement Login", "Use SHA-256 for hashing.", Task.TaskPriority.High)
        );

        if (addTaskRes != null) addTaskRes.run(handler);
        System.out.println("Requesting task list for the current board...");
        Response listTasksRes = sender.sendRequest(new ListTasksRequest());
        if (listTasksRes != null) listTasksRes.run(handler);

        UUID taskIdToUpdate = UUID.fromString("95bf26a5-e9b4-4676-8146-aac74010d9f0");

        System.out.println("Updating task status to InProgress...");
        Response updateRes = sender.sendRequest(
                new UpdateTaskStatusRequest(taskIdToUpdate, Task.TaskStatus.InProgress)
        );
        if (updateRes != null) updateRes.run(handler);

        System.out.println("Requesting task list again to see the change...");
        Response listTasksAgainRes = sender.sendRequest(new ListTasksRequest());
        if (listTasksAgainRes != null) listTasksAgainRes.run(handler);
    }

    @Override
    public void handleHiResponse(HiResponse hiResponse) {
        System.out.println("Hi response handled.");
    }
}