package SalesSystem;

import java.sql.*;

public class Database {
    
    private static final String dbAddress = "jdbc:mysql://localhost:3306/db4";//"jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db60?autoReconnect=true&useSSL=false";
    private static final String dbUsername = "root"; //"Group4";
    private static final String dbPassword = "CSCI3170";
    
    public static Connection connection() {
        //System.out.print("["+ System.getProperty("java.class.path")+ "]\n");

        Connection con = null;
        try { 
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
            System.out.println("Connect to [" + dbAddress +"] successfully!");
        } catch(ClassNotFoundException x) { 
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } catch (SQLException e){
            System.out.println(e);
        }

        return con;
    }
        
}
