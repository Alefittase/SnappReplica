package model;

import java.util.Objects;

public class Passenger {
    private final String id;
    private final String name;
    private final String password;
    //constructor
    public Passenger(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
    //getter methods
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    //authentication method
    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    //checks if a passenger object is valid
    public boolean isValid() {
        return id != null && !id.isEmpty() && 
               name != null && !name.isEmpty() && 
               password != null && !password.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Passenger passenger = (Passenger) obj;
        return Objects.equals(id, passenger.id);
    }
    @Override
    public String toString() {
        return String.format("Passenger[%s - %s]", id, name);
    }
}