package Message;

import User.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String message;
    private String date;
    private User user;

    public Message() {}

    public Message(String message, User user) {
        this.message = message;
        this.user = user;
        this.date = createDate();
    }

    private String createDate(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter nowFormated = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return nowFormated.format(now);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
