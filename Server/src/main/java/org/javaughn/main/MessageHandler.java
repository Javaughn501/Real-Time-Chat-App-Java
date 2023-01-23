package org.javaughn.main;

/*
Author: Javaughn Stephenson
Date: January 23, 2023
 */

import org.javaughn.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageHandler implements Runnable{

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    MessageHandler(Socket socket){
        try{
            this.socket = socket;
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            closeEverything();
        }
    }

    @Override
    public void run() {
        while(socket.isConnected()){

        }
    }

    public void sendMessage(Message message) {
        try{
            this.outputStream.writeObject(message);
        } catch (IOException e) {
            closeEverything();
        }
    }

    private void closeEverything() {
        try{
            this.socket.close();
            this.outputStream.close();
            this.inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
