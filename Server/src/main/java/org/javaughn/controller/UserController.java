package org.javaughn.controller;

import org.javaughn.factory.DatabaseFactory;
import org.javaughn.model.User;

import java.sql.*;

public class UserController {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private int affectedRows;

    public UserController() {
        this.connection = DatabaseFactory.getConnection();
    }

    public User signUp(User user){
        String sql = "INSERT into public.user values (default,?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getPassword());

            affectedRows = preparedStatement.executeUpdate();
            if(affectedRows == 1) //add log

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            user.setId(resultSet.getInt(1));

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public User login(String username, String password){
        User user = null;
        String sql = "Select * from public.user where username=(?)";
        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() != false) {
                do {
                    int id = resultSet.getInt("id");
                    String email = resultSet.getString("email");
                    String dbpassword = resultSet.getString("password");
                    if(password.equals(dbpassword)){
                        user = new User(id,username,email,null);
                    }
                } while (resultSet.next());
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public User getUser(int userId){
        User user = null;
        String sql = "Select * from public.user where id=(?)";
        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() != false) {
                do {
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    user = new User(userId,username,email,null);
                } while (resultSet.next());
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(String username){
        User user = null;
        String sql = "Select * from public.user where username=(?)";
        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next() != false) {
                do {
                    int userId = resultSet.getInt("id");
                    String email = resultSet.getString("email");
                    user = new User(userId, username, email, null);
                } while (resultSet.next());
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
