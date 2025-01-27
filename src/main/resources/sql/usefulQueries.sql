-- ETL Re-run

-- Find routines with 'delete' string in their name
SELECT routines.routine_name, parameters.data_type, parameters.ordinal_position
FROM information_schema.routines
         LEFT JOIN information_schema.parameters ON routines.specific_name=parameters.specific_name
WHERE routines.specific_schema='public' and routines.routine_name like '%delete%'
ORDER BY routines.routine_name, parameters.ordinal_position;

-- Show content of routine with name 'delete_etl_metadata_for_schema'
select * from information_schema.routines where routines.routine_name= 'delete_etl_metadata_for_schema';
select * from pg_proc where proname= 'delete_etl_metadata_for_schema';

-- Create function to delete etl metadata for an org
DROP FUNCTION IF EXISTS delete_etl_metadata_for_schema(inrolname text, inpassword text);
create function delete_etl_metadata_for_schema(in_impl_schema text, in_db_user text) returns bool
    language plpgsql
as
$$
BEGIN
    EXECUTE 'set role ' || in_impl_schema || ';';
    execute 'drop schema ' || in_impl_schema || ' cascade;';
    execute 'delete from entity_sync_status where db_user = ''' || in_db_user || ''';';
    execute 'delete from entity_sync_status where schema_name = ''' || in_impl_schema || ''';';
    execute 'delete from index_metadata where table_metadata_id in (select id from table_metadata where schema_name = ''' || in_impl_schema || ''');';
    execute 'delete from column_metadata where table_id in (select id from table_metadata where schema_name = ''' || in_impl_schema || ''');';
    execute 'delete from table_metadata where schema_name = ''' || in_impl_schema || ''';';
    return true;
END
$$;

-- Create function to delete etl metadata for org-group
DROP FUNCTION IF EXISTS delete_etl_metadata_for_org(inrolname text, inpassword text);
create function delete_etl_metadata_for_org(in_impl_schema text, in_db_user text) returns bool
    language plpgsql
as
$$
BEGIN
    EXECUTE 'set role openchs;';
    execute 'drop schema ' || in_impl_schema || ' cascade;';
    execute 'delete from entity_sync_status where db_user = ''' || in_db_user || ''';';
    execute 'delete from entity_sync_status where schema_name = ''' || in_impl_schema || ''';';
    execute 'delete from index_metadata where table_metadata_id in (select id from table_metadata where schema_name = ''' || in_impl_schema || ''');';
    execute 'delete from column_metadata where table_id in (select id from table_metadata where schema_name = ''' || in_impl_schema || ''');';
    execute 'delete from table_metadata where schema_name = ''' || in_impl_schema || ''';';
    return true;
END
$$;