package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerClientHandler extends Thread{
    private PrintWriter dataOut;
    private final Socket socket;
    private final ServerProtocol protocol;

    public ServerClientHandler(Socket socket, ServerProtocol serverProtocol) {
        this.socket = socket;
        this.protocol = serverProtocol;
        try {
            dataOut = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            System.out.println("Failed to create PrintWriter in ServerClientHandler");
        }
    }

    @Override
    public void run() {
        String clientRequest;
        try(BufferedReader dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while((clientRequest = dataIn.readLine()) != null) {
                System.out.println("------------------------------------------------------------------------------------");
                System.out.println("Request: " + clientRequest);
                protocol.addTask(clientRequest);
            }
        } catch (IOException e) {
            System.out.println("ServerClientHandler run() - Socket closed, ending thread." + e.getMessage());
            e.printStackTrace();
        }
    }

    public PrintWriter getDataOut() {
        return dataOut;
    }
}
