package client.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import shared.request.Request;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketRequestSender {
    private final Socket socket;
    private final PrintStream printStream;
    private final Scanner scanner;
    private final ObjectMapper objectMapper;

    public SocketRequestSender() throws IOException {
        this.socket = new Socket("127.0.0.1", 8080);
        this.printStream = new PrintStream(socket.getOutputStream());
        this.scanner = new Scanner(socket.getInputStream());
        this.objectMapper = new ObjectMapper();
    }

    public void sendRequest(Request request) {
        try {
            printStream.println(objectMapper.writeValueAsString(request));
        } catch (Exception e) {
            System.err.println("Failed to send request: " + e.getMessage());
        }
    }

    public Scanner getServerScanner() {
        return this.scanner;
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