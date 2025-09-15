package client;

import client.socket.SocketRequestSender;
import shared.request.CreateBoardRequest;
import shared.request.ListBoardsRequest;
import shared.request.LoginRequest;
import shared.request.RegisterRequest;
import shared.response.HiResponse;
import shared.response.Response;
import shared.response.ResponseHandler;

import java.io.IOException;

public class ClientMain implements ResponseHandler {
    public static void main(String[] args) throws IOException {
        SocketRequestSender sender = new SocketRequestSender();
        ResponseHandler handler = new ClientMain();

        System.out.println("Attempting to register 'ali'...");
        Response res1 = sender.sendRequest(new RegisterRequest("ali", "1234"));
        if (res1 != null) res1.run(handler);

        System.out.println("Logging in...");
        Response loginRes = sender.sendRequest(new LoginRequest("ali", "1234"));
        if (loginRes != null) loginRes.run(handler);

        System.out.println("Creating a new board...");
        Response createBoardRes = sender.sendRequest(new CreateBoardRequest("My First Project"));
        if (createBoardRes != null) createBoardRes.run(handler);

        System.out.println("Creating a new board...");
        Response createBoardRes1 = sender.sendRequest(new CreateBoardRequest("My Second Project"));
        if (createBoardRes1 != null) createBoardRes1.run(handler);

        System.out.println("Requesting board list...");
        Response listBoardsRes = sender.sendRequest(new ListBoardsRequest());
        if (listBoardsRes != null) listBoardsRes.run(handler);
    }

    @Override
    public void handleHiResponse(HiResponse hiResponse) {
        System.out.println("Hi response handled.");
    }
}