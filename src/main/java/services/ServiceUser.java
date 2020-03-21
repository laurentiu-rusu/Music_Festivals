package services;

import repositories.RepositoryUser;

public class ServiceUser {
    private RepositoryUser repositoryUser;

    public ServiceUser(RepositoryUser repositoryUser) {
        this.repositoryUser = repositoryUser;
    }

    public boolean checkLoginInfo (String username, String password) {
        return repositoryUser.SearchForUser(username, password);
    }
}
