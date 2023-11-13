package SalesSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.sql.*;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Function;

public class Admin {
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
        int choice = 0;

        do {
            try {
                System.out.println("-----Operations for administrator menu-----");
                System.out.println("What kinds of operation would you like to perform?");

                System.out.println("1. Create all tables");
                System.out.println("2. Delete all tables");
                System.out.println("3. Load from datafile");
                System.out.println("4. Show content of a table");
                System.out.println("5. Return to the main menu");
                System.out.print("Enter Your Choice: ");

                // Get user's choice
                choice = keyboard.nextInt();
                keyboard.nextLine(); // Clear the input buffer

                // Routing
                switch (choice) {
                    case 1:
                        createAllTables();
                        break; // Pending verify
                    case 2:
                        deleteAllTables();
                        break; // Pending verify
                    case 3:
                        loadDataFile();
                        break; //Placeholder
                    case 4:
                        showTableContent();
                        break; //Placeholder
                    case menuItem:
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        break;

                    default:
                        throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.printf("Invalid input. Please enter a valid integer between 1 and %d.\n", menuItem);
            } catch (SQLException e) {
                System.out.println(e);
            }
        } while (choice != menuItem);
        keyboard.reset();
    }

    private static void createAllTables() throws SQLException {
        Statement statement = connection.createStatement();
        // Create table Category
        String query = 
            "CREATE TABLE IF NOT EXISTS category ("
            + "cID INT(1) NOT NULL CHECK (LENGTH(cID) = 1 AND cID > 0),"
            + "cName VARCHAR(20) NOT NULL CHECK(LENGTH(cName) > 0),"
            + "PRIMARY KEY (cID)"
            + ")";
        statement.executeUpdate(query);

        // Create table manufacturer
        query =
            "CREATE TABLE IF NOT EXISTS manufacturer ("
            + "mID INT(2) NOT NULL CHECK (mID > 0),"
            + "mName VARCHAR(20) NOT NULL CHECK(LENGTH(mName) > 0),"
            + "mAddress VARCHAR(50) NOT NULL CHECK(LENGTH(mAddress) > 0),"
            + "mPhoneNumber INT(8) NOT NULL CHECK (LENGTH(mPhoneNumber) = 8 AND mPhoneNumber > 0),"
            + "PRIMARY KEY (mID)"
            + ")";
        statement.executeUpdate(query);

        query = 
        "CREATE TABLE IF NOT EXISTS part ("
        + "pID INT(3) NOT NULL CHECK (pID > 0),"
        + "pName VARCHAR(20) NOT NULL CHECK(LENGTH(pName) > 0),"
        + "pPrice INT(5) NOT NULL CHECK (pPrice > 0),"
        + "mID INT(2) NOT NULL CHECK (mID > 0),"
        + "CID INT(1) NOT NULL CHECK (LENGTH(cID) = 1 AND cID > 0),"
        + "pWarrantyPeriod INT(2) NOT NULL CHECK(LENGTH(pWarrantyPeriod) > 0),"
        + "pAvailableQuantity INT(2) NOT NULL CHECK(pAvailableQuantity >= 0),"
        + "PRIMARY KEY (pID),"
        + "FOREIGN KEY (mID) REFERENCES manufacturer(mID),"
        + "FOREIGN KEY (cID) REFERENCES category(cID)"
        + ")";
        statement.executeUpdate(query);

        query = 
        "CREATE TABLE IF NOT EXISTS salesperson ("
        + "sID INT(2) NOT NULL CHECK (sID > 0),"
        + "sName VARCHAR(20) NOT NULL CHECK(LENGTH(sName) > 0),"
        + "sAddress VARCHAR(50) NOT NULL CHECK(LENGTH(sAddress) > 0),"
        + "sPhoneNumber INT(8) NOT NULL CHECK (LENGTH(sPhoneNumber) = 8 AND sPhoneNumber > 0),"
        + "sExperience INT(1) NOT NULL CHECK (LENGTH(sExperience) = 1),"
        + "PRIMARY KEY (sID)"
        +")";
        statement.executeUpdate(query);

        query = 
        "CREATE TABLE IF NOT EXISTS transaction ("
        + "tID INT(4) NOT NULL CHECK (tID > 0),"
        + "pID INT(3) NOT NULL CHECK (pID > 0),"
        + "sID INT(2) NOT NULL CHECK (sID > 0),"
        + "tDate DATE NOT NULL CHECK (LENGTH(tDate) > 0),"
        + "PRIMARY KEY (tID),"
        + "FOREIGN KEY (pID) REFERENCES part(pID),"
        + "FOREIGN KEY (sID) REFERENCES salesperson(sID)"
        + ")";
        statement.executeUpdate(query);

        System.out.println("Processing...Done! Database is initialized!");
    }

