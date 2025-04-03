package GUI.ChatArea;

import User.User;
import User.Users;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserTextArea extends JTextArea {
    JScrollPane scrollPane = new JScrollPane(this);
    ArrayList<String> users = new ArrayList<>();

    public UserTextArea() {
        this.setEditable(false);
        this.setLineWrap(true);
        this.setCaretColor(Color.WHITE);
    }

    public void updateUsers(Users users) {
        this.setText("");
        this.setText("Users online:\n");

        this.users.clear();
        for(User user : users.getUsers()) {
            this.users.add(user.getUserName());
        }

        this.draw();
    }

    public void addUser(User user) {
        users.add(user.getUserName());
        this.draw();
    }

    public void removeUser(User user) {
        users.remove(user.getUserName());
        this.draw();
        System.out.println("User removed");
    }

    public void draw(){
        this.setText("Users online:\n");

        for(String user: users) {
            this.append(user + "\n");
        }

        this.setCaretPosition(this.getDocument().getLength());
        this.revalidate();
        this.repaint();
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public ArrayList<String> getUsers() {
        return users;
    }
}
