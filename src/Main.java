import java.util.Scanner;
import model.*;
import service.*;
import repository.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static Passenger currentUser = null;

    public static void main(String[] args) {
        initializeSystem();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FileDataHandler.saveAllData();
            System.out.println("\nData saved during unexpected shutdown");
        }));
        runMainMenu();
    }

    private static void initializeSystem() {
        FileDataHandler.loadInitialData();
        
        if (UserRepository.getAllDrivers().isEmpty()) {
            DriverRepository.initializeDrivers();
            for (Driver driver : DriverRepository.getInitialDrivers()) {
                UserRepository.addDriver(driver);
            }
            FileDataHandler.saveDriverData();
        }
    }

    private static void runMainMenu() {
        while (true) {
            System.out.println("\n--- Main Menu ---");
            if (currentUser == null) {
                System.out.println("1. Register\n2. Login\n10. Exit");
            } else {
                System.out.println("Logged in as: " + currentUser.getName());
                System.out.println("3. Request Trip\n4. Start Trip\n5. End Trip\n6. Cancel Trip\n7. Trip Status\n8. Trip History\n9. Logout\n10. Exit");
            }

            int choice = getIntInput("Choice: ");

            if (currentUser == null) {
                handleUnauthenticatedMenu(choice);
            } else {
                handleAuthenticatedMenu(choice);
            }
        }
    }

    private static void handleUnauthenticatedMenu(int choice) {
        switch (choice) {
            case 1: registerUser(); break;
            case 2: loginUser(); break;
            case 10: shutdown(); System.exit(0);
            default: System.out.println("Invalid choice");
        }
    }

    private static void handleAuthenticatedMenu(int choice) {
        switch (choice) {
            case 3: requestTrip(); break;
            case 4: startTrip(); break;
            case 5: endTrip(); break;
            case 6: cancelTrip(); break;
            case 7: showTripStatus(); break;
            case 8: showTripHistory(); break;
            case 9: logout(); break;
            case 10: shutdown(); System.exit(0);
            default: System.out.println("Invalid choice");
        }
    }

    private static void registerUser() {
        String username = getInput("Enter username: ");
        String password = getInput("Enter password: ");
        
        String result = UserService.registerPassenger(username, password);
        if (result != null) {
            System.out.println(result);
        } else {
            System.out.println("Registration successful");
            FileDataHandler.saveUserData();
        }
    }

    private static void loginUser() {
        String username = getInput("Username: ");
        String password = getInput("Password: ");
        
        currentUser = UserService.login(username, password);
        if (currentUser == null) {
            System.out.println("Invalid credentials");
        } else {
            System.out.println("Login successful");
        }
    }

    private static void requestTrip() {
        Location start = getLocationInput("Enter start location (x y): ");
        Location end = getLocationInput("Enter end location (x y): ");

        Trip trip = TripService.requestTrip(currentUser, start, end);
        if (trip == null) {
            System.out.println("Failed to create trip. No available drivers.");
        } else {
            FileDataHandler.saveTripData();
            FileDataHandler.saveDriver(trip.getDriver());
            System.out.println("Trip created! ID: " + trip.getId());
        }
    }

    private static void startTrip() {
        String result = TripService.startTrip(currentUser);
        if (result != null) {
            System.out.println(result);
        } else {
            FileDataHandler.saveTripData();
            System.out.println("Trip started successfully");
        }
    }

    private static void endTrip() {
        Trip activeTrip = TripService.getActiveTrip(currentUser);
        if (activeTrip == null) {
            System.out.println("No active trip to end");
            return;
        }

        Driver driver = activeTrip.getDriver();
        String result = TripService.endTrip(currentUser);

        if (result != null) {
            System.out.println(result);
        } else {
            FileDataHandler.saveTripData();
            FileDataHandler.saveDriver(driver);
            System.out.println("Trip ended successfully");
        }
    }

    private static void cancelTrip() {
        Trip activeTrip = TripService.getActiveTrip(currentUser);
        if (activeTrip == null) {
            System.out.println("No active trip to cancel");
            return;
        }

        Driver driver = activeTrip.getDriver();
        String result = TripService.cancelTrip(currentUser);

        if (result != null) {
            System.out.println(result);
        } else {
            FileDataHandler.saveTripData();
            FileDataHandler.saveDriver(driver);
            System.out.println("Trip canceled successfully");
        }
    }

    private static void showTripStatus() {
        Trip activeTrip = TripService.getActiveTrip(currentUser);
        if (activeTrip != null) {
            System.out.println("Current Trip: " + activeTrip.getStatusString() + " (" + activeTrip.getFare() + "$)");
            System.out.println("Driver: " + activeTrip.getDriver().getName());
            System.out.println("Vehicle: " + activeTrip.getDriver().getVehicle());
        } else {
            System.out.println("No active trips");
        }
    }

    private static void showTripHistory() {
        System.out.println("--- Your Trips ---");
        for (Trip trip : TripService.getTripHistory(currentUser)) {
            System.out.printf("[%s] %s -> %s (%.2f$) - %s%n",
                    trip.getId(),
                    trip.getStart(),
                    trip.getEnd(),
                    trip.getFare(),
                    trip.getStatusString());
        }
    }

    private static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully");
    }

    private static Location getLocationInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            String[] coords = input.split("\\s+");
            
            if (coords.length != 2) {
                System.out.println("Error: Please enter exactly two numbers separated by space");
            } else if (isValidDouble(coords[0]) && isValidDouble(coords[1])) {
                double x = Double.parseDouble(coords[0]);
                double y = Double.parseDouble(coords[1]);
                return new Location(x, y);
            } else {
                System.out.println("Error: Coordinates must be valid numbers");
            }
        }
    }

    private static boolean isValidDouble(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private static String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.matches("\\d+")) {
                return Integer.parseInt(input);
            } else {
                System.out.println("Invalid number. Please enter digits only");
            }
        }
    }

    private static void shutdown() {
        FileDataHandler.saveAllData();
        System.out.println("System shutdown complete");
        System.exit(0);
    }
}