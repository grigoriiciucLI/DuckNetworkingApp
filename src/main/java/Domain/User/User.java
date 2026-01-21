package Domain.User;

import Domain.Entity;

import java.util.Objects;

public abstract class User extends Entity<Long> {
    private String username;
    private String email;
    private String passwd;

    public User(Long id, String username, String email, String passwd) {
        super(id);
        this.username = username;
        this.email = email;
        this.passwd = passwd;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswd() {
        return passwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, email);
    }
}
