package com.archives.models;

/**
 * Created by locall on 2/19/2018.
 */

public class message {
    private String title;
    private String contant;
    private String admin_id;
    private String student_id;

    public message(){}

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public void setContant(String contant) {
        this.contant = contant;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public String getContant() {
        return contant;
    }

    public String getStudent_id() {
        return student_id;
    }

    public String getTitle() {
        return title;
    }
}
