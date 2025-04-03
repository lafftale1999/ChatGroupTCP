package GUI.ChatArea;

import javax.swing.*;
import java.awt.*;

public class ChatAreaPanel extends JPanel {
    ChatTextArea chatTextArea = new ChatTextArea();
    UserTextArea userTextArea = new UserTextArea();

    public ChatAreaPanel() {
        this.setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                chatTextArea.scrollPane,
                userTextArea.scrollPane
        );

        splitPane.setResizeWeight(0.8);
        splitPane.setDividerSize(5);
        splitPane.setOneTouchExpandable(true);

        this.add(splitPane, BorderLayout.CENTER);
    }

    public ChatTextArea getChatTextArea(){
        return chatTextArea;
    }

    public UserTextArea getUserTextArea() {
        return userTextArea;
    }
}
