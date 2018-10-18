package com.klog.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.klog.exception.BadRequestException;
import com.klog.exception.InternalServerErrorException;
import com.klog.exception.NotFoundException;
import com.klog.model.basic.Post;
import org.apache.commons.dbutils.DbUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static com.klog.connection.MySQLConnection.connect;
import static com.klog.utils.PostUtils.isValidPostEdit;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class EditPost implements RequestHandler<Post, Object> {

    public Map<String, Object> handleRequest(Post input, Context context) {

        System.getProperties().setProperty("org.jooq.no-logo", "true");

        if(!isValidPostEdit(input)){
            throw new BadRequestException("Invalid post");
        }

        Connection conn = null;
        ResultSet rs = null;
        Map<String, Object> result = new HashMap<>();

        try {
            conn = connect();
            DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL_8_0);

            rs = dslContext.select(field("id"))
                    .from(table("klog.post"))
                    .where(field("id").eq(input.getId()))
                    .fetchResultSet();

            if(!rs.next()){
                throw new NotFoundException("Post not found");
            }

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            dslContext.update(table("klog.post"))
                    .set(field("modify_date"), timestamp)
                    .set(field("title"), input.getTitle())
                    .set(field("status"), input.getStatus())
                    .set(field("priority"), input.getPriority())
                    .set(field("content"), input.getContent())
                    .where(field("id").eq(input.getId()))
                    .execute();

            result.put("data", input);
            return result;
        } catch (Exception e) {
            if(e instanceof NotFoundException){
                throw new NotFoundException(e.getMessage());
            } else {
                throw new InternalServerErrorException(e.getMessage());
            }
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(conn);
        }
    }

}
