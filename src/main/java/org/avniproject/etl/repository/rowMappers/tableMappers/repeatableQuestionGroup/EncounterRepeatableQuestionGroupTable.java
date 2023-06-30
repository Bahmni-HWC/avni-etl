package org.avniproject.etl.repository.rowMappers.tableMappers.repeatableQuestionGroup;

import org.avniproject.etl.domain.metadata.Column;
import org.avniproject.etl.repository.rowMappers.tableMappers.Columns;
import org.avniproject.etl.repository.rowMappers.tableMappers.Table;

import java.util.List;
import java.util.Map;

import static org.avniproject.etl.repository.rowMappers.TableNameGenerator.EncounterRepeatableQuestionGroup;
import static org.avniproject.etl.repository.rowMappers.tableMappers.CommonColumns.*;

public class EncounterRepeatableQuestionGroupTable extends Table {
    @Override
    public List<Column> columns() {
        return new Columns()
                .withColumns(CommonRepeatableGroupColumns)
                .withColumns(List.of(
                        new Column("individual_id", Column.Type.integer, Column.ColumnType.index),
                        new Column("address_id", Column.Type.integer, Column.ColumnType.index),
                        new Column("encounter_id", Column.Type.integer, Column.ColumnType.index))).build();
    }

    @Override
    public String name(Map<String, Object> tableDetails) {
        return generateTableName(EncounterRepeatableQuestionGroup, null, tableDetails, "subject_type_name", "encounter_type_name", "parent_concept_name");
    }
}
