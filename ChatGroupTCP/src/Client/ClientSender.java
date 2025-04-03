package Client;

import GUI.ChatWindow;
import Message.Message;
import User.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSender implements ActionListener {

    ChatWindow chatWindow;

    User currentUser;
    int serverPort = 55555;

    InetAddress serverAddress;
    Socket socket;
    PrintWriter dataOut;

    public ClientSender(ChatWindow chatWindow, User currentUser, Socket socket) {
        this.chatWindow = chatWindow;
        this.currentUser = currentUser;
        this.socket = socket;
        this.createListeners();
        try {
            dataOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error instantiating ChatReceiverSender!");
        }

        this.sendHandShake();
    }

    public void createListeners() {
        chatWindow.getStopButton().addActionListener(e -> {
            currentUser.setActive(false);
            sendLastBreath();
            System.exit(0);
        });


        chatWindow.getSendMessagePanel().getSendButton().addActionListener(e -> sendMessage());

        JTextArea inputArea = chatWindow.getSendMessagePanel().getMessageTextArea();

        inputArea.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "sendOnEnter");
        inputArea.getActionMap().put("sendOnEnter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputArea.getInputMap().put(KeyStroke.getKeyStroke("shift ENTER"), "insert-break");
    }

    public void sendLastBreath() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(currentUser);
            dataOut.println(json);
        } catch (IOException ex) {
            System.out.println("Error sending last Breath: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void sendMessage() {
        if(!chatWindow.getSendMessagePanel().getMessageTextArea().getText().isEmpty()) {
            Message message = new Message(chatWindow.getSendMessagePanel().getMessageTextArea().getText(), currentUser);

            ObjectMapper mapper = new ObjectMapper();
            String json = null;

            try {
                json = mapper.writeValueAsString(message);
                System.out.println("------------------- SENDING MESSAGE --------------------------");
                System.out.println(json);
                dataOut.println(json);
                System.out.println("---------------------------MESSAGE SENT---------------------------");
            } catch (IOException e) {
                System.out.println("Something went wrong in ClientSender.java sendMessage()");
            }

            chatWindow.getSendMessagePanel().getMessageTextArea().setText("");
        }
        else {
            JOptionPane.showMessageDialog(chatWindow, "You must enter something before sending!");
        }
    }

    public void sendHandShake() {
        try {
            // CREATE USER PACKET
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(currentUser);
            System.out.println("------------------------- SENDING HANDSHAKE --------------------------");
            System.out.println(json);
            dataOut.println(json);
            System.out.println("------------------------- HANDSHAKE SENT --------------------------");
        } catch (JsonProcessingException e) {
            System.out.println("Error mapping currentUser as json in ClientSender.java - sendHandShake()");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
