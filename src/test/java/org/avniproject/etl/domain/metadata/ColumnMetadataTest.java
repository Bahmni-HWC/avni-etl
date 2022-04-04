package org.avniproject.etl.domain.metadata;

import org.avniproject.etl.domain.ContextHolder;
import org.avniproject.etl.domain.OrganisationIdentity;
import org.avniproject.etl.domain.metadata.diff.AddColumn;
import org.avniproject.etl.domain.metadata.diff.Diff;
import org.avniproject.etl.domain.metadata.diff.RenameColumn;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ColumnMetadataTest {

    @Test
    public void shouldRenameColumnIfNecessary() {
        ContextHolder.create(new OrganisationIdentity(1, "dbUser", "schema", OrganisationIdentity.OrganisationType.Organisation));
        String uuid = UUID.randomUUID().toString();
        ColumnMetadata oldColumnMetadata = new ColumnMetadata(new Column("oldName", Column.Type.text), 12, ColumnMetadata.ConceptType.Text, uuid);
        ColumnMetadata newColumnMetadata = new ColumnMetadata(new Column("newName", Column.Type.text), 12, ColumnMetadata.ConceptType.Text, uuid);

        TableMetadata newTable = new TableMetadata();
        newTable.setName("table");

        List<Diff> changes = newColumnMetadata.findChanges(newTable, oldColumnMetadata);

        assertThat(changes.size(), is(1));
        assertThat(changes.get(0), instanceOf(RenameColumn.class));
        assertThat(changes.get(0).getSql(), is("alter table schema.table rename column \"oldName\" to \"newName\";"));
    }

    @Test
    //todo: fill me in
    public void shouldPopulateColumnDetails() {
//        ColumnMetadata oldColumn = new ColumnMetadata(1, new Column("oldName", Column.Type.text), 100, UUID.randomUUID().toString());
//        ColumnMetadata newColumn = new ColumnMetadata(null, new Column("oldName", Column.Type.text), 100);
    }
}