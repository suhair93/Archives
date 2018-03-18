package com.archives.models;

/**
 * Created by locall on 3/6/2018.
 */

public class comment {
    private String comment;
    private String supervisorId;
    private String studentId;
    private String Name;

    private String adminId;
    private String projectname;

    public comment(){}

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public String getComment() {
        return comment;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return Name;
    }


    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setName(String Name) {
        this.Name = Name;
    }


}
