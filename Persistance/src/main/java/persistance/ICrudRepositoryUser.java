package persistance;

public interface ICrudRepositoryUser<String> {
    boolean SearchForUser(String username, String password);
}
