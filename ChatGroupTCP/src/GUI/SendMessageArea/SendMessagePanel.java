package GUI.SendMessageArea;

import javax.swing.*;
import java.awt.*;

public class SendMessagePanel extends JPanel {
    MessageTextArea messageTextArea = new MessageTextArea();
    SendButton sendButton = new SendButton();

    public SendMessagePanel() {
        this.setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                messageTextArea.scrollPane,
                sendButton
        );

        splitPane.setResizeWeight(0.8);
        splitPane.setDividerSize(5);
        splitPane.setOneTouchExpandable(true);

        this.add(splitPane, BorderLayout.CENTER);
    }

    public MessageTextArea getMessageTextArea() {
        return messageTextArea;
    }

    public void setMessageTextArea(MessageTextArea messageTextArea) {
        this.messageTextArea = messageTextArea;
    }

    public SendButton getSendButton() {
        return sendButton;
    }

    public void setSendButton(SendButton sendButton) {
        this.sendButton = sendButton;
    }
}
