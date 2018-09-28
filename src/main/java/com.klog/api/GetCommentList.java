package com.klog.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.klog.exception.InternalServerErrorException;
import com.klog.model.basic.Comment;
import com.klog.model.input.GetCommentListInput;
import org.apache.commons.dbutils.DbUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.klog.connection.MySQLConnection.connect;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class GetCommentList implements RequestHandler<GetCommentListInput, Object> {

    public Map<String, Object> handleRequest(GetCommentListInput input, Context context) {

        System.getProperties().setProperty("org.jooq.no-logo", "true");

        ResultSet rs = null;
        Connection conn = null;
        Map<String, Object> result = new HashMap<>();

        try {
            conn = connect();
            DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL_8_0);

            rs = dslContext.select(
                    field("id"),
                    field("author"),
                    field("create_date"),
                    field("modify_date"),
                    field("status"),
                    field("ref"),
                    field("content"))
                    .from(table("klog.comment"))
                    .where(field("status").eq(0).and(field("ref").eq(input.getPostId())))
                    .fetchResultSet();
            List<Comment> comments = new ArrayList<>();
            while (rs.next()) {
                comments.add(new Comment(
                        rs.getString("id"),
                        rs.getString("author"),
                        rs.getDate("create_date"),
                        rs.getDate("modify_date"),
                        rs.getInt("status"),
                        rs.getString("ref"),
                        rs.getString("content")
                ));
            }
            result.put("data", comments);
            return result;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(conn);
        }
    }

}
