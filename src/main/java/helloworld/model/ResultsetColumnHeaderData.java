package helloworld.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class ResultsetColumnHeaderData {
    private  String columnName;
    private  String columnType;
    private  String columnLength;
    private  String columnDisplayType;
    private  boolean isColumnNullable;
    private  boolean isColumnPrimaryKey;


}
