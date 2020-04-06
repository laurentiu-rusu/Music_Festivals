package objectProtocol;

import domains.Concert;

import java.util.List;

public class getConcertsResponse implements Response {
    private List<Concert> list;

    public getConcertsResponse(List<Concert> l){
        this.list = l;
    }
    public List<Concert> getList(){
        return list;
    }
}
