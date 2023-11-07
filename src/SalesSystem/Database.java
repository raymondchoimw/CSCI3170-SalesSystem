package SalesSystem;

import java.sql.*;

public class Database {
    public static void  main(String[] args) {
        System.out.println("hello");
        System.out.print("["+ System.getProperty("java.class.path")+ "]\n");
        try { 
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch(ClassNotFoundException x) { 
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
                System.exit(0);
        } catch (Exception e){
                System.out.println(e);
        }
        
    }
        
}
