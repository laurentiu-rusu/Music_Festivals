package services;

import domains.Ticket;
import repositories.RepositoryTicket;

import java.util.List;

public class ServiceTicket {
    private RepositoryTicket repositoryTicket;

    public ServiceTicket(RepositoryTicket repositoryTicket) {
        this.repositoryTicket = repositoryTicket;
    }

    int generateID() {
        List<Ticket> tickets = repositoryTicket.findAll();
        int id = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getId() > id) {
                id = ticket.getId();
            }
        }
        return id + 1;
    }

    public void save (String buyerName, int numberOfSeats, int idConcert) {
        repositoryTicket.save(new Ticket(generateID(), idConcert, buyerName, numberOfSeats));
    }
}
