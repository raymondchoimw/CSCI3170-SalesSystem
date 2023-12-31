package SalesSystem;

import java.util.InputMismatchException;
import java.util.Scanner;

import java.sql.*;


public class App {
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    final static int menuItem = 4;
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to sales system!");
        System.out.println();

        Connection connection = Database.connection();

        // Initialize scanner to read user input
        Scanner keyboard = new Scanner(System.in);
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
                choice = keyboard.nextInt();
                keyboard.nextLine(); // Clear the input buffer
                //App.clearScreen();
                System.out.println();
                // Routing
                switch (choice) {
                    case 1:
                        Admin.init(connection, keyboard); // Pass the Scanner to subroutine
                        break;
                    case 2:
                        Sales.init(connection, keyboard); // Pass the Scanner to subroutine
                        break;
                    case 3:
                        Manager.init(connection, keyboard); // Pass the Scanner to subroutine
                        break;
                    case 4:
                        break;

                    default:
                    throw new IllegalArgumentException();
                }

            } catch (InputMismatchException e) {
                System.err.printf("[Invalid input]: Please enter a valid integer between 1 and %d, inclusive.\n", menuItem);
                keyboard.nextLine(); // Clear the input buffer
                App.clearScreen();
            } catch (IllegalArgumentException e) {
                System.err.printf("[Invalid input]: Please enter a valid integer between 1 and %d, inclusive.\n", menuItem);
                App.clearScreen();
            }
        } while (choice != menuItem);
        keyboard.close();
    }
}
