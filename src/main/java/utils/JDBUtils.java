package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBUtils {
    private  Connection instance=null;
    private Connection getNewConnection() {
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
    public Connection getConnection(){
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
