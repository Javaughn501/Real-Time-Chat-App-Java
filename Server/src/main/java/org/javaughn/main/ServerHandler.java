package org.javaughn.main;

import org.javaughn.controller.ConversationController;
import org.javaughn.controller.MessageController;
import org.javaughn.controller.UserController;
import org.javaughn.model.Conversation;
import org.javaughn.model.Message;
import org.javaughn.model.ServerCommands;
import org.javaughn.model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ServerHandler implements Runnable{

    private static HashMap<Integer,MessageHandler> connectedUsers = new HashMap<>();
    private User user;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private ServerSocket messageSocket;


    public ServerHandler(Socket socket,ServerSocket messageSocket){
        try {
            this.socket = socket;
            this.messageSocket = messageSocket;
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            closeEverything();
        }
    }

    @Override
    public void run() {

        ServerCommands serverCommand;
        while(socket.isConnected()){
            /*
            Accepts the server command and pass
            it to the helper function
            to determine what to do.
             */
            try {
                serverCommand = (ServerCommands) inputStream.readObject();
                handleCommands(serverCommand);
            } catch (IOException e) {
                closeEverything();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    Function to handle the commands instead of
    handling everything in the thread
     */
    private void handleCommands(ServerCommands serverCommand){
        try{
            switch (serverCommand){
                case SIGNUP -> {
                    User user = (User)inputStream.readObject();
                    User newUser = new UserController().signUp(user);
                    outputStream.writeObject(newUser);
                    if(newUser==null) break;


                    createMessageThread(user);
                    break;
                }
                case LOGIN -> {
                    String username = (String)inputStream.readObject();
                    String password = (String)inputStream.readObject();
                    User user = new UserController().login(username,password);
                    outputStream.writeObject(user);
                    if(user==null) break;

                    createMessageThread(user);
                    break;
                }
                case POSTCONVERSATION -> {
                    ArrayList<User> users = (ArrayList<User>)inputStream.readObject();
                    Conversation conversation = new ConversationController().postConversation(users);
                    outputStream.writeObject(conversation);
                    break;
                }
                case GETCONVERSATION -> {
                    int userId = (int) inputStream.readObject();
                    ArrayList<Conversation> allConversations = new ConversationController().getAllConversation(userId);
                    outputStream.writeObject(allConversations);
                    break;
                }
                case POSTMESSAGE -> {
                    Message message = (Message)inputStream.readObject();

                    new MessageController().postMessage(message);

                    //send message to the client
                    User user = new MessageController().getRecipientOfMessage(message.getConversationId(), message.getSenderId());
                    if(connectedUsers.get(user.getId())!=null){
                        connectedUsers.get(user.getId()).sendMessage(message);
                    }

                    break;
                }
                case GETMESSAGE -> {
                    int conversationId = (int) inputStream.readObject();
                    ArrayList<Message> allMessages = new MessageController().getMessage(conversationId);
                    outputStream.writeObject(allMessages);
                    break;
                }
                case SEARCHUSER -> {
                    String username = (String) inputStream.readObject();
                    User user = (User) inputStream.readObject();
                    User searchedUser = new UserController().getUser(username);
                    Conversation conversation = null;
                    if(searchedUser != null){
                        conversation = new ConversationController().postConversation(new ArrayList<>(Arrays.asList(user,searchedUser)));
//                        outputStream.writeObject(conversation);
                    }else{
//                        outputStream.writeObject(conversation);
                    }
                    break;
                }
            }

        } catch (IOException e) {
            closeEverything();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    starts a new thread where messages are sent for real time update
    */
    private void createMessageThread(User user){
        try{
            Socket message  = messageSocket.accept();
            MessageHandler messageHandler = new MessageHandler(message);
            new Thread(messageHandler).start();
            connectedUsers.put(user.getId(),messageHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeEverything(){
        try{
            this.socket.close();
            this.outputStream.close();
            this.inputStream.close();
            this.messageSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
