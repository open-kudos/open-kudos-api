package com.codetutr.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chc on 15.7.23.
 */
public class Verificator {

    private static Map userList = new HashMap<String, User>();

    public static User getUser(String email){

        if(userList.containsKey(email)){
            return (User)userList.get("email");
        } else {
            return null;
        }

    }

}
