package Domain.User;

import java.time.LocalDate;
import java.util.Objects;

public class Person extends User{
    private String name;
    private String surname;
    private LocalDate birthdate;

    public Person(Long id, String username, String email, String passwd, String name, String surname, LocalDate birthdate) {
        super(id, username, email, passwd);
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(surname, person.surname) && Objects.equals(birthdate, person.birthdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, surname, birthdate);
    }
}
