package objectProtocol;

import domains.Concert;

public class updateConcertRequest implements Request {
    private Concert concert;

    public updateConcertRequest(Concert e){
        concert = e;
    }

    public Concert getConcert1() {
        return concert;
    }

}
