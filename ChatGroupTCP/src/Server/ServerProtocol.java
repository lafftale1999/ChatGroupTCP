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
    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
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
            System.out.println("Thread interrupted. Ending ServerProtocol");
        }
    }

    public void runProtocol(String jsonString) {
        System.out.println("---------------------RUNNING PROTOCOL----------------------------");
        System.out.println(jsonString);

        ObjectMapper mapper = new ObjectMapper();
        Object object = null;

        try {
            JsonNode root = mapper.readTree(jsonString);

            if(root.has("userName") && !root.has("message")) {
                User newUser = mapper.readValue(jsonString, User.class);
                System.out.println(" - user received: " + newUser.getUserName());

                if(newUser.isActive()) {
                    System.out.println(" - user is active");
                    if(!currentUsers.getUsers().contains(newUser)) {
                        System.out.println(" - user was not in list");
                        currentUsers.addUser(newUser);
                        object = currentUsers;
                    }
                }
                else {
                    currentUsers.removeUser(newUser);
                    object = newUser;
                }
            }
            else if(root.has("message")) {
                object = mapper.readValue(jsonString, Message.class);
            }

            System.out.println("SENDING OBJECT: " + object.toString());
            sendAll(object);
        } catch (JsonMappingException e) {
            System.out.println("Unable to map string");
        } catch (JsonProcessingException e) {
            System.out.println("Unable to parse Json");
        }
    }

    public void sendAll(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;

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

            System.out.println(json);
            if(json != null) {
                for(PrintWriter writer: writers) {
                    writer.println(json);
                    System.out.println("Message sent");
                }
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

    public ArrayList<PrintWriter> getWriters() {
        return this.writers;
    }
}
