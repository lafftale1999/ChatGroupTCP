package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    int serverPort = 55555;
    ServerProtocol serverProtocol = new ServerProtocol();

    public ServerListener() {
        serverProtocol.start();

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept(); // ⬅️ inte try-with-resources!
                    ServerClientHandler clientHandler = new ServerClientHandler(socket, serverProtocol);
                    serverProtocol.addWriter(clientHandler.getDataOut());
                    clientHandler.start();
                } catch (IOException e) {
                    System.out.println("Kunde inte acceptera klient: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Kunde inte starta serversocket: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ServerListener serverListener = new ServerListener();
    }
}
