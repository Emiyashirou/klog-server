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
import static com.klog.utils.PostUtils.getStatusOfArchive;
import static com.klog.utils.PostUtils.isValidArchivePost;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class ArchivePost implements RequestHandler<Post, Object> {

    public Map<String, Object> handleRequest(Post input, Context context){

        System.getProperties().setProperty("org.jooq.no-logo", "true");

        if(!isValidArchivePost(input)){
            throw new BadRequestException("Invalid archive post input");
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
                    .set(field("status"), getStatusOfArchive())
                    .where(field("id").eq(input.getId()))
                    .execute();
            input.setStatus(getStatusOfArchive());

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
