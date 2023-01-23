package org.javaughn.controller;

/*
Author: Javaughn Stephenson
Date: January 23, 2023
 */

import org.javaughn.model.Message;
import org.javaughn.view.Messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageClient implements Runnable{

    private static Socket messageSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    //Messages View
    private Messages messages;

    public MessageClient(){
        try{
            if(messageSocket == null) messageSocket = new Socket("localhost",3306);
            this.outputStream = new ObjectOutputStream(messageSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(messageSocket.getInputStream());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run() {
        while(messageSocket.isConnected()){
            try{
                Message message = (Message)this.inputStream.readObject();
                messages.recieveMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addMessageView(Messages messages){
        this.messages = messages;
    }
}
