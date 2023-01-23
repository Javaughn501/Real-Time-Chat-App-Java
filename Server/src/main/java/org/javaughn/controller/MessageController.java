package org.javaughn.controller;

import org.javaughn.factory.DatabaseFactory;
import org.javaughn.model.Message;
import org.javaughn.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MessageController {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private int affectedRows;

    public MessageController() {
        this.connection = DatabaseFactory.getConnection();
    }

    public void postMessage(Message message){

        String sql = "INSERT into public.message (\"id\",\"conversationId\",\"senderId\",\"text\",\"timestamp\") values (default,?,?,?,default)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,message.getConversationId());
            preparedStatement.setInt(2,message.getSenderId());
            preparedStatement.setString(3,message.getText());
            affectedRows = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Message> getMessage(int conversationId){

        ArrayList<Message> allMessages = new ArrayList<>();
        String sql = "Select * from public.message where \"conversationId\"=(?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,conversationId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() != false) {
                do {
                    int senderId = resultSet.getInt("senderId");
                    String text = resultSet.getString("text");
                    allMessages.add(new Message(conversationId,senderId,text));
                } while (resultSet.next());
            }

            return allMessages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getRecipientOfMessage(int conversationId,int senderId){

        User user = null;

        String GetConversationMembersSql = "Select * from public.\"conversationMembers\" where \"conversationId\"=(?)";
        try {
            preparedStatement = connection.prepareStatement(GetConversationMembersSql);
            preparedStatement.setInt(1,conversationId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() != false) {
                do {
                    int userId = resultSet.getInt("userId");
                    if(userId!= senderId) user = new UserController().getUser(userId);
                } while (resultSet.next());
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
