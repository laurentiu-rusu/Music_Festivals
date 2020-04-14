package server;

import domains.Concert;
import domains.Ticket;
import domains.User;
import observer.applicationObserver;
import persistance.ICrudRepositoryConcert;
import persistance.ICrudRepositoryTicket;
import persistance.ICrudRepositoryUser;
import persistance.PersistanceException;
import service.IService;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IService {
    ICrudRepositoryConcert repositoryConcert;
    ICrudRepositoryTicket repositoryTicket;
    ICrudRepositoryUser<String> repositoryUser;
    private Map<String, applicationObserver> loggedClients;

    public Service(ICrudRepositoryConcert repositoryConcert, ICrudRepositoryTicket repositoryTicket, ICrudRepositoryUser<String> repositoryUser) {
        this.repositoryConcert = repositoryConcert;
        this.repositoryTicket = repositoryTicket;
        this.repositoryUser = repositoryUser;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public void update(int id, String name, LocalDate date, LocalTime time, String place, int takenSeats, int emptySeats) throws PersistanceException {
        Concert concert = new Concert(id, name, date, time, place, takenSeats, emptySeats);
        repositoryConcert.update(concert.getId(), concert);
        for(applicationObserver observer:loggedClients.values())
            try {
                observer.updateTrips(findAll());
            }
            catch (RemoteException exc){
                System.out.println("Error sending update " + exc);
            }
    }

    @Override
    public Concert findOne(int id) throws PersistanceException {
        return (Concert) repositoryConcert.findOne(id);
    }

    @Override
    public List<Concert> findAll() throws PersistanceException {
        return  repositoryConcert.findAll();
    }

    @Override
    public void saveTicket(String buyerName, int numberOfSeats, int idConcert) throws PersistanceException {
        int id = repositoryTicket.findAll().size() + 1;
        Ticket ticket = new Ticket(id, idConcert, buyerName, numberOfSeats);
        repositoryTicket.save(ticket);
    }

    @Override
    public synchronized void login(User user, applicationObserver applicationObserver) throws PersistanceException {
        System.out.println("user");
        if (!repositoryUser.SearchForUser(user.getUsername(), user.getPassword())) {
            throw new PersistanceException("Incorrect username or password");
        }
        loggedClients.put(user.getUsername(), applicationObserver);
    }

    public synchronized void logout(String username) throws PersistanceException {
            loggedClients.remove(username);
    }
}
