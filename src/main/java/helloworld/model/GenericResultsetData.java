package helloworld.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public final class GenericResultsetData {
    private final List<ResultsetColumnHeaderData> columnHeaders;
    private final List<ResultsetRowData> data;

    public GenericResultsetData(List<ResultsetColumnHeaderData> columnHeaders, List<ResultsetRowData> data) {
        this.columnHeaders = columnHeaders;
        this.data = data;
    }

    public List<ResultsetColumnHeaderData> getColumnHeaders() {
        return columnHeaders;
    }

    public List<ResultsetRowData> getData() {
        return data;
    }

}
