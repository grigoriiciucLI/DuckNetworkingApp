package Service;

import Domain.Entity;
import Repository.IRepository;

import java.util.List;
import java.util.Optional;

public abstract class EntityService<ID,E extends Entity<ID>> implements IService {
    protected final IRepository<ID, E> repo;
    public EntityService(IRepository<ID, E> repo) {
        this.repo = repo;
    }
    public IRepository<ID, E> getRepo() {
        return repo;
    }

    public List<E> getAll() {
        return repo.getAll();
    }
    public void add(E e){
        repo.add(e);
    }

    Optional<E> remove(ID id){
        return repo.remove(id);
    }
}
