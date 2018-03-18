package com.archives.models;

/**
 * Created by locall on 2/14/2018.
 */

public class student {
    private String name;
    private String id;
    private String project_name;
    private String project_id ;
    private String Token;
    private String password;
    private String msg;
    private String title_msg;

    public student(){}

    public student(String name,String id){
        this.name =name;
        this.id=id;
    }

    public student(String name,String id,String project_name,String project_id){
        this.name =name;
        this.id=id;
        this.project_id=project_id;
        this.project_name=project_name;

    }

    public String getTitle_msg() {
        return title_msg;
    }

    public void setTitle_msg(String title_msg) {
        this.title_msg = title_msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProject_id() {
        return project_id;
    }


    public String getProject_name() {
        return project_name;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }



    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


}
