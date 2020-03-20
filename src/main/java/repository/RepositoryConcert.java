package repository;

import domain.Concert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JDBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositoryConcert implements ICrudRepository<Integer, Concert> {
    private JDBUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RepositoryConcert() {
        this.dbUtils = new JDBUtils();
    }

    @Override
    public int size() {
        try {
            PreparedStatement ps;
            ps = dbUtils.getConnection().prepareStatement("select count(*) from concert");
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            logger.traceExit("Size of the table returned");
            return resultSet.getInt(1);
        }catch (SQLException err){
            logger.error("Error DB: " + err);
            err.printStackTrace();
        }
        logger.traceExit();
        return 0;
    }

    @Override
    public void save(Concert entity) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("insert into concert values (?, ?, ?, ?, ?, ?)")){
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getDate());
            preparedStatement.setString(4, entity.getPlace());
            preparedStatement.setInt(5, entity.getTakenSeats());
            preparedStatement.setInt(6, entity.getEmptySeats());
            int result = preparedStatement.executeUpdate();
//            con.close();
            logger.traceExit("Concert save into DB!");
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB: " + e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("delete from concert where id = ?")){
            preparedStatement.setInt(1, integer);
            int result = preparedStatement.executeUpdate();
//            con.close();
            logger.traceExit("Concert deleted!");
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB: " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Concert entity) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(
                "UPDATE concert SET id = ?, artist_name = ?, date = ?, place = ?, taken_places = ?, empty_seats = ? WHERE id = ?"
        )){
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getDate());
            preparedStatement.setString(4, entity.getPlace());
            preparedStatement.setInt(5, entity.getTakenSeats());
            preparedStatement.setInt(6, entity.getEmptySeats());
            int result = preparedStatement.executeUpdate();
            if(result == 0)
                logger.traceExit("Concert not found!");
            else
                logger.traceExit("Concert updated");
//            con.close();
        }
        catch (SQLException e){
            logger.error(e);
            System.out.println("Error DB: " + e);
        }
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
                    String date = result.getString("date");
                    String place = result.getString("place");
                    Integer taken_places = result.getInt("taken_places");
                    Integer empty_seats = result.getInt("empty_seats");
                    Concert concert = new Concert(id, name, date, place, taken_places, empty_seats);
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
    public Iterable<Concert> findAll() {
        List<Concert> all = new ArrayList<>();
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM concert")) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    String name = rs.getString("artist_name");
                    String date = rs.getString("date");
                    String place = rs.getString("place");
                    Integer taken_places = rs.getInt("taken_places");
                    Integer empty_seats = rs.getInt("empty_seats");
                    all.add(new Concert(id, name, date, place, taken_places, empty_seats));
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
