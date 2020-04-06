package objectProtocol;

import domains.Concert;

public class findConcertResponse implements Response {
    private Concert excursie;

    public findConcertResponse(Concert exc){
        excursie = exc;
    }
    public Concert getConcert1(){
        return excursie;
    }
}
