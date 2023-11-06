package SalesSystem;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Manager {
    final static int menuItem = 5;
    public static void menu(Scanner inputScanner) {
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
                choice = inputScanner.nextInt();
                
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
                System.out.printf("Invalid input. Please enter a valid integer between 1 and %d.\n", menuItem);
                inputScanner.nextLine(); // Clear the input buffer
            }
        } while (choice != menuItem);
        inputScanner.reset();
    }
}
