package repository;

import domain.Concert;
import domain.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import utils.JDBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositoryTicket implements ICrudRepositoryTicket<Integer, Ticket> {
    private JDBUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RepositoryTicket() {
        this.dbUtils = new JDBUtils();
    }

    @Override
    public void save(Ticket entity) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("insert into ticket values (?, ?, ?, ?)")){
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setInt(2, entity.getIdConcert());
            preparedStatement.setString(3, entity.getBuyerName());
            preparedStatement.setInt(4, entity.getNumberTickets());
            int result = preparedStatement.executeUpdate();
//            con.close();
            logger.traceExit("Ticket save into DB!");
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB: " + e);
        }
        logger.traceExit();
    }

    @Override
    public Ticket findOne(Integer integer) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("select * from ticket where id = ?")){
            preparedStatement.setInt(1, integer);
            try(ResultSet result = preparedStatement.executeQuery()){
                if(result.next()){
                    Integer id = result.getInt("id");
                    Integer idConcert = result.getInt("id_concert");
                    String buyerName = result.getString("buyer_name");
                    Integer number_seats = result.getInt("number_seats");
                    Ticket ticket = new Ticket(id, idConcert, buyerName, number_seats);
                    logger.traceExit("Ticket found!");
                    return ticket;
                }
//                con.close();
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB: " + e);
        }
        logger.traceExit("No ticket found with this id: " + integer);
        return null;
    }

    @Override
    public Iterable<Ticket> findAll() {
        List<Ticket> all = new ArrayList<>();
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM ticket")) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    Integer idConcert = rs.getInt("id_concert");
                    String buyerName = rs.getString("buyer_name");
                    Integer number_seats = rs.getInt("number_seats");
                    Ticket ticket = new Ticket(id, idConcert, buyerName, number_seats);
                    all.add(ticket);
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
