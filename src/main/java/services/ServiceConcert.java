package services;

import domains.Concert;
import repositories.RepositoryConcert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ServiceConcert {
    private RepositoryConcert repositoryConcert;

    public ServiceConcert(RepositoryConcert repositoryConcert) {
        this.repositoryConcert = repositoryConcert;
    }

    public void update(int id, String name, LocalDate date, LocalTime time, String place, int takenSeats, int emptySeats) {
        repositoryConcert.update(id, new Concert(id, name, date, time, place, takenSeats, emptySeats));
    }

    public Concert findOne(int id) {
        return repositoryConcert.findOne(id);
    }

    public List<Concert> findAll(){
        return repositoryConcert.findAll();
    }
}
