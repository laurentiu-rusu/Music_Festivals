package objectProtocol;

import domains.User;

public class loginRequest implements Request {
    private User user;
    public loginRequest(User u){
        user = u;
    }
    public User getUser(){
        return user;
    }
}
