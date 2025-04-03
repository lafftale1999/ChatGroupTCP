package Client;
/*Object that listens to actions performed in the GUI and then send corresponding information to the server.*/
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

    InetAddress serverAddress;
    Socket socket;
    PrintWriter dataOut;

    public ClientSender(ChatWindow chatWindow, User currentUser, Socket socket) {
        this.chatWindow = chatWindow;
        this.currentUser = currentUser;
        this.socket = socket;
        createListeners();
        try {
            dataOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error instantiating ChatReceiverSender!");
        }
        sendHandShake();
    }

    public void createListeners() {
        chatWindow.getStopButton().addActionListener(e -> {
            currentUser.setActive(false);
            sendLastBreath();
            System.exit(0);
        });

        chatWindow.getSendMessagePanel().getSendButton().addActionListener(e -> sendMessage());

        // Enter sends message and shift + Enter = \n
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

    public void sendHandShake() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(currentUser);
            dataOut.println(json);

            // Send hello message in chat.
            String greetChat = currentUser.getUserName() + " has entered the chat";
            Message message = new Message(greetChat, currentUser);
            json = mapper.writeValueAsString(message);
            dataOut.println(json);

        } catch (JsonProcessingException e) {
            System.out.println("Error mapping currentUser as json in ClientSender.java - sendHandShake()");
        }
    }

    public void sendMessage() {
        // Check that the text area isn't empty - then parse message to json
        if(!chatWindow.getSendMessagePanel().getMessageTextArea().getText().isEmpty()) {
            Message message = new Message(chatWindow.getSendMessagePanel().getMessageTextArea().getText(), currentUser);

            ObjectMapper mapper = new ObjectMapper();
            String json = null;

            try {
                json = mapper.writeValueAsString(message);
                dataOut.println(json);
            } catch (IOException e) {
                System.out.println("Something went wrong in ClientSender.java sendMessage()");
            }

            chatWindow.getSendMessagePanel().getMessageTextArea().setText("");
        }
        else {
            JOptionPane.showMessageDialog(chatWindow, "You must enter something before sending!");
        }
    }

    public void sendLastBreath() {
        try {
            // Remove user from group chat window by sending inactive user.
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(currentUser);
            dataOut.println(json);

            // Send goodbye message in chat.
            String leaveChat = currentUser.getUserName() + " has left the chat";
            Message message = new Message(leaveChat, currentUser);
            json = mapper.writeValueAsString(message);
            dataOut.println(json);
        } catch (IOException ex) {
            System.out.println("ClientSender sendLastBreath() - Error when sending last breath: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
