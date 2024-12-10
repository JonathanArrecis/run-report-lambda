package helloworld.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public final class GenericResultsetData {
    private final List<ResultsetColumnHeaderData> columnHeaders;
    private final List<ResultsetRowData> data;

}
