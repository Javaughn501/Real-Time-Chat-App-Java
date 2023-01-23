package org.javaughn.model;

/*
Author: Javaughn Stephenson
Date: January 23, 2023
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Conversation implements Serializable {

    private int id;
    private ArrayList<User> members;

    public Conversation() {
        members = new ArrayList<>();
    }
    public Conversation(int id,ArrayList<User> members) {
        this.id = id;
        this.members = members;
    }

    public Conversation(ArrayList<User> members) {
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<User> getMembers() {return members;}

    public void setMembers(ArrayList<User> members) {this.members = members;}

    public void addMember(User user) {this.members.add(user);}

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", members=" + members +
                '}';
    }
}
