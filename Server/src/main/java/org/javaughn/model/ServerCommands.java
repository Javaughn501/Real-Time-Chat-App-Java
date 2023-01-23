package org.javaughn.model;

import java.io.Serializable;

public enum ServerCommands implements Serializable {
    SIGNUP,LOGIN,POSTCONVERSATION,GETCONVERSATION,POSTMESSAGE,GETMESSAGE, SEARCHUSER;
}
