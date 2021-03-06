package com.klog.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.klog.exception.BadRequestException;
import com.klog.exception.InternalServerErrorException;
import com.klog.model.basic.Comment;
import org.apache.commons.dbutils.DbUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.klog.connection.MySQLConnection.connect;
import static com.klog.utils.CommentUtils.getStatusOfOpen;
import static com.klog.utils.CommentUtils.isValidComment;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class AddComment implements RequestHandler<Comment, Object> {

    public Map<String, Object> handleRequest(Comment input, Context context) {

        System.getProperties().setProperty("org.jooq.no-logo", "true");

        if(!isValidComment(input)){
            throw new BadRequestException("Invalid comment");
        }

        Connection conn = null;
        Map<String, Object> result = new HashMap<>();

        try {
            conn = connect();
            DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL_8_0);
            String id = UUID.randomUUID().toString();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            dslContext.insertInto(table("klog.comment")).columns(
                    field("id"),
                    field("author"),
                    field("create_date"),
                    field("modify_date"),
                    field("ref"),
                    field("status"),
                    field("content"))
                    .values(
                            id,
                            input.getAuthor(),
                            timestamp,
                            null,
                            input.getRef(),
                            getStatusOfOpen(),
                            input.getContent())
                    .execute();

            input.setId(id);

            result.put("data", input);
            return result;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

}
