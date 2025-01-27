package org.avniproject.etl.repository.sync;

import org.avniproject.etl.domain.ContextHolder;
import org.avniproject.etl.domain.NullObject;
import org.avniproject.etl.domain.metadata.SchemaMetadata;
import org.avniproject.etl.domain.metadata.TableMetadata;
import org.avniproject.etl.repository.sql.TransactionalSyncSqlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.format;
import static org.avniproject.etl.repository.JdbcContextWrapper.runInOrgContext;

@Repository
public class DuplicateRowDeleteAction implements EntitySyncAction {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DuplicateRowDeleteAction(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean supports(TableMetadata tableMetadata) {
        return new TransactionalSyncSqlGenerator().supports(tableMetadata) ||
                tableMetadata.getType().equals(TableMetadata.Type.Address);
    }

    @Override
    public void perform(TableMetadata tableMetadata, Date lastSyncTime, Date dataSyncBoundaryTime, SchemaMetadata currentSchemaMetadata) {
        if (!this.supports(tableMetadata)) {
            return;
        }
        deleteDuplicateRows(tableMetadata.getName(), lastSyncTime);
    }

    private void deleteDuplicateRows(String tableName, Date lastSyncTime) {
        String schema = ContextHolder.getDbSchema();
        String baseSql = format("delete from \"${schemaName}\".\"${tableName}\"\n" +
                "where id in (\n" +
                "    select t1.id\n" +
                "    from \"${schemaName}\".\"${tableName}\" t1\n" +
                "             inner join \"${schemaName}\".\"${tableName}\" t2 on\n" +
                "                t1.id = t2.id\n" +
                "            and t1.last_modified_date_time < t2.last_modified_date_time)" +
                "   and last_modified_date_time <= '%s';", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(lastSyncTime));

        String sql = baseSql
                .replace("${schemaName}", schema)
                .replace("${tableName}", tableName);
        runInOrgContext(() -> {
            jdbcTemplate.execute(sql);
            return NullObject.instance();
        }, jdbcTemplate);
    }
}
