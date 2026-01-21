package Service;

import Domain.User.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//e ideal sa lucreze cu User, nu cu Person
//nu trebuie sa stie detalii personale
public class AuthService {
    private final PersonService personService;
    private User currentUser;

    public AuthService(PersonService personService) {
        this.personService = personService;
    }

    public User login(String username, String rawPassword) {
        User user = personService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!org.mindrot.jbcrypt.BCrypt.checkpw(rawPassword, user.getPasswd())) {
            throw new RuntimeException("Wrong password");
        }
        currentUser = user;
        return user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public PersonService getPersonService() {
        return personService;
    }
}
