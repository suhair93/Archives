package com.archives.models;

/**
 * Created by locall on 3/2/2018.
 */

public class student_project {
    private String nameStudent;
    private String numberStudent;
    private String nameProject;
    private String detailsProject;
    private String referancProject;
    private String adminId;
    private String supervisorId;

    public student_project(){}

    public String getAdminId() {
        return adminId;
    }

    public String getDetailsProject() {
        return detailsProject;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public void setDetailsProject(String detailsProject) {
        this.detailsProject = detailsProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public void setNameStudent(String nameStudent) {
        this.nameStudent = nameStudent;
    }

    public void setNumberStudent(String numberStudent) {
        this.numberStudent = numberStudent;
    }

    public void setReferancProject(String referancProject) {
        this.referancProject = referancProject;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getNameProject() {
        return nameProject;
    }

    public String getNameStudent() {
        return nameStudent;
    }

    public String getNumberStudent() {
        return numberStudent;
    }

    public String getReferancProject() {
        return referancProject;
    }

    public String getSupervisorId() {
        return supervisorId;
    }


}
