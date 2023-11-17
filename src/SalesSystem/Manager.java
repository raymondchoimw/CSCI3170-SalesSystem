package SalesSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Manager {
    private final static int menuItem = 5;
    private static Connection connection = null;
    private static Scanner keyboard = null;
    
    private static void setConnection(Connection newConnection) {
        connection = newConnection;
    }
    private static void setKeyboard(Scanner newKeyboard) {
        keyboard = newKeyboard;
    }

    public static void init(Connection newConnection, Scanner newKeyboard) {
        if (connection == null) {
            setConnection(newConnection);
        }
        if (keyboard == null) {
            setKeyboard(newKeyboard);
        }
        menu();
    }
    private static void menu() {
        // Initialize scanner to read user input
        int choice = 0;

        do {
            try {
                System.out.println("-----Operations for manager menu-----");
                System.out.println("What kinds of operation would you like to perform?");

                System.out.println("1. List all salespersons");
                System.out.println("2. Count the no. of sales record of each salesperson under a specific range on years of experience");
                System.out.println("3. Show the total sales value of each manufacturer");
                System.out.println("4. Show the N most popular part");
                System.out.println("5. Return to the main menu");
                System.out.print("Enter Your Choice: ");

                // Get user's choice
                choice = keyboard.nextInt();
                keyboard.nextLine(); // Clear the input buffer
                
                // Routing
                switch (choice) {
                    case 1:
                        listSalespersons();
                        break;
                    case 2:
                        countTransactionsByExpRange();
                        break;
                    case 3:
                        manufacturerTotalSalesValue();
                        break;
                    case 4:
                        nMostPopularPart();
                        break;
                    case menuItem:
                        App.clearScreen();
                        break;

                    default:
                        throw new IllegalArgumentException();
                }
            } catch (InputMismatchException e) {
                System.err.printf("[Invalid input]: Please enter a valid integer between 1 and %d, inclusive.\n", menuItem);
                keyboard.nextLine(); // Clear the input buffer
            } catch (IllegalArgumentException e) {
                System.err.printf("[Invalid input]: Please enter a valid integer between 1 and %d, inclusive.\n", menuItem);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        } while (choice != menuItem);
        keyboard.reset();
    }

    private static void listSalespersons() throws SQLException {
        int choice;
        String orderDirection;

        boolean continueLoop = true;

        while (continueLoop) {
            try {
            System.out.println("Choose ordering:");
            System.out.println("1. By experience, ascending order");
            System.out.println("2. By experience, descending order");
            System.out.print("Enter your choice: ");

            // Get user's choice
            choice = keyboard.nextInt();
            if (choice < 1 || choice > 2) throw new IllegalArgumentException();
            keyboard.nextLine(); // Clear the input buffer
            orderDirection = (choice == 1)? "ASC" : "DESC";
            String query =
            "SELECT sID as ID, sName as Name, sPhoneNumber as 'Phone Number', sExperience as 'Years of Experience'\n"
            + "FROM salesperson\n"
            + "ORDER BY sExperience " + orderDirection;

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            Database.printResultSet(resultSet);
            System.out.println("End of Query");

            continueLoop = false;
            // Clean up resources
            resultSet.close();
            statement.close();
            } catch (InputMismatchException e) {
                System.err.println("[Invalid input]: Please enter a valid integer between 1 and 2, inclusive.");
                keyboard.nextLine(); // Clear the input buffer
            } catch (IllegalArgumentException e) {
                System.err.println("[Invalid input]: Please enter a valid integer between 1 and 2, inclusive.");
            }
        }
    }

    private static void countTransactionsByExpRange () throws SQLException {
        boolean continueLoop = true;
        int lowerBound = 0, upperBound = -1;
        while (continueLoop) {
            try {
                while (upperBound < lowerBound) {
                    System.out.print("Type in the lower bound for years of experience: ");
                    lowerBound = keyboard.nextInt();
                    keyboard.nextLine();

                    System.out.print("Type in the upper bound for years of experience: ");
                    upperBound = keyboard.nextInt();
                    keyboard.nextLine();

                    if (upperBound < lowerBound) {
                        System.err.println("[Illegal Arguement]: Upper bound should be larger than or equal to lower bound. Please input again.");
                    }
                }
                System.out.printf("WHERE sExperience BETWEEN %d AND %d", lowerBound, upperBound);

                String query = 
                "SELECT salesperson.sID as ID, sName as Name, sExperience as 'Years of Experience', t.count as 'Number of Transaction'\n"
                + "FROM salesperson\n"
                + "INNER JOIN (SELECT sID, count(sID) as count from transaction GROUP BY sID) t ON salesperson.sID = t.sID\n"
                + "WHERE sExperience BETWEEN ? AND ?\n"
                + "ORDER BY ID DESC";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, lowerBound);
                statement.setInt(2, upperBound);
                ResultSet resultSet = statement.executeQuery();
                Database.printResultSet(resultSet);
                System.out.println("End of Query"); 
                

                continueLoop = false;
                // Clean up resources
                resultSet.close();
                statement.close();
            } catch (InputMismatchException e) {
                System.err.println("[Invalid input]: Please enter a valid integer.");
                System.err.println("Let's start again from *Lower* bound.");
                keyboard.nextLine(); // Clear the input buffer
            } 
        }
    }
    private static void manufacturerTotalSalesValue() throws SQLException {
        String query =
        "SELECT part.mID as 'Manufacturer ID', mm.mName as 'Manufacturer Name' , sum(tt.count * pPrice) as 'Total Sales Value'\n"
        +"FROM part\n"
        +"INNER JOIN (SELECT pID, count(pID) as count FROM transaction GROUP BY pID) tt ON part.pID = tt.pID\n"
        +"INNER JOIN (SELECT mID, mName FROM manufacturer) mm on part.mID = mm.mID\n"
        +"GROUP BY part.mID\n"
        +"ORDER BY sum(tt.count * pPrice) DESC";

        //System.out.println(query);
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        Database.printResultSet(resultSet);
        System.out.println("End of Query");

        // Clean up resources
        resultSet.close();
        statement.close();
    }
    // [WIP] Clarifying sorting logic
    private static void nMostPopularPart() throws SQLException {
        int limit = 0;

        boolean continueLoop = true;

        while (continueLoop) {
            try {
                System.out.print("Type in the number of parts: ");

                limit = keyboard.nextInt();
                if (limit < 0) throw new IllegalArgumentException();
                keyboard.nextLine(); // Clear the input buffer

                continueLoop = false;
            } catch (InputMismatchException e) {
                System.err.printf("[Invalid input]: Please input a non-negative integer.\n");
                keyboard.nextLine(); // Clear the input buffer
            } catch (IllegalArgumentException e) {
                System.err.printf("[Invalid input]: Please input a non-negative integer.\n");
            }
        }
        String query =
        "SELECT part.pID as 'Part ID', pName as 'Part Name', tt.count as 'No.of transaction'\n"
        + "FROM part\n"
        + "INNER JOIN (SELECT pID, count(pID) as count FROM transaction GROUP BY pID) tt ON part.pID = tt.pID\n"
        + "ORDER BY tt.count DESC\n"
        + "LIMIT ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, limit);
        ResultSet resultSet = statement.executeQuery();
        Database.printResultSet(resultSet);

        // Clean up resources
        resultSet.close();
        statement.close();
    }
}
