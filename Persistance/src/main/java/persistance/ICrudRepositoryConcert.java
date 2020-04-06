package persistance;

import java.util.List;

public interface ICrudRepositoryConcert<Integer, Concert> {
    Concert findOne(int id);
    List<Concert> findAll();
    public void update(int id, Concert concert);
}
