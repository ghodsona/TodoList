package client;

import client.socket.SocketRequestSender;
import shared.Model.Task;
import shared.request.*;
import shared.response.HiResponse;
import shared.response.Response;
import shared.response.ResponseHandler;
import java.io.IOException;

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
    }

    @Override
    public void handleHiResponse(HiResponse hiResponse) {
        System.out.println("Hi response handled.");
    }
}