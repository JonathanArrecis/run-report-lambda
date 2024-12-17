package helloworld.model;


public final class ResultsetColumnHeaderData {
    private  String columnName;
    private  String columnType;
    private  String columnLength;
    private  String columnDisplayType;
    private  boolean isColumnNullable;
    private  boolean isColumnPrimaryKey;

    public ResultsetColumnHeaderData(String columnName, String columnType, String columnLength, String columnDisplayType, boolean isColumnNullable, boolean isColumnPrimaryKey) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.columnLength = columnLength;
        this.columnDisplayType = columnDisplayType;
        this.isColumnNullable = isColumnNullable;
        this.isColumnPrimaryKey = isColumnPrimaryKey;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength;
    }

    public String getColumnDisplayType() {
        return columnDisplayType;
    }

    public void setColumnDisplayType(String columnDisplayType) {
        this.columnDisplayType = columnDisplayType;
    }

    public boolean isColumnNullable() {
        return isColumnNullable;
    }

    public void setColumnNullable(boolean columnNullable) {
        isColumnNullable = columnNullable;
    }

    public boolean isColumnPrimaryKey() {
        return isColumnPrimaryKey;
    }

    public void setColumnPrimaryKey(boolean columnPrimaryKey) {
        isColumnPrimaryKey = columnPrimaryKey;
    }
}
