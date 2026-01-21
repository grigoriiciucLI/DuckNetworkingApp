package Repository.Db;
import Domain.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class PaginatedDbRepository<ID, E extends Entity<ID>> extends DbRepository<ID, E> {
    public PaginatedDbRepository(Connection conn) {
        super(conn);
    }

    public List<E> getPage(int limit, int offset) {
        List<E> entities = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName() + " ORDER BY id LIMIT ? OFFSET ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }
    public List<E> filter(Filter filter, int limit, int offset) {
        List<E> entities = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName() + " " + filter.buildWhere() + " ORDER BY id LIMIT ? OFFSET ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            filter.applyParameters(ps);
            ps.setInt(filter.size() + 1, limit);  // after filter params
            ps.setInt(filter.size() + 2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }

}
