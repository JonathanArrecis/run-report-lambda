package helloworld.repository;

import helloworld.config.DatabaseConfig;
import helloworld.data.GenericResultsetData;
import helloworld.data.ResultsetColumnHeaderData;
import helloworld.data.ResultsetRowData;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import software.amazon.lambda.powertools.tracing.Tracing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportRepository {

    private final JdbcTemplate jdbcTemplate = DatabaseConfig.getJdbcTemplate();

    @Tracing
    public GenericResultsetData fillGenericResultSetData(String sql, MapSqlParameterSource namedParameters){

        try{
            final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            final SqlParameterSource parameterSource = namedParameters != null ?namedParameters : new MapSqlParameterSource();

            final SqlRowSet rs = namedParameterJdbcTemplate.queryForRowSet(sql, parameterSource);
            final List<ResultsetColumnHeaderData> headers = new ArrayList<>();
            final List<ResultsetRowData> resultsetRowData = new ArrayList<>();
            final SqlRowSetMetaData metaData = rs.getMetaData();


            for(int i =0; i < metaData.getColumnCount(); i++){
                final String columnName = metaData.getColumnName(i+1);
                final String columnType = metaData.getColumnTypeName(i+1);
                final ResultsetColumnHeaderData columnHeaderData = ResultsetColumnHeaderData.basic(columnName,columnType);
                headers.add(columnHeaderData);
            }

            while(rs.next()){
                final List<String> columnValues = new ArrayList<>();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    final String columnName = metaData.getColumnName(i+1);
                    final String columnValue = rs.getString(columnName);
                    columnValues.add(columnValue);
                }
                final ResultsetRowData rowData = ResultsetRowData.create(columnValues);
                resultsetRowData.add(rowData);
            }
            return new GenericResultsetData(headers,resultsetRowData);
        }catch (DataAccessException e){
            throw new RuntimeException("Error executing sql: " + sql, e);
        }
    }



    @Tracing
    public String getSqlToRun(final String nameReport, final Map<String, String> queryParams, MapSqlParameterSource namedParameters) {
        System.out.println("Getting sql for report: " + nameReport);
        String sql = getSql(nameReport);
        final Set<String> keys = queryParams.keySet();
        for(String key : keys){
            System.out.println("Replacing key: " + key + " with value: " + queryParams.get(key));
            final String value = queryParams.get(key);
            if(sql.contains(key)){
                final String keysasNamedParameter = key.replace("${",":").replace("}","");
                sql = this.replace(sql,key,keysasNamedParameter);
                namedParameters.addValue(keysasNamedParameter.replace(":",""),value);
            }
        }
        sql = this.wrapSql(sql);
        return sql;
    }

    @Tracing
    private String getSql(final String nameReport){

        final String inputSql = "select report_sql as the_sql from stretchy_report sr where sr.report_name = ? ";
        final String inputSqlWrapped = wrapSql(inputSql);

        final SqlRowSet rs = jdbcTemplate.queryForRowSet(inputSqlWrapped, nameReport);

        if(rs.next() && rs.getString("the_sql") != null){
            return rs.getString("the_sql");
        }
        throw new RuntimeException("No sql found for report " + nameReport);

    }

    @Tracing
    private String replace(final String str, final String pattern, final String replace) {
        // JPW - this replace may / may not be any better or quicker than the
        // apache stringutils equivalent. It works, but if someone shows the
        // apache one to be about the same then this can be removed.
        int s = 0;
        int e = 0;
        final StringBuilder result = new StringBuilder();

        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    @Tracing
    private String wrapSql(final String sql){
        return "select * from (" + sql + ") as x";
    }
}
