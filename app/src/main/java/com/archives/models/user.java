package com.archives.models;

/**
 * Created by locall on 2/14/2018.
 */

public class user {
    private String name;
    private String password;
    private String typeUser;
    private String Token;
    private String id;

    public  user(){}

    public user(String username,String password,String typeUser){
        this.name=username;
        this.password=password;
        this.typeUser=typeUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return Token;
    }

    public String getPassword() {
        return password;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public String getName() {
        return name;
    }

    public void setToken(String token) {
        Token = token;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public void setName(String username) {
        this.name = username;
    }
}
