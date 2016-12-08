package com.latentdev.uberesque;

import java.util.List;

/**
 * Created by Laten on 11/26/2016.
 */

public class Response {
    public Boolean success;
    public String error;
    User user;
    Vehicle vehicle;
    public List<Ride> rides;
    Response()
    {
        user = new User();
        vehicle = new Vehicle();
    }
}

