package domain;

public class Ticket {
    private int id;
    private int idConcert;
    private String buyerName;
    private int numberTickets;

    public Ticket(int id, int idConcert, String buyerName, int numberTickets) {
        this.id = id;
        this.idConcert = idConcert;
        this.buyerName = buyerName;
        this.numberTickets = numberTickets;
    }

    public int getId() {
        return id;
    }

    public int getIdConcert() {
        return idConcert;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public int getNumberTickets() {
        return numberTickets;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", idConcert=" + idConcert +
                ", buyerName='" + buyerName + '\'' +
                ", numberTickets=" + numberTickets +
                '}';
    }
}
