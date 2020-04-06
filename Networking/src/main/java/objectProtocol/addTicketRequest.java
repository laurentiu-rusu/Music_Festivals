package objectProtocol;

import domains.Ticket;

public class addTicketRequest implements Request {
    private Ticket ticket;

    public addTicketRequest(Ticket rezervare){
        this.ticket = rezervare;
    }
    public Ticket getRezervare(){
        return ticket;
    }
}
