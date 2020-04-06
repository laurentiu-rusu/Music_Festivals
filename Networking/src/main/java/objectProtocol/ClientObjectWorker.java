package objectProtocol;

import domains.Concert;
import domains.Ticket;
import domains.User;
import observer.applicationObserver;
import persistance.PersistanceException;
import service.IService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ClientObjectWorker implements Runnable, applicationObserver {
    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientObjectWorker(IService serv, Socket con) {
        server = serv;
        connection = con;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try{
                Object request = input.readObject();
                Object response = handleRequest((Request) request);
                if(response != null){
                    sendResponse((Response) response);
                }
            }catch (IOException | ClassNotFoundException | PersistanceException exc){
                exc.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        try{
            input.close();
            output.close();
            connection.close();
        } catch (IOException exc){
            System.out.println("Error " + exc);
        }
    }

    @Override
    public void updateTrips(List<Concert> arrayList) throws PersistanceException {
        try {
            sendResponse(new updateGetAllConcerts(arrayList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response handleRequest(Request request) throws PersistanceException {
        Response response = null;
        if(request instanceof loginRequest){
            System.out.println("Login request");
            loginRequest request1 = (loginRequest) request;
            User user = request1.getUser();
            try{
                server.login(user,this);
                return new OkResponse();
            } catch (PersistanceException e){
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof logoutRequest){
            System.out.println("Logout request");
            logoutRequest request1 = (logoutRequest) request;
            try {
                server.logout(request1.getUsername());
                connected=false;
                return new OkResponse();

            } catch (PersistanceException e) {

            }
        }
        if(request instanceof getConcertsRequest){
            System.out.println("Get trips request");
            try{
                List<Concert> list = server.findAll();
                return new getConcertsResponse(list);
            }
            catch (PersistanceException exc){
                return new ErrorResponse(exc.getMessage());
            }
        }
        if(request instanceof findConcertRequest){
            try{
                Concert exc = server.findOne(((findConcertRequest) request).getIdExc());
                return new findConcertResponse(exc);
            }
            catch (PersistanceException exc){
                return new ErrorResponse(exc.getMessage());
            }
        }
        if(request instanceof updateConcertRequest)
        {
            try{
                updateConcertRequest request1 = (updateConcertRequest) request;
                Concert concert = request1.getConcert1();
                int id = concert.getId();
                String name = concert.getName();
                LocalDate date = concert.getDate();
                LocalTime time =concert.getTime();
                String place = concert.getPlace();
                int takenSeats = concert.getTakenSeats();
                int emptySeats = concert.getEmptySeats();
                server.update(id, name, date, time, place, takenSeats, emptySeats);
                return new OkResponse();
            }
            catch (PersistanceException exc){
                return new ErrorResponse(exc.getMessage());
            }
        }
        if(request instanceof addTicketRequest){
            try{
                Ticket ticket = ((addTicketRequest) request).getRezervare();
                String buyerName = ticket.getBuyerName();
                int numberOfSeats = ticket.getNumberTickets();
                int idConcert = ticket.getIdConcert();
                server.saveTicket(buyerName, numberOfSeats, idConcert);
                return new OkResponse();
            }
            catch (PersistanceException exc){
                return new ErrorResponse(exc.getMessage());
            }
        }
        return null;
    }
    private void sendResponse(Response response) throws IOException{
        System.out.println("Sending Response");
        output.writeObject(response);
        output.flush();
    }
}
