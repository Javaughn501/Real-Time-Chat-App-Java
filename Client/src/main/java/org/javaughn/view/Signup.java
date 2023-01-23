package org.javaughn.view;

import org.javaughn.controller.Client;
import org.javaughn.model.User;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class Signup {

    private JFrame jframe;
    private JPanel panel;
    private JLabel username, email, password,confirmPassword, passwordNotMatch;
    private JTextField usernameText, emailText, passwordText,confirmPasswordText;
    private JButton submitBtn;


    public Signup() {
        initializeComponents();
        addComponentsToPanel();
        setActionListener();
        setWindowProperty();
    }

    private void initializeComponents(){
        jframe = new JFrame();

        panel = new JPanel(new GridLayout(6, 5));

        username = new JLabel("Username");
        email = new JLabel("Email");
        password = new JLabel("Password");
        confirmPassword = new JLabel("confirmPassword");
        passwordNotMatch = new JLabel();


        usernameText = new JTextField();
        emailText = new JTextField();
        passwordText = new JTextField();
        confirmPasswordText = new JTextField();


        submitBtn = new JButton("Signup");
    }

    private void addComponentsToPanel() {


        panel.add(username);
        panel.add(usernameText);

        panel.add(email);
        panel.add(emailText);

        panel.add(password);
        panel.add(passwordText);

        panel.add(confirmPassword);
        panel.add(confirmPasswordText);

        panel.add(passwordNotMatch);

        panel.add(submitBtn, BorderLayout.CENTER);


    }

    private void setActionListener() {

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(passwordText.getText().equals(confirmPasswordText.getText())) {
                    passwordNotMatch.setText("");
                    String username = usernameText.getText();
                    String email = emailText.getText();
                    String password = passwordText.getText();
                    User user = new User(username,email,password);
                    User newUser = new Client().signup(user);
                    if(newUser!=null){
                        jframe.dispose();
                        new Client().startMessageClient();
                        Messages messages = new Messages(newUser);
                        new Client().addMessagesView(messages);
                    }else{
                        passwordNotMatch.setText("Invalid");
                    }
                }
                else{
                    passwordNotMatch.setText("Password doesn't match");
                }
            }
        });

    }

    private void setWindowProperty() {
        jframe.setLayout(new BorderLayout(1, 1));
        jframe.add(panel, BorderLayout.CENTER);
        jframe.setTitle("Signup");
        jframe.setVisible(true);
        jframe.setSize(320, 250);
        jframe.setResizable(false);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
