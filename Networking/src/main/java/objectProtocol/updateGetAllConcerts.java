package objectProtocol;

import domains.Concert;

import java.util.List;

public class updateGetAllConcerts implements Response {
    private List<Concert> list;
    public updateGetAllConcerts(List<Concert> l){
        list = l;
    }
    public List<Concert> getList(){
        return list;
    }
}
