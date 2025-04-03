package Client;

import GUI.ChatWindow;
import Message.Message;
import User.User;
import User.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientProtocol {

    private enum States {
        INITIALIZE,
        READY_TO_RECEIVE
    }

    States state = States.INITIALIZE;
    ChatWindow chatWindow;
    User currentUser;

    public ClientProtocol(ChatWindow chatWindow, User currentUser) {
        this.chatWindow = chatWindow;
        this.currentUser = currentUser;
    }

    public void runProtocol(String jsonString) {
        if (state == States.READY_TO_RECEIVE) {
            this.doAction(jsonString);
            System.out.println("READY_TO_RECEIVE");
        }
        else if (state == States.INITIALIZE) {
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("INITIALIZING");
            if(jsonString.contains("\"users\"") && !jsonString.contains("\"message\"")) {
                try {
                    Users users = mapper.readValue(jsonString, Users.class);
                    chatWindow.getChatAreaPanel().getUserTextArea().updateUsers(users);
                    state = States.READY_TO_RECEIVE;
                    System.out.println("Users added");
                } catch (JsonMappingException e) {
                    System.out.println("ClientProtocol.java runProtocol() - mapping fault: " + e.getMessage());
                    e.printStackTrace();
                } catch (JsonProcessingException e) {
                    System.out.println("ClientProtocol.java runProtocol() - Procession fault: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void doAction(String jsonString) {
        System.out.println("-------------------JSON RECEIVED-----------------------");
        System.out.println(jsonString);

        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode root = mapper.readTree(jsonString);

            if(root.has("users")) {
                Users users = mapper.readValue(jsonString, Users.class);
                chatWindow.getChatAreaPanel().getUserTextArea().updateUsers(users);

                for(User user : users.getUsers()) {
                    System.out.println(user.getUserName());
                }
            }
            else if(root.has("userName") && !root.has("message")) {
                User user = mapper.readValue(jsonString, User.class);
                // IF USER IS ACTIVE AND NOT IN THE USER LIST - ADD TO USER LIST
                if (user.isActive() && !chatWindow.getChatAreaPanel().getUserTextArea().getUsers().contains(user.getUserName())) {
                    chatWindow.getChatAreaPanel().getUserTextArea().addUser(user);

                    // DEBUG
                    System.out.println("User added");

                }
                else if(!user.isActive()) {
                    System.out.println("Removing users");
                    chatWindow.getChatAreaPanel().getUserTextArea().removeUser(user);
                }
                else {
                    System.out.println("Something went wrong when deleting users");
                }
            }
            else if(root.has("message") && root.has("date")) {
                Message message = mapper.readValue(jsonString, Message.class);
                if(message != null) {
                    chatWindow.getChatAreaPanel().getChatTextArea().addMessage(message);
                }
            }
            else {
                System.out.println("Something went wrong in ClientProtocol - doAction");
            }
        } catch (JsonMappingException e) {
            System.out.println("ClientProtocol.java doAction() - mapping fault: " + e.getMessage());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            System.out.println("ClientProtocol.java doAction() - Procession fault: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
