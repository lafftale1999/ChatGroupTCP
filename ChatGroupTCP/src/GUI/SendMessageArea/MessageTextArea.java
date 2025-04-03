package GUI.SendMessageArea;

import javax.swing.*;

public class MessageTextArea extends JTextArea {
    JScrollPane scrollPane = new JScrollPane(this);

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
}
