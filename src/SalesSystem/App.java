package SalesSystem;

import java.util.InputMismatchException;
import java.util.Scanner;

import java.sql.*;


public class App {
    final static int menuItem = 4;
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to sales system!");
        System.out.println();

        Connection connection = Database.connection();

        // Initialize scanner to read user input
        Scanner inputScanner = new Scanner(System.in);
        int choice = 0;

        do {
            try{
                System.out.println("-----Main Menu-----");
                System.out.println("What kinds of operation would you like to perform?");
                System.out.println("1. Operations for administrator");
                System.out.println("2. Operations for salesperson");
                System.out.println("3. Operations for manager");
                System.out.println("4. Exit this program ");
                System.out.print("Enter Your Choice: ");

                // Get user's choice
                choice = inputScanner.nextInt();

                // Routing
                switch (choice) {
                    case 1:
                        System.out.println();
                        Admin.menu(inputScanner); // Pass the Scanner to subroutine
                        break;
                    case 2:
                        System.out.println();
                        Sales.menu(inputScanner); // Pass the Scanner to subroutine
                        break;
                    case 3:
                        System.out.println();
                        Manager.menu(inputScanner); // Pass the Scanner to subroutine
                        break;
                    case 4:
                        break;

                    default:
                    throw new InputMismatchException();
                }

            } catch (InputMismatchException e) {
                System.out.printf("Invalid input. Please enter a valid integer between 1 and %d.\n", menuItem);
                inputScanner.nextLine(); // Clear the input buffer
            }
        } while (choice != menuItem);
        inputScanner.close();
    }
}
