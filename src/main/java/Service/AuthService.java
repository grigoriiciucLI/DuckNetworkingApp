package Service;

import Domain.User.Person;

public class AuthService {
    private final PersonService personService;
    private final FriendshipService friendshipService;
    private Person currentPerson;

    public AuthService(PersonService personService, FriendshipService friendshipService) {
        this.personService = personService;
        this.friendshipService = friendshipService;
    }

    public Person login(String username, String rawPassword) {
        Person user = personService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        if (!org.mindrot.jbcrypt.BCrypt.checkpw(rawPassword, user.getPasswd())) {
            throw new RuntimeException("Wrong password");
        }
        currentPerson = user;
        return user;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void logout() {
        currentPerson = null;
    }

    public boolean isAuthenticated() {
        return currentPerson != null;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public FriendshipService getFriendshipService() {
        return friendshipService;
    }
}
