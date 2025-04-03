package Server;

import Message.Message;
import User.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerProtocol extends Thread{

    // Thread-safe queue with all the IO-tasks for program
    // Only used for addTask -> runProtocol -> sendAll
    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();

    // All PrintWriters for current handlers to be able to sendOut data from here
    private final ArrayList<PrintWriter> writers = new ArrayList<>();
    private final Users currentUsers = new Users();

    @Override
    public void run() {
        try {
            while(true) {
                Runnable task = tasks.take();
                task.run();
            }
        } catch (InterruptedException e) {
            System.out.println("ServerProtocol run() - Thread interrupted, ending ServerProtocol");
        }
    }

    public void runProtocol(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        Object object = null;

        try {
            JsonNode root = mapper.readTree(jsonString);
            // IF USER
            if(root.has("userName") && !root.has("message")) {
                User newUser = mapper.readValue(jsonString, User.class);
                // If active && not exists in current user - add to current user
                if(newUser.isActive() && !currentUsers.getUsers().contains(newUser)) {
                    currentUsers.addUser(newUser);
                    object = currentUsers;
                }
                // Else if user is not active - remove from currentUsers
                else if(!newUser.isActive()){
                    currentUsers.removeUser(newUser);
                    object = newUser;
                }
            }
            // IF MESSAGE
            else if(root.has("message")) {
                object = mapper.readValue(jsonString, Message.class);
            }

            // SEND DATA TO ALL CLIENTS
            if(object != null) {
                sendAll(object);
            }
            else {
                System.out.println("Something went wrong when creating object in ServerProtocol runProtocol()");
            }
        } catch (JsonMappingException e) {
            System.out.println("ServerProtocol runProtocol() - Unable to map string: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println("ServerProtocol runProtocol() - Unable to parse Json" + e.getMessage());
        }
    }

    public void sendAll(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;

        // Creating json depending on class
        try {
            if(object instanceof Message message) {
                json = mapper.writeValueAsString(message);
            }
            else if(object instanceof User user) {
                json = mapper.writeValueAsString(user);
            }
            else if(object instanceof Users users) {
                json = mapper.writeValueAsString(users);
            }

            if(json != null) {
                for(PrintWriter writer: writers) {
                    writer.println(json);
                    System.out.println("Response: " + json);
                    System.out.println("------------------------------------------------------------------------------------");
                }
            }
            else {
                System.out.println("Something went wrong when sending message in ServerProtocol sendAll()");
            }
        } catch (JsonProcessingException e) {
            System.out.println("Unable to parse JSON object: " + e.getMessage());
        }
    }

    public void addTask(String jsonString) {
        tasks.add(() -> runProtocol(jsonString));
    }

    public void addWriter(PrintWriter writer) {
        this.writers.add(writer);
    }
}
