package org.javaughn.view;

import org.javaughn.controller.Client;
import org.javaughn.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {

    JFrame jframe;
    JPanel mainPanel;
    JLabel usernameLabel,passwordLabel,invalidLoginLabel;
    JTextField usernameField;
    JPasswordField passwordField;
    JButton signupButton,loginButton;

    public Login(){
        initializeComponents();
        addComponentsToPanel();
        addActionListeners();
        setWindowProperty();
    }

    private void initializeComponents() {
        jframe = new JFrame();

        mainPanel = new JPanel(new GridLayout(4,2));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("password:");
        invalidLoginLabel = new JLabel();

        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");
    }
    private void addComponentsToPanel() {


        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);

        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        mainPanel.add(loginButton);
        mainPanel.add(signupButton);

        mainPanel.add(invalidLoginLabel);

    }

    private void addActionListeners(){

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                jframe.dispose();
                new Signup();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Client client = new Client();
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                User user = client.login(username,password);
                if(user!=null){
                    jframe.dispose();
                    client.startMessageClient();
                    Messages messages = new Messages(user);
                    client.addMessagesView(messages);
                }else{
                    invalidLoginLabel.setText("Invalid Login");
                }
            }
        });

    }

    private void setWindowProperty() {
        jframe.setTitle("Real-Time Chat Application");
        jframe.add(mainPanel);
        jframe.setSize(250, 150);
        jframe.setVisible(true);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);
    }

}
