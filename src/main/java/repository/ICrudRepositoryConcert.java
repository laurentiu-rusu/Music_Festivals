package repository;

public interface ICrudRepositoryConcert<ID, T> {
    T findOne(ID id);
    Iterable<T> findAll();
}
