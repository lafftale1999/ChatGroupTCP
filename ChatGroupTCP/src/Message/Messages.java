package Message;

import java.util.ArrayList;

public class Messages {
    private ArrayList<Message> messages = new ArrayList<>();

    public ArrayList<Message> getMessage() {
        return messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
