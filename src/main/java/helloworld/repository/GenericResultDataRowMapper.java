package helloworld.repository;

import helloworld.model.GenericResultsetData;
import helloworld.model.ResultsetColumnHeaderData;
import helloworld.model.ResultsetRowData;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class GenericResultDataRowMapper implements RowMapper<GenericResultsetData> {

    @Override
    public GenericResultsetData mapRow(ResultSet rs) throws SQLException {
        List<ResultsetColumnHeaderData> headers = this.mapHeader(rs);
        List<ResultsetRowData> rowDataList = this.mapData(rs);
        return new GenericResultsetData(headers, rowDataList);
    }

    private List<ResultsetColumnHeaderData> mapHeader(ResultSet var1) throws SQLException {
        List<ResultsetColumnHeaderData> headers = new LinkedList<>();
        ResultSetMetaData rsmd = var1.getMetaData();
        int columnCount = rsmd.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = rsmd.getColumnName(i);
            String columnType = rsmd.getColumnTypeName(i);
            boolean isNullable = rsmd.isNullable(i) == ResultSetMetaData.columnNullable;
            boolean isPrimaryKey = false;
            String columnDisplayType = "string"; //todo: implement logic
            List<String> columnValues = Collections.emptyList();

            ResultsetColumnHeaderData columnHeaderData = new ResultsetColumnHeaderData(columnName, columnType, "", columnDisplayType, isNullable, isPrimaryKey);
            headers.add(columnHeaderData);
        }

        return headers;
    }

    private List<ResultsetRowData> mapData(ResultSet var1) throws SQLException {
        List<ResultsetRowData> data = new LinkedList<>();
        while (var1.next()) {
            List<String> row = new LinkedList<>();
            for (int i = 1; i <= var1.getMetaData().getColumnCount(); i++) {
                row.add(var1.getString(i));
            }
            ResultsetRowData rowData = ResultsetRowData.create(row);
            data.add(rowData);
        }
        return data;
    }
}
