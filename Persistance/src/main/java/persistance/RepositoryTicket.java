package persistance;

import domains.Ticket;
import utils.JDBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositoryTicket implements ICrudRepositoryTicket<Integer, Ticket> {
    public RepositoryTicket() {}

    @Override
    public void save(Ticket entity) {
        Connection con = JDBUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("insert into ticket values (?, ?, ?, ?)")){
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setInt(2, entity.getIdConcert());
            preparedStatement.setString(3, entity.getBuyerName());
            preparedStatement.setInt(4, entity.getNumberTickets());
            int result = preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
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
    public Ticket findOne(int integer) {
        Connection con = JDBUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("select * from ticket where id = ?")){
            preparedStatement.setInt(1, integer);
            try(ResultSet result = preparedStatement.executeQuery()){
                if(result.next()){
                    Integer id = result.getInt("id");
                    Integer idConcert = result.getInt("id_concert");
                    String buyerName = result.getString("buyer_name");
                    Integer number_seats = result.getInt("number_seats");
                    Ticket ticket = new Ticket(id, idConcert, buyerName, number_seats);
                    return ticket;
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Error DB: " + e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<Ticket> findAll() {
        List<Ticket> all = new ArrayList<>();
        Connection conn = JDBUtils.getConnection();
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
            }
        }
        catch (SQLException e) {
            System.out.println("Error DB: " + e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return all;
    }
}
