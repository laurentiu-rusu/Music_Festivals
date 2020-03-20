package repository;

import domain.User;
import utils.JDBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositoryUser {
    private JDBUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RepositoryUser() {
        dbUtils = new JDBUtils();
    }

    public User findOne(Integer id) {
        logger.traceEntry("Cautare user cu id {} ", id);
        if (id != null) {
            try {
                PreparedStatement ps;
                ps = dbUtils.getConnection().prepareStatement("select * from user where id = ?");
                ps.setInt(1, id);
                ps.execute();
                ResultSet rs = ps.getResultSet();
                logger.traceExit("Am gasit user cu id-ul dat");
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3));
            } catch (SQLException e) {
                logger.error(e);
                e.printStackTrace();
            }
        }
        logger.traceExit("Nu am gasit user cu id-ul dat");
        return null;
    }
}
