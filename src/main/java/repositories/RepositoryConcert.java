package repositories;

import domains.Concert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JDBUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RepositoryConcert implements ICrudRepositoryConcert<Integer, Concert> {
    private JDBUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RepositoryConcert() {
        this.dbUtils = new JDBUtils();
    }

    @Override
    public Concert findOne(Integer integer) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("select * from concert where id = ?")){
            preparedStatement.setInt(1, integer);
            try(ResultSet result = preparedStatement.executeQuery()){
                if(result.next()){
                    Integer id = result.getInt("id");
                    String name = result.getString("artist_name");
                    LocalDate date = LocalDate.parse(result.getString("date"));
                    String place = result.getString("place");
                    Integer taken_places = result.getInt("taken_places");
                    Integer empty_seats = result.getInt("empty_seats");
                    LocalTime time = LocalTime.parse(result.getString("time"));
                    Concert concert = new Concert(id, name, date, time, place, taken_places, empty_seats);
                    logger.traceExit("Concert found!");
                    return concert;
                }
//                con.close();
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB: " + e);
        }
        logger.traceExit("No concert found with this id: " + integer);
        return null;
    }

    @Override
    public List<Concert> findAll() {
        List<Concert> all = new ArrayList<>();
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM concert")) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    String name = rs.getString("artist_name");
                    LocalDate date = LocalDate.parse(rs.getString("date"));
                    System.out.println("date" + date);
                    String place = rs.getString("place");
                    Integer taken_places = rs.getInt("taken_places");
                    Integer empty_seats = rs.getInt("empty_seats");
                    LocalTime time = LocalTime.parse(rs.getString("time"));
                    all.add(new Concert(id, name, date, time, place, taken_places, empty_seats));
                }
//                conn.close();
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB: " + e);
        }
        logger.traceExit(all);
        return all;
    }
}
