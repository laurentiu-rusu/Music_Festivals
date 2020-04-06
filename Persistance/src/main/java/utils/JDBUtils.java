
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBUtils {
    private static Connection instance;
//    private  Connection instance = null;
    private static Connection getNewConnection() {
        String jdbc = ApplicationContext.getPROPERTIES().getProperty("db.jdbc");
        Connection con = null;
        try{
            con = DriverManager.getConnection(jdbc);
        }
        catch (SQLException e)
        {
            System.out.println("Error: " + e);
        }
        return con;
    }
    public static Connection getConnection(){
        try{
            if(instance == null || instance.isClosed())
                instance = getNewConnection();
        }
        catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return instance;
    }
}
