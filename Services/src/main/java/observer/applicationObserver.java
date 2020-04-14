package observer;

import domains.Concert;
import persistance.PersistanceException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface applicationObserver extends Remote {
    public void updateTrips(List<Concert> concerts) throws PersistanceException, RemoteException;
}
