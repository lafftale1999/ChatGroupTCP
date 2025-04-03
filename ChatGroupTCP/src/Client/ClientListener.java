package Client;

import GUI.ChatWindow;
import User.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ClientListener extends Thread{

    ChatWindow chatWindow;
    ClientProtocol protocol;
    BufferedReader dataIn;

    public ClientListener(ChatWindow chatWindow, User currentUser, Socket socket) {
        this.chatWindow = chatWindow;
        this.protocol = new ClientProtocol(chatWindow, currentUser);

        try{
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("ClientListener.java Constructor() - Error instantiating dataIn: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while(!this.isInterrupted()) {
            try {
                String json = dataIn.readLine();
                protocol.runProtocol(json);
            } catch (SocketException socketException) {
                System.out.println("ClientListener run() - Socket closed, ending thread: " + socketException.getMessage());
                socketException.printStackTrace();
                break;
            } catch (IOException e) {
                System.out.println("ClientListener run() - Something went wrong, closing thread: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }
}
