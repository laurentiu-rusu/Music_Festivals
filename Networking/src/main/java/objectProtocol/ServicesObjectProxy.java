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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesObjectProxy implements IService {
    private String host;
    private Integer port;

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;

    private applicationObserver client;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesObjectProxy(String host, int p){
        this.host = host;
        port = p;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public void login(User user, applicationObserver applicationObserver) throws PersistanceException {
        initializeConnection();
        sendRequest(new loginRequest(user));
        Response response = readResponse();
        if(response instanceof OkResponse){
            System.out.println("Ok");
            this.client = applicationObserver;
        }
        if(response instanceof ErrorResponse){
            ErrorResponse errorResponse = (ErrorResponse) response;
            throw new PersistanceException(errorResponse.getError());
        }
    }

    @Override
    public void logout(String username) throws PersistanceException {
        if(connection != null){
            sendRequest(new logoutRequest(username));
            System.out.println("Logout succes");
            Response response=readResponse();
            closeConnection();

        }
    }


    @Override
    public Concert findOne(int id) throws PersistanceException {
        sendRequest(new findConcertRequest(id));
        Response response = readResponse();
        if(response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new PersistanceException(err.getError());
        }
        findConcertResponse response1 = (findConcertResponse) response;
        return response1.getConcert1();
    }

    @Override
    public List<Concert> findAll() throws PersistanceException {
        sendRequest(new getConcertsRequest());
        Response response = readResponse();
        if(response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new PersistanceException(err.getError());
        }
        getConcertsResponse response1 = (getConcertsResponse) response;
        return response1.getList();
    }

    @Override
    public void update(int id, String name, LocalDate date, LocalTime time, String place, int takenSeats, int emptySeats) throws PersistanceException {
        Concert concert = new Concert(id, name, date, time, place, takenSeats, emptySeats);
        sendRequest(new updateConcertRequest(concert));
        Response response = readResponse();
        if(response instanceof OkResponse){
            System.out.println("Updated");
        }
        else {
            ErrorResponse response1 = (ErrorResponse) response;
            System.out.println("Couldn't been updated" + response1.getError());
        }
    }


    private void handleUpdate(Response response){
        if(response instanceof updateGetAllConcerts){
            updateGetAllConcerts response1 = (updateGetAllConcerts) response;
            List<Concert> concerts = response1.getList();
            try{
                client.updateTrips(concerts);

            }
            catch (PersistanceException exc){
                exc.printStackTrace();
            }
        }
    }
    @Override
    public void saveTicket(String buyerName, int numberOfSeats, int idConcert) throws PersistanceException {
        Ticket ticket = new Ticket(0, idConcert, buyerName, numberOfSeats);
        sendRequest(new addTicketRequest(ticket));
        Response response = readResponse();
        if(response instanceof OkResponse){
            System.out.println("Added");
        }
        else {
            ErrorResponse response1 = (ErrorResponse) response;
            System.out.println("Couldn't been added" + response1.getError());
        }
    }

    private void initializeConnection() throws PersistanceException {
        try {
            connection = new Socket(host,port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sendRequest(Request request) throws PersistanceException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new PersistanceException("Error sending object"+e);
        }

    }
    private Response readResponse() throws PersistanceException {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("Response Received ");
                    if (response instanceof updateGetAllConcerts){
                        handleUpdate((updateGetAllConcerts)response);
                    }
                    else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
