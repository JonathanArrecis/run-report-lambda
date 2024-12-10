package helloworld.repository;

import helloworld.model.RunReportRequest;
import helloworld.config.DatabaseConfig;
import helloworld.model.GenericResultsetData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepository {
    private static final String QUERY = "SELECT st.id, \n" +
            "         st.transaction_date, \n" +
            "         st.amount, \n" +
            "         case cc.operation_type when 'D' then 'Debito' else 'Credito' end description, \n" +
            "         case evl.enum_type when 1 then 'Debito' else 'Credito' end transaction_type, \n" +
            "         cc.short_description,  \n" +
            "         st.transaction_insert_order,\n" +
            "         1 id_bank,\n" +
            "         '' comments,\n" +
            "         ' ' tipo_ach,\n" +
            "         st.cause_id,\n" +
            "         1 operation,\n" +
            "         'general' report_name\n" +
            "  from  m_savings_account_transaction st\n" +
            "         left outer join r_enum_value evl on evl.enum_name = 'savings_transaction_type_enum' and evl.enum_id = st.transaction_type_enum\n" +
            "         left outer join m_code_causal cc on cc.id =  case coalesce(st.cause_id, evl.nexa_code) when 0 then evl.nexa_code else coalesce(st.cause_id,evl.nexa_code) end\n" +
            "         left outer join m_note dr  on dr.savings_account_transaction_id = st.id and dr.note_type_enum = 800\n" +
            "   where \n" +
            "  \tst.is_reversed = 0\n" +
            "    and transaction_type_enum  not in ('22','20','21')\n" +
            "    and cause_id not in (1047,1024,1046,1023,1067,1068,1025,1045,1053,1022,1021,1026,1044,1054,1013,1014,1015,1016,1017,1036,1076,1077,1027,1040,1082,1083,1086,1087,1084,1085,1081,1088,1033,1090,1091,1008,1039,1092, 1107, 1108, 1111, 1112\t)\n" +
            "    and st.savings_account_id = ${accountid}\n" +
            "    and st.transaction_date between ${startDate} and ${endDate}\n" +
            "    -- limit ${limit} offset ${offset}\n" +
            " union all\n" +
            " /* transacciones ach entrantes */\n" +
            " select at.id, at.transaction_date, at.amount, '' description, \n" +
            "          case when cc.operation_type = 'C'  then 'Credito' else 'Debito' end transaction_type,\n" +
            "          cc.short_description,\n" +
            "          at.transaction_insert_order,\n" +
            "          bn.id id_bank,\n" +
            "          CASE \n" +
            "           \tWHEN cause_id <> 1112 THEN mt.comments\n" +
            "           \tELSE 'Transferencia QR ACH'\n" +
            "          END comments,\n" +
            "\t  \t\t\tmt.origin_account_name tipo_ach,\n" +
            "          at.cause_id,\n" +
            "          1 operation,\n" +
            "          'ach-entrante' report_name\n" +
            "    from m_savings_account_transaction at \n" +
            "         inner join m_code_causal cc on cc.id = at.cause_id \n" +
            "         inner join m_currency mn on mn.int_code  = currency_int_code \n" +
            "         left outer join mwbank.customer_ach_credit mt on mt.transaction_id = at.id \n" +
            "         left outer join mwbank.customer_bank bn on bn.code = case when mt.origin_bank != '' then mt.origin_bank else substr(mt.destination_account_ach,5,4) end\n" +
            "         left outer join mwbank.customer_ach_file af on af.id = mt.ach_file_id\n" +
            "  where cause_id in (1047,1024,1112) \n" +
            "    and at.is_reversed = 0\n" +
            "    and at.savings_account_id = ${accountid}\n" +
            "    and at.transaction_date  between ${startDate} and ${endDate}\n" +
            "  union all\n" +
            "  /* transacciones ach salientes */\n" +
            "  select at.id, at.transaction_date, at.amount, '' description, \n" +
            "          case when cc.operation_type = 'C'  then 'Credito' else 'Debito' end transaction_type,\n" +
            "          cc.short_description,\n" +
            "          at.transaction_insert_order ,\n" +
            "          -- mt.beneficiary_bank id_bank,\n" +
            "          '' id_bank,\n" +
            "          -- mt.comments,\n" +
            "          '' comments,\n" +
            "          -- mt.beneficiary_name tipo_ach,\n" +
            "          '' tipo_ach,\n" +
            "          at.cause_id,\n" +
            "          1 operation,\n" +
            "          'ach-saliente' report_name\n" +
            "  from m_savings_account_transaction at  \n" +
            "       inner join m_code_causal cc on cc.id = at.cause_id \n" +
            "       inner join m_currency mn on mn.int_code  = currency_int_code \n" +
            "  --     left outer join mwbank.customer_transfer mt on mt.reference_number = at.id \n" +
            "  --     left outer join mwbank.customer_transfer mt on mt.refdebito = at.id\n" +
            "  --     left outer join mwbank.customer_bank bn on bn.id = mt.beneficiary_bank \n" +
            "  where cause_id in (1046,1023,1076,1077)  \n" +
            "    and at.is_reversed = 0\n" +
            "    and at.savings_account_id = ${accountid}\n" +
            "    and at.transaction_date  between  ${startDate} and ${endDate}\n" +
            " union all\n" +
            " /* transacciones devoluciones ach entrantes */\n" +
            " select at.id, at.transaction_date, at.amount, '' description, \n" +
            "          case when cc.operation_type = 'C'  then 'Credito' else 'Debito' end transaction_type,\n" +
            "          cc.short_description,\n" +
            "          at.transaction_insert_order ,\n" +
            "          mt.beneficiary_bank id_bank,\n" +
            "          mt.comments,\n" +
            "          mt.beneficiary_name tipo_ach,\n" +
            "          at.cause_id,\n" +
            "          1 operation,\n" +
            "          'ach-devolucion' report_name\n" +
            "  from m_savings_account_transaction at  \n" +
            "       inner join m_code_causal cc on cc.id = at.cause_id \n" +
            "       inner join m_currency mn on mn.int_code  = currency_int_code \n" +
            "       left outer join mwbank.customer_transfer mt on mt.ach_reverse_transaccion = at.id \n" +
            "       left outer join mwbank.customer_bank bn on bn.id = mt.beneficiary_bank  \n" +
            " where cause_id in (1067,1068) \n" +
            "   and at.is_reversed = 0\n" +
            "   and at.savings_account_id = ${accountid}\n" +
            "   and at.transaction_date  between  ${startDate} and ${endDate}\n" +
            "union all\n" +
            "/* transacciones transferencias locales  Debitos*/\n" +
            "select at.id, at.transaction_date, at.amount, '' description, \n" +
            "          case when cc.operation_type = 'C'  then 'Credito' else 'Debito' end transaction_type,\n" +
            "          cc.short_description,\n" +
            "          at.transaction_insert_order ,\n" +
            "          1 id_bank,\n" +
            "          -- mt.comments  ,\n" +
            "          '' comments ,\n" +
            "\t  \t  -- coalesce(cl.display_name,gr.display_name) tipo_ach,\n" +
            "\t  \t  '' tipo_ach,\n" +
            "          at.cause_id,\n" +
            "          1 operation,\n" +
            "          case when cause_id in (1045,1022,1082,1086) then 'lsaliente-misma-moneda' else 'lsaliente-distinfa-moneda' end report_name\n" +
            "  from m_savings_account_transaction at  \n" +
            "       inner join m_code_causal cc on cc.id = at.cause_id \n" +
            "       inner join m_currency mn on mn.int_code  = currency_int_code \n" +
            "--       inner join mwbank.customer_transfer mt on substr(mt.reference_number,2,position('-' in mt.reference_number)-2) = at.id\n" +
            "--       inner join mwbank.customer_transfer mt on mt.refdebito = at.id\n" +
            "--       inner join NexaCoreDB.m_savings_account sa on sa.id = mt.beneficiary_account\n" +
            "--       left outer join NexaCoreDB.m_client cl on cl.id = sa.client_id\n" +
            "--       left outer join NexaCoreDB.m_group gr on gr.id = sa.group_id\n" +
            " where cause_id in (1025,1045,1053,1022,1082,1083,1086,1087)\n" +
            "   and at.is_reversed = 0\n" +
            "   and at.savings_account_id = ${accountid}\n" +
            "   and at.transaction_date  between  ${startDate} and ${endDate}\n" +
            "union all\n" +
            "/* transacciones transferencias locales Creditos */\n" +
            "select at.id, at.transaction_date, at.amount, '' description, \n" +
            "          case when cc.operation_type = 'C'  then 'Credito' else 'Debito' end transaction_type,\n" +
            "          cc.short_description,\n" +
            "          at.transaction_insert_order ,\n" +
            "          1 id_bank,\n" +
            "          -- mt.comments,\n" +
            "          '' comments ,\n" +
            "          -- coalesce(cl.display_name,gr.display_name) tipo_ach ,\n" +
            "          '' tipo_ach,\n" +
            "          at.cause_id,\n" +
            "          1 operation,\n" +
            "          'local-entrante' report_name\n" +
            "  from m_savings_account_transaction at  \n" +
            "       inner join m_code_causal cc on cc.id = at.cause_id \n" +
            "       inner join m_currency mn on mn.int_code  = currency_int_code \n" +
            "--       left outer join mwbank.customer_transfer mt on substr(mt.reference_number,position('-' in mt.reference_number)+1) = at.id\n" +
            "--       left outer join mwbank.customer_transfer mt on mt.refcredito = at.id       \n" +
            "--      left outer join NexaCoreDB.m_savings_account sa on sa.id = mt.account_number_from\n" +
            "--       left outer join NexaCoreDB.m_client cl on cl.id = sa.client_id\n" +
            "--       left outer join NexaCoreDB.m_group gr on gr.id = sa.group_id\n" +
            " where cause_id in (1021,1026,1044,1054,1084,1085,1081,1088,1033)\n" +
            "   and at.is_reversed = 0\n" +
            "   and at.savings_account_id = ${accountid}\n" +
            "   and at.transaction_date  between  ${startDate} and ${endDate}\n" +
            "union all\n" +
            "/* transacciones pagos servicio */\n" +
            "select at.id, at.transaction_date, at.amount, '' description, \n" +
            "          case when cc.operation_type = 'C'  then 'Credito' else 'Debito' end transaction_type,\n" +
            "          cc.short_description,\n" +
            "          at.transaction_insert_order ,\n" +
            "          mt.payee id_bank,\n" +
            "          mt.customer_account_number comments ,\n" +
            "          cp.param_name tipo_ach ,\n" +
            "          at.cause_id,\n" +
            "          2 operation,\n" +
            "          'pago-servicio' report_name\n" +
            "  from m_savings_account_transaction at  \n" +
            "       inner join m_code_causal cc on cc.id = at.cause_id \n" +
            "       inner join m_currency mn on mn.int_code  = currency_int_code \n" +
            "       left outer join mwbank.customer_bill_pay mt on mt.reference_number = CONVERT(at.id , char)\n" +
            "       left outer join NexaCoreDB.m_savings_account sa on sa.id = mt.account_number_from\n" +
            "\t   left outer join mwbank.customer_payees cp on cp.id = mt.payee\n" +
            " where cause_id in (1013,1014,1015,1016,1017)\n" +
            "  and transaction_type_enum  not in ('22','20','21')\n" +
            "   and at.is_reversed = 0\n" +
            "   and at.savings_account_id = ${accountid}\n" +
            "   and at.transaction_date  between  ${startDate} and ${endDate} -- select * from mwbank.customer_bill_pay  where reference_number = 'Pending';\n" +
            "union all\n" +
            "/* transacciones OST */\n" +
            "select at.id, at.transaction_date, at.amount, '' description, \n" +
            "          case when cc.operation_type = 'C'  then 'Credito' else 'Debito' end transaction_type,\n" +
            "          cc.short_description,\n" +
            "          at.transaction_insert_order ,\n" +
            "          1 id_bank,\n" +
            "          -- case when coalesce(trim(ct.comments),'Retiro sin tarjeta') = '' then 'Retiro sin tarjeta' else coalesce((ct.comments),'Retiro sin tarjeta') end   comments,\n" +
            "          '' comments,\n" +
            "          trim(substr(mt.ubicacion_terminal,1,25)) tipo_ach,\n" +
            "          at.cause_id,\n" +
            "          3 operation,\n" +
            "          'retiro-sin-tarjeta' report_name\n" +
            "  from m_savings_account_transaction at  \n" +
            "       inner join m_code_causal cc on cc.id = at.cause_id \n" +
            "       inner join m_currency mn on mn.int_code  = currency_int_code \n" +
            "       left outer join NexaCoreDB.tm_transacciones mt on mt.savings_account_transaction_id = at.id\n" +
            "       left outer join mwbank.customer_movil_transfer ost on ost.id = mt.movil_transfer_id\n" +
            "--     left outer join mwbank.customer_transfer ct on ct.refdebito = ost.transaction_core_id\n" +
            " where cause_id in (1036)     \n" +
            "   and transaction_type_enum  not in ('22','20','21')\n" +
            "   and at.is_reversed = 0\n" +
            "   and at.savings_account_id = ${accountid}\n" +
            "   and at.transaction_date  between   ${startDate} and ${endDate}\n" +
            "union all\n" +
            "/* transacciones tarjeta debito */\n" +
            "select  st.id, \n" +
            "\t  st.transaction_date, \n" +
            "\t coalesce(tc.amount,st.amount) amount,\n" +
            "\t  case cc.operation_type when 'D' then 'Debito' else 'Credito' end description, \n" +
            "\t  case cc.operation_type when 'D' then 'Debito' else 'Credito' end transaction_type, \n" +
            "\t  cc.short_description,  \n" +
            "\t  st.transaction_insert_order,\n" +
            "\t   case when td.aq_mcc = 6011 and td.pan_entry_mode = 51 then 1 else coalesce(td.comercio_code,ct.codigo_comercio) end  id_bank,\n" +
            "\t  concat(substr(td.aq_name_location,1,1), substr(lower(substr(td.aq_name_location,1, (CHAR_LENGTH(trim(substr(td.aq_name_location,1, (CHAR_LENGTH(td.aq_name_location) - LOCATE(' ', REVERSE(td.aq_name_location))+1)))) - LOCATE(' ', REVERSE(trim(substr(td.aq_name_location,1, (CHAR_LENGTH(td.aq_name_location) - LOCATE(' ', REVERSE(td.aq_name_location))+1)))))+1))),2))  comments,\n" +
            "\t   ct.descripcion tipo_ach,\n" +
            "\t  st.cause_id,\n" +
            "          3 operation,\n" +
            "\t  'tarjeta-debito' report_name\n" +
            "from  m_savings_account_transaction st        \n" +
            "\t left outer join m_code_causal cc on cc.id =  st.cause_id\t\n" +
            "\t left outer join td_transactions td on td.savings_account_transaction_id = st.id\n" +
            "\t left outer join m_savings_account_transaction tc on tc.id = td.conciliacionId\n" +
            "\t left outer join mwbank.customer_categorias_compras_td ct on ct.code = td.aq_mcc\n" +
            "where \n" +
            "   st.is_reversed = 0\n" +
            "   and  ( ( st.transaction_type_enum  in (20)  and st.cause_id in (1027,1040,1090,1091) and td.confrontado is not null ) or  (  st.cause_id in (1008,1039,1092) ) )\n" +
            "   and td.reversada = 0\n" +
            "   and st.savings_account_id = ${accountid}\n" +
            "   and st.transaction_date between ${startDate} and ${endDate}\n" +
            "union all \n" +
            " /* transacciones qr locales entrantes */\n" +
            "select at.id, at.transaction_date, at.amount, '' description, \n" +
            "          case when cc.operation_type = 'C'  then 'Credito' else 'Debito' end transaction_type,\n" +
            "          cc.short_description,\n" +
            "          at.transaction_insert_order ,\n" +
            "          1 id_bank,\n" +
            "          -- qtd.addemdum comments,\n" +
            "          '' comments,\n" +
            "          -- qtd.origin_account_name tipo_ach ,\n" +
            "          '' tipo_ach,\n" +
            "          at.cause_id,\n" +
            "          1 operation,\n" +
            "          'local-entrante' report_name\n" +
            "  from m_savings_account_transaction at  \n" +
            "       inner join m_code_causal cc on cc.id = at.cause_id\n" +
            "--     left outer join mwbank.customer_transfer mt on mt.refcredito = at.id\n" +
            "--     LEFT OUTER JOIN mwbank.qr_transaccion_transfer qtt ON qtt.transfer_id = mt.id \n" +
            "--     LEFT OUTER JOIN mwbank.qr_transaccion qt ON qt.id = qtt.qr_transaction_id\n" +
            "--     LEFT OUTER JOIN mwbank.qr_transaccion_detalle qtd ON qtd.id = qt.transaction_detail_id \n" +
            " where cause_id in (1114, 1108)\n" +
            "   and at.is_reversed = 0\n" +
            "   and at.savings_account_id = ${accountid}\n" +
            " --  and qt.transaction_type = 'TC'\n" +
            "   and at.transaction_date  between  ${startDate} and ${endDate}\n" +
            "union all\n" +
            "/* transacciones qr salientes */\n" +
            "select at.id, at.transaction_date, at.amount, '' description, \n" +
            "          case when cc.operation_type = 'C'  then 'Credito' else 'Debito' end transaction_type,\n" +
            "          cc.short_description,\n" +
            "          at.transaction_insert_order ,\n" +
            "          -- cb.id id_bank,\n" +
            "          '' id_bank,\n" +
            "          -- mt.comments  ,\n" +
            "          '' comments,\n" +
            "\t  \t  --\tqtd.destination_account_name tipo_ach,\n" +
            "          '' tipo_ach,\n" +
            "          at.cause_id,\n" +
            "          1 operation,\n" +
            "          'qr-saliente' report_name\n" +
            "  from m_savings_account_transaction at  \n" +
            "       inner join m_code_causal cc on cc.id = at.cause_id\n" +
            "--        inner join mwbank.customer_transfer mt on mt.refdebito = at.id\n" +
            "--       left outer join mwbank.qr_transaccion_transfer qtt ON qtt.transfer_id = mt.id \n" +
            "--     left outer join mwbank.qr_transaccion qt ON qt.id = qtt.qr_transaction_id\n" +
            "--      left outer join mwbank.qr_transaccion_detalle qtd ON qtd.id = qt.transaction_detail_id \n" +
            "--       left outer  join mwbank.customer_bank cb on qtd.destination_bank = JSON_UNQUOTE(json_extract(cb.attrs, '$.destination') ) \n" +
            " where \n" +
            " \t cause_id in (1107,1111)\n" +
            "   and at.is_reversed = 0\n" +
            "  -- and qt.transaction_type = 'TD'\n" +
            "   and at.savings_account_id = ${accountid}\n" +
            "   and at.transaction_date  between  ${startDate} and ${endDate}\n" +
            "order by id \n" +
            "limit ${limit} offset ${offset}";



    public GenericResultsetData fetchTransactions(RunReportRequest runReportRequest){

        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(QUERY);
        ){
            Map<String, Object> params = new HashMap<>();
            params.put("accountid", runReportRequest.getIdAccount());
            params.put("startDate", runReportRequest.getFechaInicial());
            params.put("endDate", runReportRequest.getFechaFinal());


            RowMapper<GenericResultsetData> rowMapper = new GenericResultDataRowMapper();
            DynamicQueryExecutor executor = new DynamicQueryExecutor();
            List<GenericResultsetData> result = executor.executeQuery(conn, QUERY, params, rowMapper);

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }


}
