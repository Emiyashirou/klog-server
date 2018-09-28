package com.klog.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.klog.exception.InternalServerErrorException;
import com.klog.model.basic.Work;
import com.klog.model.input.GetWorkListInput;
import org.apache.commons.dbutils.DbUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

import static com.klog.connection.MySQLConnection.connect;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class GetWorkList implements RequestHandler<GetWorkListInput, Object> {

    public Map<String, Object> handleRequest(GetWorkListInput input, Context context) {

        System.getProperties().setProperty("org.jooq.no-logo", "true");

        ResultSet rs = null;
        Connection conn = null;
        Map<String, Object> result = new HashMap<>();

        try {
            conn = connect();
            DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL_8_0);

            rs = dslContext.select(
                    field("id"),
                    field("title"),
                    field("create_date"),
                    field("modify_date"),
                    field("status"),
                    field("priority"),
                    field("description"))
                    .from(table("klog.work"))
                    .where(field("status").eq(0))
                    .fetchResultSet();
            List<Work> works = new ArrayList<>();
            while (rs.next()) {
                works.add(new Work(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getDate("create_date"),
                        rs.getDate("modify_date"),
                        rs.getInt("status"),
                        rs.getInt("priority"),
                        rs.getString("description")
                ));
            }
            result.put("data", works);
            return result;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(conn);
        }
    }

}
