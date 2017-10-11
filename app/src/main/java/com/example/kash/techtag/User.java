package com.example.kash.techtag;

/*
 * Copyright 2017  Koushhik Kumar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
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
