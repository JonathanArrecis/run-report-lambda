package helloworld.repository;

import software.amazon.lambda.powertools.tracing.Tracing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.List;

public class DynamicQueryExecutor {
    /**
     * Execute one query dynamically with params in format ${paramName}
     * @param conn  The connection to the database
     * @param originalQuery The query with params in format ${paramName}
     * @param params The map of params
     * @throws SQLException If the query fails
     */
    @Tracing
    public <T> T executeQuery(Connection conn, String originalQuery, Map<String,Object> params,RowMapper<T> rowMapper) throws SQLException {
        QueryProcessor qp = new QueryProcessor(originalQuery);
        String sql = qp.getProcessedQuery();
        List<String> orderedParams = qp.getOrderedParameters();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParamValues(orderedParams, ps, params);
            try(ResultSet rs = ps.executeQuery()){
                return rowMapper.mapRow(rs);
            }
        }
    }
    @Tracing
    private void setParamValues(List<String> orderedParams, PreparedStatement ps, Map<String, Object> params) throws SQLException {
        for (int i = 0; i < orderedParams.size(); i++) {
                String paramName = orderedParams.get(i);
                Object paramValue = params.get(paramName);

                if (paramValue == null) {
                    ps.setObject(i + 1, null);
                } else if (paramValue instanceof String) {
                    ps.setString(i + 1, (String) paramValue);
                } else if (paramValue instanceof Integer) {
                    ps.setInt(i + 1, (Integer) paramValue);
                } else if (paramValue instanceof Long) {
                    ps.setLong(i + 1, (Long) paramValue);
                } else if (paramValue instanceof LocalDate) {
                    java.sql.Date sqldate = java.sql.Date.valueOf((LocalDate) paramValue);
                    ps.setDate(i + 1,sqldate);
                } else if (paramValue instanceof java.util.Date) {
                    ps.setTimestamp(i + 1, new java.sql.Timestamp(((java.util.Date) paramValue).getTime()));
                }
                else {
                    ps.setObject(i + 1, paramValue);

                }
        }
    }
}
