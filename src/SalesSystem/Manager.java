package SalesSystem;

import java.sql.Connection;
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

                System.out.println("1. List all salesperson");
                System.out.println("2. Count the no. of sales record of each salesperson under a specific range on years of experience");
                System.out.println("3. Show the total sales value of each manufacturer");
                System.out.println("4. Show the N most popular part");
                System.out.println("5. Return to the main menu");
                System.out.print("Enter Your Choice: ");

                // Get user's choice
                choice = keyboard.nextInt();
                
                // Routing
                switch (choice) {
                    case menuItem:
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        break;

                    default:
                        throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.printf("Invalid input. Please enter a valid integer between 1 and %d, inclusive.\n", menuItem);
                keyboard.nextLine(); // Clear the input buffer
            }
        } while (choice != menuItem);
        keyboard.reset();
    }
}
