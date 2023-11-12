package SalesSystem;

import java.sql.*;

public class Database {
    
    private static final String dbAddress = "jdbc:mysql://localhost:3306/db4";//"jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db60?autoReconnect=true&useSSL=false";
    private static final String dbUsername = "root"; //"Group4";
    private static final String dbPassword = "CSCI3170";
    
    // WIP: print result set in specific format
    public static void printResultSet(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();

        // Print column names
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(resultSet.getMetaData().getColumnName(i) + "\t");
        }
        System.out.println();

        // Print rows
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + "\t");
            }
            System.out.println();
        }
    }
    
    
    // Check if table exists in a database
    public static boolean existTable(Connection connection, String tableName){
        
        boolean exist = false;
        try {
            PreparedStatement statement = connection.prepareStatement("SHOW TABLES LIKE ?");
            statement.setString(1, tableName);
            ResultSet resultSet = statement.executeQuery();

            exist = resultSet.next();
            System.out.println("exist: " + exist);

            // Clean up resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        

        return exist;
    }

    public static Connection connection() {
        //System.out.print("["+ System.getProperty("java.class.path")+ "]\n");

        Connection con = null;
        try { 
            // Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Connect to database
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
