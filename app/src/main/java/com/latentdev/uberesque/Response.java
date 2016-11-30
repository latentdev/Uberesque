package com.latentdev.uberesque;

/**
 * Created by Laten on 11/26/2016.
 */

public class Response {
    Boolean success;
    String error;
    User user;
    Vehicle vehicle;
    Response()
    {
        user = new User();
        vehicle = new Vehicle();
    }
}
