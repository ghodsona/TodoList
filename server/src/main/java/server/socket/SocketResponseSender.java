package server.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import shared.request.Request;
import shared.response.Response;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketResponseSender {
    public final Scanner scanner;
    private final Socket socket;
    private final PrintStream printStream;
    private final ObjectMapper objectMapper;

    public SocketResponseSender(Socket socket) throws IOException {
        this.socket = socket;
        printStream = new PrintStream(socket.getOutputStream());
        scanner = new Scanner(socket.getInputStream());
        objectMapper = new ObjectMapper();
    }

    public Request getRequest() {
        try {
            if (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                System.out.println("Read line: " + s);
                Request request = objectMapper.readValue(s, Request.class);
                System.out.println("Deserialized Request: " + request);
                return request;
            } else {
                System.out.println("No line found");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void sendResponse(Response response) {
        try {
            printStream.println(objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            System.out.println("asd");
        }
    }

    public void close() {
        try {
            scanner.close();
            printStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
