package org.javaughn.main;

/*
Author: Javaughn Stephenson
Date: January 23, 2023
 */

import org.javaughn.main.ServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private ServerSocket messageSocket;

    public Server(ServerSocket serverSocket,ServerSocket messageSocket){
        this.serverSocket = serverSocket;
        this.messageSocket = messageSocket;
    }

    public void startServer(){
        try {
            System.out.println("Waiting...");
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("A Client has connected" + socket.getInetAddress() + socket.getLocalAddress());
                ServerHandler serverHandler = new ServerHandler(socket,messageSocket);
                new Thread(serverHandler).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally{
            if (serverSocket!=null)
                try{
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }

    }

}
