package helloworld;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RunReportService {

    public List<Transaction> getTransactions(Connection connection,RunReportRequest runReportRequest){

        List<Transaction> transactions = new LinkedList<>();

        String sql = "select savings_account_id ,mcc.description , msat.amount, mcc.operation_type  \n" +
                "from m_savings_account_transaction msat \n" +
                "join m_code_causal mcc on mcc.id = msat.cause_id \n" +
                "where msat.savings_account_id = ? \n" +
                "and msat.transaction_date between ? and ? limit 10 \n" ;


        try(PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1, runReportRequest.getIdAccount());
            ps.setString(2, runReportRequest.getFechaInicial());
            ps.setString(3, runReportRequest.getFechaFinal());
            System.out.println("Query: " + ps.toString());

           try(ResultSet rs = ps.executeQuery();){
               System.out.println("Query executed");
               while(rs.next()){
                    String savings_account_id = rs.getString("savings_account_id");
                    String description = rs.getString("description");
                    String amount = rs.getString("amount");
                    String operation_type = rs.getString("operation_type");
                    Transaction transaction = new Transaction(savings_account_id, description, amount, operation_type);
                    transactions.add(transaction);
                   System.out.println("Transaction: " + transaction.toString());
               }
           }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transactions;
    }






}
