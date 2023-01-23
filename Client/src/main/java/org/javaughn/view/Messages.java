package org.javaughn.view;

import org.javaughn.controller.Client;
import org.javaughn.model.Conversation;
import org.javaughn.model.Message;
import org.javaughn.model.User;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Messages{

    private JFrame jframe;
    private JPanel mainPanel,leftMenu,messagePanel;

    private JTextArea textArea;
    private JTextField textField,searchField;

    private User user;
    private ArrayList<Conversation> userConversations;
    private ArrayList<Message> allMessages;
    private Conversation currentConversation;



    public Messages(User user){
        this.user = user;
        initializeComponents();
        addComponentsToPanel();
        addListenersToComponents();
        setWindowProperty();
    }


    private void initializeComponents() {
        jframe = new JFrame(user.getUsername() + " Messages");

        //holds all the panels
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));

        //left of the screen with each person
        leftMenu = new JPanel();
        leftMenu.setLayout(new BoxLayout(leftMenu, BoxLayout.PAGE_AXIS));
        leftMenu.setPreferredSize(new Dimension(250, Integer.MAX_VALUE));


        //display message of the clicked user
        messagePanel = new JPanel(new BorderLayout(0,2));

        //Search for a user
        searchField = new JTextField("Search for a user..");
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);

        textField = new JTextField();
    }

    private void addComponentsToPanel() {
        mainPanel.add(leftMenu);
        mainPanel.add(messagePanel);

        messagePanel.add(textArea,BorderLayout.CENTER);
        messagePanel.add(textField,BorderLayout.PAGE_END);

        leftMenu.add(searchField);

        friendComponent(leftMenu,textArea);
    }

    private void setWindowProperty() {
        jframe.add(mainPanel);
        jframe.setSize(1024, 680);
        jframe.setVisible(true);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(true);
    }

    private void addListenersToComponents(){
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER){

                    /*
                    TODO:Post the message and add it to the messages list
                     */
                    Message message = new Message(currentConversation.getId(), user.getId(),textField.getText());
                    textArea.append(user.getUsername() +": " + textField.getText()+"\n");
                    textField.setText("");
                    new Client().postMessage(message);
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        searchField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                searchField.setText("");
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                    new Client().searchUser(searchField.getText(), user);
                    searchField.setText("");
                    jframe.dispose();
                    new Messages(user);
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
    }


    private void friendComponent(JPanel panel, JTextArea textArea){

        /*
        TODO:Fetch the conversations for the logged in user
         */
        userConversations = new Client().getConversations(user.getId());

        //TODO:
        if(userConversations==null){
            searchField.setPreferredSize(new Dimension(Integer.MAX_VALUE,40));
            return;
        }
        for(Conversation conversation:userConversations){
            User friend = conversation.getMembers().get(0).getId() == user.getId() ? conversation.getMembers().get(1) : conversation.getMembers().get(0);
            JPanel conversationPanel = new JPanel();
            conversationPanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    //TODO:Fetch the messages for the friend
                    currentConversation = conversation;
                    allMessages = new Client().getMessages(conversation.getId());
                    /*
                    TODO:Loop through , if seder message is mine
                    username: message
                    else other username: message
                     */
                    textArea.setText("");
                    for(Message message:allMessages){
                        if(message.getSenderId() == user.getId()){
                            textArea.append(user.getUsername() +": " + message.getText() +"\n");
                        }
                        else{
                            textArea.append(friend.getUsername() +": " + message.getText()+"\n");
                        }
                    }

                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {

                }
            });

            JLabel username = new JLabel(friend.getUsername());
            conversationPanel.add(username);
            panel.add(conversationPanel);
        }

    }

    public void recieveMessage(Message message){

        User friend = currentConversation.getMembers().get(0).getId() == user.getId() ? currentConversation.getMembers().get(1) : currentConversation.getMembers().get(0);

        //TODO:Add it to the textField if the current conversation is clicked
        if(currentConversation.getId() == message.getConversationId()){
            if(message.getSenderId() == user.getId()){
                textArea.append(user.getUsername() +": " + message.getText()+"\n");
            }else{
                textArea.append(friend.getUsername() +": " + message.getText()+"\n");
            }
        }
    }
}
