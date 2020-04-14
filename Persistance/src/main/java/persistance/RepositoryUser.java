package persistance;

import utils.JDBUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositoryUser implements ICrudRepositoryUser<String> {
    public RepositoryUser() {}

    @Override
    public boolean SearchForUser(String username, String password) {
        if (username != null  && password != null ) {
            try {
                PreparedStatement ps;
                ps = JDBUtils.getConnection().prepareStatement(
                        "select * from user where username = ? and password = ?");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while(rs.next()) {
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("Error DB " + e);
                e.printStackTrace();
            }
        }
        return false;
    }
}
