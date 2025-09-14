package server;

import server.socket.SocketStarter;

public class ServerMain {
    public static void main(String[] args) {
        SocketStarter socketStarter = new SocketStarter();
        socketStarter.start();
    }
}