package repositories;

import utils.JDBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositoryUser implements ICrudRepositoryUser<String> {
    private JDBUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RepositoryUser() {
        dbUtils = new JDBUtils();
    }

    @Override
    public boolean SearchForUser(String username, String password) {
        logger.traceEntry("Search user after username and password.");
        if (username != null  && password != null ) {
            try {
                PreparedStatement ps;
                ps = dbUtils.getConnection().prepareStatement(
                        "select * from user where username = ? and password = ?");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while(rs.next()) {
                    logger.traceExit("User found!");
                    return true;
                }
            } catch (SQLException e) {
                logger.error(e);
                System.out.println("Error DB " + e);
                e.printStackTrace();
            }
        }
        logger.traceExit("User not found!");
        return false;
    }
}
