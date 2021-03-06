package com.klog.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.klog.exception.InternalServerErrorException;
import com.klog.model.basic.Post;
import org.apache.commons.dbutils.DbUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static com.klog.connection.MySQLConnection.connect;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class GetPost implements RequestHandler<Post, Object> {

    public Map<String, Object> handleRequest(Post input, Context context) {

        System.getProperties().setProperty("org.jooq.no-logo", "true");

        ResultSet rs = null;
        Connection conn = null;
        Map<String, Object> result = new HashMap<>();

        try {
            conn = connect();
            DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL_8_0);

            rs = dslContext.select(
                    field("id"),
                    field("workId"),
                    field("title"),
                    field("create_date"),
                    field("modify_date"),
                    field("status"),
                    field("priority"),
                    field("content"))
                    .from(table("klog.post"))
                    .where(field("id").eq(input.getId()).and(field("status").eq(0)))
                    .fetchResultSet();
            Post post = new Post();
            while (rs.next()) {
                post = new Post(
                        rs.getString("id"),
                        rs.getString("workId"),
                        rs.getString("title"),
                        rs.getDate("create_date"),
                        rs.getDate("modify_date"),
                        rs.getInt("status"),
                        rs.getInt("priority"),
                        rs.getString("content"));
            }
            result.put("data", post);
            return result;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(conn);
        }
    }

}
