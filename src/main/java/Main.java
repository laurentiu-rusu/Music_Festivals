import domain.Ticket;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import repository.RepositoryConcert;
import repository.RepositoryTicket;
import repository.RepositoryUser;

public class Main {

    static RepositoryTicket getRepositoryTicket () {
        ApplicationContext context = new ClassPathXmlApplicationContext("FestivalApp.xml");
        RepositoryTicket ticket = context.getBean(RepositoryTicket.class);
        return ticket;
    }

    public static void main(String[] args) {
        RepositoryTicket ticket = getRepositoryTicket();

        System.out.println(ticket.findOne(1));
    }

}