    private static void deleteAllTables() throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP DATABASE IF EXISTS db4");
        statement.executeUpdate("CREATE DATABASE db4");
        statement.executeUpdate("USE db4");

        /*
        String query = "DROP TABLE IF EXISTS category";
        statement.executeUpdate(query);

        query = "DROP TABLE IF EXISTS manufacturer";
        statement.executeUpdate(query);

        query = "DROP TABLE IF EXISTS part";
        statement.executeUpdate(query);

        query = "DROP TABLE IF EXISTS salesperson";
        statement.executeUpdate(query);

        query = "DROP TABLE IF EXISTS transaction";
        statement.executeUpdate(query);
        */

        System.out.println("Processing...Done! Database is removed!");
    }

    private static void loadDataFile() throws SQLException {
        System.out.print("Type in the Source Data Folder Path: ");
        String path = keyboard.nextLine();
        File f = null;
        try {
            f = new File("./" + path);

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    // Filter to find only .txt files
                    return name.endsWith(".txt");
                }
            };

            File[] files = f.listFiles(filter);
            if (files == null) {
                throw new NullPointerException();
            } else {
                // Start reading contents from files
                for (File file:files) {
                    String fileName = file.getName().split("\\.")[0];
                    //System.out.println("Insert into " + fileName);
                    Scanner fileScanner = new Scanner(file);

                    Function<String[], String> formatValues = null;

                    if (fileName.equals("transaction")) {
                        formatValues = (data) -> {
                            // Example String: '1','1','1',
                            // which is tID, pID, sID respectively
                            String exceptLastParam = "'" + String.join("','", Arrays.copyOfRange(data, 0, data.length - 1)) + "'";
                            
                            // Example String: STR_TO_DATE('31/12/2000', '%d/%m/%Y')
                            // To format Date into MySQL acceptable insert format
                            String strToDate = "STR_TO_DATE('" + data[data.length - 1] + "', '%d/%m/%Y')";

                            return String.format("%s, %s", exceptLastParam, strToDate);
                        };
                    } else {
                        formatValues = (data) -> {
                            return "'" + String.join("','", data) + "'";
                        };
                    }

                    while (fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine();
                        String[] data = line.split("\t");
                        String values = formatValues.apply(data);

                        String query = String.format("INSERT INTO %s VALUES (%s)", fileName, values);
                        //System.out.println(query);
                        
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.executeUpdate();
                        
                    }
                    fileScanner.close();
                }
            }
            System.out.println("Processing...Done! Data is inputted into the database!");
        } catch (NullPointerException | FileNotFoundException e) {
            System.err.printf("[Error]: No .txt file found in the given path: [%s]. Please check your input.\n", f.getPath());
            System.err.println("Going back to administrator menu...");
            System.err.println();
        }
    }

    private static void showTableContent() throws SQLException {
        System.err.print("Which table would you like to show: ");
        String tableName = keyboard.nextLine();
        
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName);
        ResultSet resultSet = statement.executeQuery();
        Database.printResultSet(resultSet);

    }
}
