package Domain;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Filter {
    private final Map<String, Object> filters = new HashMap<>();

    public void addFilter(String column, Object value) {
        if (column != null && value != null)
            filters.put(column, value);
    }

    public String buildWhere() {
        if (filters.isEmpty()) {
            return "";
        }
        List<String> conditions = new ArrayList<>();
        for (String column : filters.keySet()) {
            conditions.add(column + " = ? ");
        }
        return "WHERE " + String.join(" AND ", conditions);
    }

    public void applyParameters(PreparedStatement stmt) throws SQLException {
        int i = 1;
        for (Object value : filters.values()) {
            stmt.setObject(i++, value);
        }
    }
}
