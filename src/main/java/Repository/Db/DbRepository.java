package Repository.Db;
import Domain.Entity;
import Repository.IRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public abstract class DbRepository<ID,E extends Entity<ID>> implements IRepository<ID, E> {
    protected final Connection conn;
    public DbRepository(Connection conn) {
        this.conn = conn;
    }
    public Connection getConn() {
        return conn;
    }
    @Override
    public List<E> filter(Filter filter){
        List<E> entities = new ArrayList<>();
        String where = filter.buildWhere();
        String sql = "SELECT * FROM " + getTableName();
        sql += " " + where;
        try(PreparedStatement preparedStatement = conn.prepareStatement(sql)){
            filter.applyParameters(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                E entity = mapResultSetToEntity(resultSet);
                entities.add(entity);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);}
        return entities;
    }
    @Override
    public List<E> getAll() {
        List<E> entities = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();
        try(PreparedStatement preparedStatement = conn.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                E entity = mapResultSetToEntity(resultSet);
                entities.add(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }
    @Override
    public Optional<E> remove(ID id) {
        Optional<E> entity = findById(id);
        if (entity.isEmpty()) return Optional.empty();
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> findById(ID id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<E> update(E e) {
        String sql = buildUpdateSql();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setUpdateParameters(ps, e);
            int rows = ps.executeUpdate();
            if (rows == 0)
                return Optional.empty();
            return Optional.of(e);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    @Override
    public void add(E e) {
        String sql = buildInsertSql();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setInsertParameters(ps, e);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    protected abstract String buildInsertSql();
    protected abstract void setInsertParameters(PreparedStatement ps, E e) throws SQLException;
    protected abstract String buildUpdateSql();
    protected abstract void setUpdateParameters(PreparedStatement preparedStatement, E e) throws SQLException;
    public abstract String getTableName();
    protected abstract E mapResultSetToEntity(ResultSet resultSet) throws SQLException;
}