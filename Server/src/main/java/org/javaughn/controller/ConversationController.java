package org.javaughn.controller;


/*
Author: Javaughn Stephenson
Date: January 23, 2023
 */

import org.javaughn.factory.DatabaseFactory;
import org.javaughn.model.Conversation;
import org.javaughn.model.User;

import java.sql.*;
import java.util.ArrayList;

public class ConversationController {

    private static Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;
    private int affectedRows;

    public ConversationController(){
        this.connection = DatabaseFactory.getConnection();
    }

    public Conversation postConversation(ArrayList<User> users){
        Conversation conversation = new Conversation();
        Boolean insertMember = true;

        String createConversationSql = "INSERT into public.conversation (id,timestamp) values (default,default)";
        String insertMembersSql = "Insert into public.\"conversationMembers\" (\"userId\",\"conversationId\") values (?,?)";
        try{
            preparedStatement = connection.prepareStatement(createConversationSql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int conversationId = resultSet.getInt(1);
            conversation.setId(conversationId);

            preparedStatement = connection.prepareStatement(insertMembersSql);
            for(User user:users){
                preparedStatement.setInt(1,user.getId());
                preparedStatement.setInt(2,conversationId);
                affectedRows = preparedStatement.executeUpdate();
                if(affectedRows != 1) insertMember = false;
                conversation.addMember(user);
            }

            if(!insertMember) conversation = null;

            return conversation;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Conversation> getAllConversation(int userId){
        UserController userController = new UserController();
        User user = userController.getUser(userId);
        if(user == null) return null;
        String conversationsSql = "Select * from public.\"conversationMembers\" where \"userId\"=(?)";
        String usersSql = "Select * from public.\"conversationMembers\" where \"conversationId\"=(?)";
        ArrayList<Conversation> conversations = new ArrayList<>();
        try{
            //Get all conversation the user is involved in
            preparedStatement = connection.prepareStatement(conversationsSql);
            preparedStatement.setInt(1,userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next() != false) {
                do {
                    int conversationId = resultSet.getInt("conversationId");
                    conversations.add(new Conversation(conversationId,new ArrayList<>()));
                } while (resultSet.next());
            }

            //Fetch the other users in the conversation
            for(Conversation conversation:conversations){
                preparedStatement = connection.prepareStatement(usersSql);
                preparedStatement.setInt(1,conversation.getId());
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next() != false) {
                    do {
                        if(resultSet.getInt("userId") == userId) continue;
                        conversation.addMember(user);
                        conversation.addMember(userController.getUser(resultSet.getInt("userId")));
                    } while (resultSet.next());
                }
            }

            return conversations;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
