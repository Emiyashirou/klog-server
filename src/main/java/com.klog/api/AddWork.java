package com.klog.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.klog.exception.BadRequestException;
import com.klog.exception.InternalServerErrorException;
import com.klog.model.basic.Work;
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
import static com.klog.utils.WorkUtils.getStatusOfActive;
import static com.klog.utils.WorkUtils.isValidWork;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class AddWork implements RequestHandler<Work, Object> {

    public Map<String, Object> handleRequest(Work input, Context context) {

        System.getProperties().setProperty("org.jooq.no-logo", "true");

        if(!isValidWork(input)){
            throw new BadRequestException("Invalid work");
        }

        Connection conn = null;
        Map<String, Object> result = new HashMap<>();

        try {
            conn = connect();
            DSLContext dslContext = DSL.using(conn, SQLDialect.MYSQL_8_0);
            String id = UUID.randomUUID().toString();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            dslContext.insertInto(table("klog.work")).columns(
                    field("id"),
                    field("title"),
                    field("create_date"),
                    field("modify_date"),
                    field("priority"),
                    field("status"),
                    field("description"))
                    .values(
                            id,
                            input.getTitle(),
                            timestamp,
                            null,
                            input.getPriority(),
                            getStatusOfActive(),
                            input.getDescription())
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
