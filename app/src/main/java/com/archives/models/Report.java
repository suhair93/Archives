package com.archives.models;


import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Belal on 2/23/2017.
 */
@IgnoreExtraProperties
public class Report {

    private String name;
    private String url;
    private String admin_id;
    private String student_id;
    private String supervisor_id;
    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Report() {
    }
    public Report(String name, String url , String student_id, String supervisor_id , String admin_id) {
        this.name = name;
        this.url= url;
        this.supervisor_id=supervisor_id;
        this.student_id=student_id;
        this.admin_id=admin_id;
    }


    public String getStudent_id() {
        return student_id;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getSupervisor_id() {
        return supervisor_id;
    }

    public void setSupervisor_id(String supervisor_id) {
        this.supervisor_id = supervisor_id;
    }
}