package services;

import domains.Concert;
import repositories.RepositoryConcert;

import java.util.List;

public class ServiceConcert {
    private RepositoryConcert repositoryConcert;

    public ServiceConcert(RepositoryConcert repositoryConcert) {
        this.repositoryConcert = repositoryConcert;
    }

    public Concert findOne(int id) {
        return repositoryConcert.findOne(id);
    }

    public List<Concert> findAll(){
        return repositoryConcert.findAll();
    }
}
