package Service;

import Domain.Entity;
import Repository.IRepository;

public class AbstractService<ID,E extends Entity<ID>> implements IService<ID,E> {
    protected final IRepository<ID, E> repo;

    public AbstractService(IRepository<ID, E> repo) {
        this.repo = repo;
    }
}
