package repositories;

public interface ICrudRepositoryConcert<ID, T> {
    T findOne(ID id);
    Iterable<T> findAll();
    public void update(ID id, T entity);
}
