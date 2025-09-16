package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import shared.response.Response;
import shared.response.ResponseHandler;

import java.io.IOException;
import java.util.Scanner;

public class ServerListener extends Thread {
    private final Scanner serverScanner;
    private final ObjectMapper objectMapper;
    private final ResponseHandler responseHandler;

    public ServerListener(Scanner serverScanner, ResponseHandler responseHandler) {
        this.serverScanner = serverScanner;
        this.responseHandler = responseHandler;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void run() {
        try {
            while (serverScanner.hasNextLine()) {
                String responseJson = serverScanner.nextLine();
                Response response = objectMapper.readValue(responseJson, Response.class);
                response.run(responseHandler);
            }
        } catch (IOException e) {
            System.out.println("\nConnection to the server has been lost.");
        } finally {
            serverScanner.close();
        }
    }
}