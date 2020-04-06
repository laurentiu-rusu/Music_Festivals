package objectProtocol;

public class findConcertRequest implements Request {
    private int id;
    public findConcertRequest(int idE){
        id = idE;
    }
    public int getIdExc(){
        return id;
    }
}
