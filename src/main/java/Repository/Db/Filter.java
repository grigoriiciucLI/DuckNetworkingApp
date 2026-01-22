package Repository.Db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Filter {
    private final Map<String, Object> filters = new LinkedHashMap<>();

    public void addFilter(String column, Object value) {
        if (column != null && value != null)
            filters.put(column+" = ?", value);
    }
    public void likeIgnoreCase(String column, String value) {
        if (value != null && !value.isBlank())
            filters.put("LOWER(" + column + ") LIKE ?", "%" + value.toLowerCase() + "%");
    }

    public String buildWhere() {
        if (filters.isEmpty()) {
            return "";
        }
        return "WHERE " + String.join(" AND ", filters.keySet());
    }

    public void applyParameters(PreparedStatement stmt) throws SQLException {
        int i = 1;
        for (Object value : filters.values()) {
            stmt.setObject(i++, value);
        }
    }

    public int size() {
        return filters.size();
    }

}
