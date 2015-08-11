package com.appslight.dentistapp.util;

/**
 * Created by Satyen on 02/06/2015.
 */
public class Detail {

    private int id;
    private String name;
    private String dob;
    private String email;
    private String gender;
    private String location;

    public Detail(int id, String name, String dob, String email, String gender, String location) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.gender = gender;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
