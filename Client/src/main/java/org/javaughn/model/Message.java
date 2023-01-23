package org.javaughn.model;

/*
Author: Javaughn Stephenson
Date: January 23, 2023
 */

import java.io.Serializable;

public class Message implements Serializable {

    private int conversationId;
    private int senderId;
    private String text;

    public Message(int conversationId, int senderId, String text) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.text = text;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "conversationId=" + conversationId +
                ", senderId=" + senderId +
                ", text='" + text + '\'' +
                '}';
    }
}
