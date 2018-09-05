package com.pango.hsec.hsec.model;

/**
 * Created by BOB on 29/08/2018.
 */

public class LoginModel {
    String username;
    String password;
    String domain;
    public LoginModel(String user, String pass){
        username=user;
        password= pass;
        domain="anyaccess";
    }
}
