package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    int serverPort = 55555;
    ServerProtocol serverProtocol = new ServerProtocol();

    public ServerListener() {
        // Starting the thread for the shared protocol between all instances of ServerClientHandler
        serverProtocol.start();

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();

                    // Creates a handler for each separate client connecting to server
                    ServerClientHandler clientHandler = new ServerClientHandler(socket, serverProtocol);

                    // Adds writer to a list in serverProtocol to be able to send out all
                    serverProtocol.addWriter(clientHandler.getDataOut());
                    clientHandler.start();
                } catch (IOException e) {
                    System.out.println("ServerListener Constructor() - Could not accept client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("ServerListener Constructor() - Could start socket: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ServerListener serverListener = new ServerListener();
    }
}
