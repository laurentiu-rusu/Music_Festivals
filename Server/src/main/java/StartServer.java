import persistance.RepositoryConcert;
import persistance.RepositoryTicket;
import persistance.RepositoryUser;
import server.Service;
import service.IService;
import utils.AbstractServer;
import utils.ObjectConcurrentServer;

import java.io.FileReader;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.Properties;


public class StartServer {
    private static Properties getProperty(){
        Properties serverProps=new Properties();
        try {
            serverProps.load(new FileReader("db.properties"));

            System.out.println("Properties added!");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.out.println("DB properties not found " + e);
            return null;
        }
        return serverProps;
    }

    public static void main(String[] args) {
        RepositoryConcert repositoryConcert = new RepositoryConcert();
        RepositoryTicket repositoryTicket = new RepositoryTicket();
        RepositoryUser repositoryUser = new RepositoryUser();
        IService service= new Service(repositoryConcert, repositoryTicket, repositoryUser);
        AbstractServer server = new ObjectConcurrentServer(55555, service);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }

}
