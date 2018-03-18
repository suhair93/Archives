package com.archives.models;


import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Belal on 2/23/2017.
 */
@IgnoreExtraProperties
public class project {

    private String name;
    private String details;
    private String admin_id;
    private String referanc;
    private String supervisor_id;
    private List<student_project> students;
    public project() {
    }
    public project(String name, String details , String referanc, String supervisor_id , String admin_id) {
        this.name = name;
        this.details= details;
        this.supervisor_id=supervisor_id;
        this.referanc=referanc;
        this.admin_id=admin_id;
    }

    public List<student_project> getStudents() {
        return students;
    }

    public void setStudents(List<student_project> students) {
        this.students = students;
    }



    public String getReferanc() {
        return referanc;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setReferanc(String referanc) {
        this.referanc = referanc;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getSupervisor_id() {
        return supervisor_id;
    }

    public void setSupervisor_id(String supervisor_id) {
        this.supervisor_id = supervisor_id;
    }
}