package org.javaughn.model;

/*
Author: Javaughn Stephenson
Date: January 23, 2023
 */

import java.io.Serializable;

public enum ServerCommands implements Serializable {
    SIGNUP,LOGIN,POSTCONVERSATION,GETCONVERSATION,POSTMESSAGE,GETMESSAGE, SEARCHUSER;
}
