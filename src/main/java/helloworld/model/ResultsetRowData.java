package helloworld.model;


import java.util.List;

public final class ResultsetRowData {
    private final List<String> row;

    public static ResultsetRowData create(List<String> rowValues){
        return new ResultsetRowData(rowValues);
    }
    private ResultsetRowData(final List<String> rowValues){
        this.row = rowValues;
    }
    public List<String> getRow(){
        return row;
    }
}
