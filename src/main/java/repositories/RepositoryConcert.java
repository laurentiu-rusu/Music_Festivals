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
    private static final Logger logger = LogManager.getLogger();

    public RepositoryConcert() { }

    @Override
    public void update(Integer integer, Concert entity) {
        Connection con = JDBUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(
                "UPDATE concert SET id = ?, artist_name = ?, date = ?, place = ?, taken_places = ?," +
                        "empty_seats = ?, time = ? WHERE id = ?"
        )) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getDate().toString());
            preparedStatement.setString(4, entity.getPlace());
            preparedStatement.setInt(5, entity.getTakenSeats());
            preparedStatement.setInt(6, entity.getEmptySeats());
            preparedStatement.setString(7, entity.getTime().toString());
            preparedStatement.setInt(8, entity.getId());
            int result = preparedStatement.executeUpdate();
            System.out.println();
            if(result == 0)
                logger.traceExit("Concert not found!");
            else
                logger.traceExit("Concert updated!");
        }
        catch (SQLException e){
            logger.error(e);
            System.out.println("Error DB: " + e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Concert findOne(Integer integer) {
        Connection con = JDBUtils.getConnection();
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
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB: " + e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.traceExit("No concert found with this id: " + integer);
        return null;
    }

    @Override
    public List<Concert> findAll() {
        List<Concert> all = new ArrayList<>();
        Connection conn = JDBUtils.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM concert")) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    String name = rs.getString("artist_name");
                    LocalDate date = LocalDate.parse(rs.getString("date"));
                    String place = rs.getString("place");
                    Integer taken_places = rs.getInt("taken_places");
                    Integer empty_seats = rs.getInt("empty_seats");
                    LocalTime time = LocalTime.parse(rs.getString("time"));
                    all.add(new Concert(id, name, date, time, place, taken_places, empty_seats));
                }
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB: " + e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.traceExit(all);
        return all;
    }
}
