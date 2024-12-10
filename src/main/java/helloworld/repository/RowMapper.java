package helloworld.repository;


import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {

    T mapRow(ResultSet var1) throws SQLException;


}
