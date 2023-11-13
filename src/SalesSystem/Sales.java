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
                        performTransaction();
                        break; // Placeholder

                    case menuItem:
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        break;

                    default:
                        throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                keyboard.reset(); // Clear the input buffer
                System.err.printf("[Invalid Input]: Please enter a valid integer between 1 and %d.\n", menuItem);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
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
                "SELECT pID as ID, pName as Name, mName as Manufacturer, cName as Category, pAvailableQuantity as Quantity, pWarrantyPeriod as Warranty, pPrice as Price\n"
                + "FROM part\n"
                + "INNER JOIN (SELECT mID, mName FROM manufacturer) m ON part.mID = m.mID\n"
                + "INNER JOIN category ON part.cID=category.cID\n"
                + String.format("WHERE %s LIKE ? \n", fieldName)
                + "ORDER BY Price " + orderDirection;
                //System.out.println(query);

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, "%" + keyword + "%");
                ResultSet resultSet = statement.executeQuery();
                Database.printResultSet(resultSet);
                System.out.println("End of Query");

                continueLoop = false;
                // Clean up resources
                resultSet.close();
                statement.close();
            } catch (InputMismatchException e) {
                keyboard.nextLine(); // Clear the input buffer
                System.err.println(e.getMessage());
            }
        }
    }

    private static void performTransaction() throws SQLException {
        boolean continueLoop = true;

        while (continueLoop) {
            try {
                PreparedStatement statement;
                ResultSet resultSet;

                int availQty = 0, partId = 0, salesId = 0;

                while (availQty == 0) {
                    System.out.print("Enter the Part ID: ");
                    partId = keyboard.nextInt();
                    keyboard.nextLine();

                    // Check if pAvailableQuantity > 0
                    statement = connection.prepareStatement("SELECT pName, pAvailableQuantity FROM part WHERE pID = ?");
                    statement.setInt(1, partId);
                    resultSet = statement.executeQuery();
                    availQty = (resultSet.next()) ? resultSet.getInt(2) : 0;
                    if (availQty == 0) {
                        System.out.printf("Part with ID: [ %d ] is currently unavailable or not found in database.\n", partId); 
                        System.out.println("Please check if you inputted a correct Part ID.");
                        System.out.println();
                    }
                };

                boolean needReinput = true;
                while (needReinput) {
                    System.out.print("Enter the Salesperson ID: ");
                    salesId = keyboard.nextInt();
                    keyboard.nextLine();

                    // Check SID correctness
                    statement = connection.prepareStatement("SELECT sID FROM salesperson WHERE sID = ?");
                    statement.setInt(1, salesId);
                    resultSet = statement.executeQuery();
                    if (!resultSet.next()) {
                        System.out.printf("Salesperson with ID: [ %d ] is not found in database.\n", salesId); 
                        System.out.println("Please check if you inputted a correct Salesperson ID.");
                        System.out.println();
                    } else {
                        needReinput = false;
                    }
                }
                String query = 
                "UPDATE part\n"
                + "SET pAvailableQuantity = ?\n"
                + "WHERE pID = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, --availQty);
                statement.setInt(2, partId);
                statement.executeUpdate();
                
                //System.out.println("updated part");

                statement = connection.prepareStatement("SELECT MAX(tID) FROM transaction");
                resultSet = statement.executeQuery();
                
                int tId = (resultSet.next()) ? resultSet.getInt(1) : 0;


                query =
                "INSERT INTO transaction\n"
                + "values\n"
                + "(?,?,?,CURDATE())";
                statement = connection.prepareStatement(query);
                statement.setInt(1, ++tId);
                statement.setInt(2, partId);
                statement.setInt(3, salesId);
                statement.executeUpdate();

                //System.out.println("Inserted into transactions");

                query = 
                "SELECT pName, pAvailableQuantity FROM part\n"
                + "WHERE pID = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, partId);
                resultSet = statement.executeQuery();
                String partName = null;
                int checkAvailQty = 0;
                if (resultSet.next()) {
                    partName = resultSet.getString(1);
                    checkAvailQty = resultSet.getInt(2);
                }

                System.out.printf("Product: %s (id: %d) Remaining Quantity: %d\n", partName, partId, checkAvailQty);
                continueLoop = false;
                // Clean up resources
                resultSet.close();
                statement.close();
            } catch (InputMismatchException e) {
                keyboard.nextLine(); // Clear the input buffer
                System.err.println("Please input a integer ID. Let's start from *Part* ID again.");
            } 
        }
    }
}
