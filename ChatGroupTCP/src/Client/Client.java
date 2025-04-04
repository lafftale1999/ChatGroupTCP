package Client;

import GUI.ChatWindow;
import User.User;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    User currentUser;
    ChatWindow chatWindow;
    int serverPort = 55555;

    // Create current User and GUI
    public Client() {
        while(true) {
            String userName = JOptionPane.showInputDialog("What is your username?");
            if(userName != null && !userName.isEmpty()) {
                currentUser = new User(userName);
                chatWindow = new ChatWindow(userName);
                System.out.println("Username is: " + userName);
                break;
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        try{
            InetAddress serverAddress = InetAddress.getLocalHost();
            try(Socket socket = new Socket(serverAddress, client.serverPort)){

                //Create sender. Listens to the actions performed in GUI and SENDS corresponding data.
                ClientSender clientSender = new ClientSender(client.chatWindow, client.currentUser, socket);

                // Separate thread for listening to incoming data from Server. Also creates an instance of ClientProtocol
                ClientListener clientListener = new ClientListener(client.chatWindow, client.currentUser, socket);
                clientListener.start();
                while(true) {}

            } catch (IOException e) {
                System.out.println("Client.java - Socket closed: " + e.getMessage());
            }
        } catch (UnknownHostException e) {
            System.out.println("Client.java - Unable to find host: " + e.getMessage());
        }
    }
}
