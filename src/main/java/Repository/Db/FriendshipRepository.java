package Repository.Db;

import Domain.User.Friendship;
import Domain.User.Person;
import Domain.User.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipRepository extends DbRepository<Integer,Friendship> {
    private final PersonRepository personRepository;
    public FriendshipRepository(Connection conn, PersonRepository personRepository) {
        super(conn);
        this.personRepository = personRepository;
    }

    @Override
    protected String buildInsertSql() {
        return "INSERT INTO friendships (id, user1_id, user2_id) VALUES (?, ?, ?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Friendship friendship) throws SQLException {
        ps.setInt(1, friendship.getId());
        ps.setLong(2, friendship.getU1().getId());
        ps.setLong(3, friendship.getU2().getId());
    }

    @Override
    protected String buildUpdateSql() {
        return "UPDATE friendships SET user1_id = ?, user2_id = ? WHERE id = ?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Friendship friendship) throws SQLException {
        ps.setLong(1, friendship.getU1().getId());
        ps.setLong(2, friendship.getU2().getId());
        ps.setInt(3, friendship.getId());
    }

    @Override
    public String getTableName() {
        return "friendships";
    }

    @Override
    protected Friendship mapResultSetToEntity(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        User u1 = personRepository.findById(rs.getLong("user1_id")).orElseThrow();
        User u2 = personRepository.findById(rs.getLong("user2_id")).orElseThrow();
        return new Friendship(id, u1, u2);
    }
}

