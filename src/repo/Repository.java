package repo;

import model.Media;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Repository<T extends Media> {

    void add(T media);

    boolean remove(int id);

    Optional<T> findById(int id);

    List<T> findAll();

    List<T> findBy(Predicate<? super T> predicate);

    int count();

    boolean exists(int id);

    void clear();
}

