package repositories;

public interface ICrudRepositoryTicket<ID, T> {
    void save(T entity);
    T findOne(ID id);
    Iterable<T> findAll();
}
