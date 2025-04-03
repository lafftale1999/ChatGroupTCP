package GUI.ChatArea;

import Message.Message;

import javax.swing.*;
import java.awt.*;

public class ChatTextArea extends JTextArea {
    JScrollPane scrollPane = new JScrollPane(this);

    public ChatTextArea() {
        this.setEditable(false);
        this.setLineWrap(true);
        this.setCaretColor(Color.WHITE);
    }

    public void addMessage(Message message) {
        String tempMessage = message.getUser().getUserName() + " " + message.getDate() + ": " + message.getMessage() + "\n";
        this.append(tempMessage);
        this.setCaretPosition(this.getDocument().getLength());
        this.draw();
        System.out.println("Message added");
    }

    public void draw(){
        this.revalidate();
        this.repaint();
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
}
