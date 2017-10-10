package com.example.kash.techtag;

/**
 * Created by kash on 5/2/17.
 */

public class User {
    public String email;
    public String uid;
    public double longitude;
    public double latitude;
    public String tagged;

    User()  {

    }

    public User(String email, String uid, double latitude, double longitude, String tagged) {
        this.email = email;
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tagged = tagged;
    }
}
