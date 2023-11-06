package SalesSystem;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Admin {
    final static int menuItem = 5;
    public static void menu(Scanner inputScanner) {
        // Initialize scanner to read user input
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
                choice = inputScanner.nextInt();

                // Routing
                switch (choice) {
                    case 1:
                        break; //Placeholder
                    case 2:
                        break; //Placeholder
                    case 3:
                        break; //Placeholder
                    case 4:
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
                inputScanner.nextLine(); // Clear the input buffer
            }
        } while (choice != menuItem);
        inputScanner.reset();
    }
}
