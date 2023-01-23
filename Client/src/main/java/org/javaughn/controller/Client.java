package org.javaughn.controller;

/*
Author: Javaughn Stephenson
Date: January 23, 2023
 */

import org.javaughn.model.Conversation;
import org.javaughn.model.Message;
import org.javaughn.model.ServerCommands;
import org.javaughn.model.User;
import org.javaughn.view.Messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {

    private static Socket socket;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    private static MessageClient messageClient;
    public Client(){
        try{
            if(socket == null) socket = new Socket("localhost",8080);
            if(outputStream == null) outputStream = new ObjectOutputStream(socket.getOutputStream());
            if(inputStream == null) inputStream = new ObjectInputStream(socket.getInputStream());

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public User login(String username, String password){
        User user;
        ServerCommands serverCommand = ServerCommands.LOGIN;
        try{
            outputStream.writeObject(serverCommand);
            outputStream.writeObject(username);
            outputStream.writeObject(password);
            user = (User)inputStream.readObject();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User signup(User user){
        User newUser;
        ServerCommands serverCommand = ServerCommands.SIGNUP;
        try{
            outputStream.writeObject(serverCommand);
            outputStream.writeObject(user);
            newUser = (User) inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return newUser;
    }

    public Conversation postConversation(ArrayList<User> users){
        Conversation conversation;
        ServerCommands serverCommand = ServerCommands.POSTCONVERSATION;
        try{
            outputStream.writeObject(serverCommand);
            outputStream.writeObject(users);
            conversation = (Conversation) inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conversation;
    }

    public ArrayList<Conversation> getConversations(int userId){
        ArrayList<Conversation> allConversations = null;
        ServerCommands serverCommand = ServerCommands.GETCONVERSATION;
        try{
            outputStream.writeObject(serverCommand);
            outputStream.writeObject(userId);
            allConversations = (ArrayList<Conversation>) inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return allConversations;
    }

    public void postMessage(Message message){
        ServerCommands serverCommand = ServerCommands.POSTMESSAGE;
        try{
            outputStream.writeObject(serverCommand);
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Message> getMessages(int conversationId){
        ArrayList<Message> allMessages;
        ServerCommands serverCommand = ServerCommands.GETMESSAGE;
        try{
            outputStream.writeObject(serverCommand);
            outputStream.writeObject(conversationId);
            allMessages = (ArrayList<Message>) inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return allMessages;
    }

    public void searchUser(String username,User user){

        Conversation conversation;
        ServerCommands serverCommand = ServerCommands.SEARCHUSER;
        try{
            outputStream.writeObject(serverCommand);
            outputStream.writeObject(username);
            outputStream.writeObject(user);
//            conversation = (Conversation) inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return;

    }

    public void startMessageClient(){
        if(messageClient == null) messageClient = new MessageClient();
        new Thread(messageClient).start();
    }
    public void addMessagesView(Messages messages){
        messageClient.addMessageView(messages);
    }

}
