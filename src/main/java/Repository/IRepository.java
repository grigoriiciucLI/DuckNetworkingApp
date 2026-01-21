package Repository;

import Domain.Entity;
import Repository.Db.Filter;

import java.util.List;
import java.util.Optional;

public interface IRepository<ID, E extends Entity<ID>> {
    void add(E e);
    Optional<E> remove(ID id);
    Optional<E> findById(ID id);
    List<E> getAll();
    Optional<E> update(E e);
    List<E> filter(Filter f);
}
