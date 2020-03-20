package repository;

public interface ICrudRepositoryUser<ID, T> {
    T findOne(ID id);
}
