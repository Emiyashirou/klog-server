package com.klog.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.klog.exception.BadRequestException;
import com.klog.exception.InternalServerErrorException;
import com.klog.model.basic.Post;
import com.klog.model.input.GetPostListInput;
import org.apache.commons.dbutils.DbUtils;
import org.jooq.Condition;
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
import static com.klog.utils.PostUtils.isValidPostListInput;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.value;

public class GetPostList implements RequestHandler<GetPostListInput, Object> {

    public Map<String, Object> handleRequest(GetPostListInput input, Context context) {

        System.getProperties().setProperty("org.jooq.no-logo", "true");

        if(!isValidPostListInput(input)){
            throw new BadRequestException("Invalid get post list input");
        }

        ResultSet rs = null;
        Connection conn = null;
        Map<String, Object> result = new HashMap<>();

        try {
            conn = connect();
            DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL_8_0);

            Condition getPostByWorkId;
            if(input != null && input.getWorkId() != null && input.getWorkId().length() != 0){
                getPostByWorkId = field("workId").eq(value(input.getWorkId()));
            } else {
                getPostByWorkId = null;
            }

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
                    .where(field("status").eq(0).and(getPostByWorkId))
                    .fetchResultSet();
            List<Post> posts = new ArrayList<>();
            while (rs.next()) {
                posts.add(new Post(
                        rs.getString("id"),
                        rs.getString("workId"),
                        rs.getString("title"),
                        rs.getDate("create_date"),
                        rs.getDate("modify_date"),
                        rs.getInt("status"),
                        rs.getInt("priority"),
                        rs.getString("content")
                ));
            }
            result.put("data", posts);
            return result;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(conn);
        }
    }
}

