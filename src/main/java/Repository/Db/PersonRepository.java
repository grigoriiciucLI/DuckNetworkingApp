package Repository.Db;

import Domain.User.Person;

import java.sql.*;

public class PersonRepository extends DbRepository<Long, Person >{
    public PersonRepository(Connection conn) {
        super(conn);
    }

    @Override
    protected String buildInsertSql() {
        return "INSERT INTO users " +
                "(id, username, email, password, name, surname, birthdate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Person p) throws SQLException {
        ps.setLong(1, p.getId());
        ps.setString(2, p.getUsername());
        ps.setString(3, p.getEmail());
        ps.setString(4, p.getPasswd());
        ps.setString(5, p.getName());
        ps.setString(6, p.getSurname());
        ps.setDate(7, Date.valueOf(p.getBirthdate()));
    }


    @Override
    protected String buildUpdateSql() {
        return "UPDATE users SET username = ?, email = ?, password = ?, name = ?, surname = ?, birthdate = ? WHERE id = ?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Person p) throws SQLException {
        ps.setString(1, p.getUsername());
        ps.setString(2, p.getEmail());
        ps.setString(3, p.getPasswd());
        ps.setString(4, p.getName());
        ps.setString(5, p.getSurname());
        ps.setDate(6, Date.valueOf(p.getBirthdate()));
        ps.setLong(7, p.getId());
    }

    @Override
    public String getTableName() {
        return "users";
    }

    @Override
    protected Person mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Person(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getDate("birthdate").toLocalDate()
        );
    }

}
