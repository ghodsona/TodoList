package client;

import client.socket.SocketRequestSender;
import shared.request.CreateBoardRequest;
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

        // ۱. همیشه اول برای ثبت نام تلاش کن
        System.out.println("Attempting to register 'ali'...");
        Response res1 = sender.sendRequest(new RegisterRequest("ali", "1234"));
        if (res1 != null) res1.run(handler);
        // اگه کاربر از قبل وجود داشته باشه، این مرحله خطا میده که اشکالی نداره.

        // ۲. حالا با همون کاربر لاگین کن
        System.out.println("Logging in...");
        Response loginRes = sender.sendRequest(new LoginRequest("ali", "1234"));
        if (loginRes != null) loginRes.run(handler);

        // ۳. اگه لاگین موفق بود، یک بورد جدید بساز
        System.out.println("Creating a new board...");
        Response createBoardRes = sender.sendRequest(new CreateBoardRequest("My First Project"));
        if (createBoardRes != null) createBoardRes.run(handler);
    }

    @Override
    public void handleHiResponse(HiResponse hiResponse) {
        System.out.println("Hi response handled.");
    }
}