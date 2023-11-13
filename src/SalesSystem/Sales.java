package SalesSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Sales {
    private final static int menuItem = 3;
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

    public static void menu() {
        // Initialize scanner to read user input
        int choice = 0;

        do {
            try {
                System.out.println("-----Operations for salesperson menu-----");
                System.out.println("What kinds of operation would you like to perform?");

                System.out.println("1. Search for parts");
                System.out.println("2. Sell a part");
                System.out.println("3. Return to the main menu");
                System.out.print("Enter Your Choice: ");

                // Get user's choice
                choice = keyboard.nextInt();
                keyboard.nextLine(); // Clear the input buffer

                
                // Routing
                switch (choice) {
                    case 1:
                        searchForParts();
                        break; // Placeholder
                    case 2:
                        break; // Placeholder

                    case menuItem:
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        break;

                    default:
                        throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.printf("[Invalid Input]: Please enter a valid integer between 1 and %d.\n", menuItem);
            } catch (SQLException e) {
                System.out.println(e);
            }
        } while (choice != menuItem);
        keyboard.reset();
    }

    private static void searchForParts() throws SQLException {
        boolean continueLoop = true;
        while (continueLoop) {
            try {
                int choice;

                String tableName, fieldName, keyword, orderDirection;

                System.out.println("Choose the search criterion:");
                System.out.println("1. Part Name");
                System.out.println("2. Manufacturer Name");

                System.out.print("Enter your choice: ");
                // Get user's choice
                choice = keyboard.nextInt();
                keyboard.nextLine(); // Clear the input buffer
                if (choice < 1 || choice > 2) throw new InputMismatchException("[Invalid input]: Please enter a valid integer between 1 and 2, inclusive.");
                tableName = (choice == 1)? "part": "manufacturer";
                fieldName = tableName.charAt(0) + "Name";
                
                //System.out.printf("FROM %s WHERE %s\n", tableName, fieldName);

                System.out.print("Type in the Search Keyword: ");
                keyword = keyboard.nextLine();

                //System.out.println("keyword: " + keyword);

                System.out.println("Choose ordering:");
                System.out.println("1. By price, ascending order");
                System.out.println("2. By price, descending order");
                System.out.print("Enter your choice: ");

                // Get user's choice
                choice = keyboard.nextInt();
                keyboard.nextLine(); // Clear the input buffer
                if (choice < 1 || choice > 2) throw new InputMismatchException("[Invalid input]: Please enter a valid integer between 1 and 2, inclusive.");
                orderDirection = (choice == 1)? "ASC" : "DESC";
                
                //System.out.println("ORDER BY " + orderDirection);


                String query = 
                "SELECT pID as ID, pName as Name, mName as Manufacturer, cName as Category, pAvailableQuantity as Quantity, pWarrantyPeriod as Warranty, pPrice as Price "
                + "FROM part "
                + "INNER JOIN (SELECT mID, mName FROM manufacturer) m ON part.mID = m.mID "
                + "INNER JOIN category ON part.cID=category.cID "
                + String.format("WHERE %s LIKE ? ", fieldName)
                + "ORDER BY Price " + orderDirection;
                //System.out.println(query);

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, "%" + keyword + "%");
                ResultSet resultSet = statement.executeQuery();
                Database.printResultSet(resultSet);

                continueLoop = false;
            } catch (InputMismatchException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
