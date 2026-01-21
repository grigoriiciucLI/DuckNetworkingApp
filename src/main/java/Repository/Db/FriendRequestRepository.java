package Repository.Db;

import Domain.Event.FriendRequest;
import Domain.Event.Status;
import Domain.User.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendRequestRepository extends DbRepository<Integer, FriendRequest>{
    private final PersonRepository personRepository;

    public FriendRequestRepository(Connection conn, PersonRepository personRepository) {
        super(conn);
        this.personRepository = personRepository;
    }

    @Override
    protected String buildInsertSql() {
        return "INSERT INTO friend_requests (id, request_from, request_to, status) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, FriendRequest fr) throws SQLException {
        ps.setInt(1, fr.getId());
        ps.setLong(2, fr.getRequestFrom().getId());
        ps.setLong(3, fr.getRequestTo().getId());
        ps.setString(4, fr.getStatus().name()); // store enum as string
    }

    @Override
    protected String buildUpdateSql() {
        return "UPDATE friend_requests SET request_from = ?, request_to = ?, status = ? WHERE id = ?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, FriendRequest fr) throws SQLException {
        ps.setLong(1, fr.getRequestFrom().getId());
        ps.setLong(2, fr.getRequestTo().getId());
        ps.setString(3, fr.getStatus().name());
        ps.setInt(4, fr.getId());
    }

    @Override
    public String getTableName() {
        return "friend_requests";
    }

    @Override
    protected FriendRequest mapResultSetToEntity(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");

        User from = personRepository.findById(rs.getLong("request_from")).orElseThrow();
        User to = personRepository.findById(rs.getLong("request_to")).orElseThrow();

        Status status = Status.valueOf(rs.getString("status"));

        return new FriendRequest(id, from, to, status);
    }
}