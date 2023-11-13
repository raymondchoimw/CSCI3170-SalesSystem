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
                
                // Routing
                switch (choice) {
                    case 1:
                        listSalespersons();
                        break;
                    case 2:
                        countTransactionsByExpRange();
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case menuItem:
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        break;

                    default:
                        throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.err.printf("Invalid input. Please enter a valid integer between 1 and %d, inclusive.\n", menuItem);
                keyboard.nextLine(); // Clear the input buffer
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
            keyboard.nextLine(); // Clear the input buffer
            if (choice < 1 || choice > 2) throw new InputMismatchException();
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
                        System.err.println("[Illegal Arguement] Upper bound should be larger than or equal to lower bound. Please input again.");
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
            } catch (InputMismatchException e) {
                System.err.println("[Invalid input]: Please enter a valid integer.");
                System.err.println("Let's start again from *Lower* bound.");
                keyboard.nextLine(); // Clear the input buffer
            } 
        }
    }
}
