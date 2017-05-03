package com.example.kash.techtag;

/**
 * Created by kash on 5/2/17.
 */

public class User {
    public String email;
    public String uid;
    public long longitude;
    public long latitude;
    public String tagged;

    User()  {

    }

    public User(String email, String uid, long longitude, long latitude, String tagged) {
        this.email = email;
        this.uid = uid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.tagged = tagged;
    }
}
