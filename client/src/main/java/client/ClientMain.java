package client;

import client.socket.SocketRequestSender;
import shared.request.LoginRequest;
import shared.request.RegisterRequest;
import shared.response.HiResponse;
import shared.response.Response;
import shared.response.ResponseHandler;

import java.io.IOException;

public class ClientMain implements ResponseHandler {
    public static void main(String[] args) throws IOException {
        SocketRequestSender socketRequestSender = new SocketRequestSender();
        ResponseHandler responseHandler = new ClientMain(); // یک نمونه از همین کلاس که قابلیت هندل کردن پاسخ رو داره

        System.out.println("Sending Register Request...");
        Response registerResponse = socketRequestSender.sendRequest(new RegisterRequest("ali", "1234"));

        if (registerResponse != null) {
            registerResponse.run(responseHandler);
        }

        System.out.println("Sending Login Request...");
        Response loginResponse = socketRequestSender.sendRequest(new LoginRequest("ali", "1234"));
        if (loginResponse != null) {
            loginResponse.run(responseHandler);
        }
    }

    @Override
    public void handleHiResponse(HiResponse hiResponse) {
        System.out.println("yoyoyo");
    }
}