package gui;

import model.*;
import service.TripService;

public class Session {//handles transferring data between the Controller instances in different scenes
    private static Session session;
    private Passenger currentUser;
    //getter methods
    public static Session getSession() {
        if (session == null) session = new Session();
        return session;
    }
    public Passenger getCurrentUser() {
        return currentUser;
    }
    public Trip getActiveTrip() {
        return TripService.getActiveTrip(currentUser);
    }
    //setter method
    public void setCurrentUser(Passenger currentUser) {
        this.currentUser = currentUser;
    }
}