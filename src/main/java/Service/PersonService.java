package Service;

import Domain.User.Person;
import Domain.User.User;
import Repository.Db.Filter;
import Repository.IRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PersonService extends EntityService<Long, Person> {
    public PersonService(IRepository<Long, Person> repo) {
        super(repo);
    }
    //return person instead*
    public Optional<User> findByUsername(String username) {
        Filter f = new Filter();
        f.addFilter("username", username);
        return getRepo().filter(f)
                .stream()
                .map(p -> (User) p)
                .findFirst();
    }

    public List<Person> searchByUsername(String query) {
        return getRepo().getAll()
                .stream()
                .filter(p -> p.getUsername().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }
    public void add(Long id, String username, String email, String rawPassword, String name, String surname, LocalDate birthdate) {
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        Person person = new Person(id, username, email, hashedPassword, name, surname, birthdate
        );
        super.add(person);
    }

}

