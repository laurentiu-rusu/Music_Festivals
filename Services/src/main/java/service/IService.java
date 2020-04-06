package service;

import domains.User;
import domains.Concert;
import observer.applicationObserver;
import persistance.PersistanceException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IService {

    public void update(int id, String name, LocalDate date, LocalTime time, String place, int takenSeats, int emptySeats) throws PersistanceException;
    public Concert findOne(int id) throws PersistanceException;
    public List<Concert> findAll() throws PersistanceException;

    public void saveTicket (String buyerName, int numberOfSeats, int idConcert) throws PersistanceException;

    public void login(User user, applicationObserver observer) throws PersistanceException;
    public void logout(String username) throws PersistanceException;
}
