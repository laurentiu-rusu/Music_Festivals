package observer;

import domains.Concert;
import persistance.PersistanceException;

import java.util.List;

public interface applicationObserver {
    public void updateTrips(List<Concert> concerts) throws PersistanceException;
}
