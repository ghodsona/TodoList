package client;

import client.socket.SocketRequestSender;
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

        sender.sendRequest(new RegisterRequest("ownerUser", "pass1")).run(handler);
        sender.sendRequest(new RegisterRequest("memberUser", "pass2")).run(handler);

        System.out.println("Logging in as ownerUser...");
        sender.sendRequest(new LoginRequest("ownerUser", "pass1")).run(handler);

        String boardNameToAddUserTo = "Team Project"; // نام بورد رو در یک متغیر ذخیره میکنیم
        System.out.println("Creating board '" + boardNameToAddUserTo + "'...");
        sender.sendRequest(new CreateBoardRequest(boardNameToAddUserTo)).run(handler);

        System.out.println("Adding 'memberUser' to the board '" + boardNameToAddUserTo + "'...");
        Response addUserRes = sender.sendRequest(new AddUserToBoardRequest(boardNameToAddUserTo, "memberUser"));
        if (addUserRes != null) addUserRes.run(handler);
    }
    @Override
    public void handleHiResponse(HiResponse hiResponse) {
        System.out.println("Hi response handled.");
    }
}