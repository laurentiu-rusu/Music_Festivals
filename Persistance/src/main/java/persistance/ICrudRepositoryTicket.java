package persistance;

import java.util.List;

public interface ICrudRepositoryTicket<Integer, Ticket> {
    void save(Ticket ticket);
    Ticket findOne(int id);
    List<Ticket> findAll();
}
