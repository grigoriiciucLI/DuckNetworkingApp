package Service;

import Domain.Entity;
import Repository.IRepository;

import java.util.List;

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
}
