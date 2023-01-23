package org.javaughn.main;

import org.javaughn.controller.ConversationController;
import org.javaughn.controller.MessageController;
import org.javaughn.controller.UserController;
import org.javaughn.model.Conversation;
import org.javaughn.model.Message;
import org.javaughn.model.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server(new ServerSocket(8080), new ServerSocket(3306));
        server.startServer();
    }
}