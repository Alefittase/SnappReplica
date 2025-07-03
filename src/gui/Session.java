package gui;

import model.*;
import service.TripService;

public class Session {
    private static Session session;
    private Passenger currentUser;

    public static Session getSession() {
        if (session == null) session = new Session();
        return session;
    }

    public Passenger getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(Passenger currentUser) {
        this.currentUser = currentUser;
    }

    public Trip getActiveTrip() {
        return TripService.getActiveTrip(currentUser);
    }
}