## Storage

### Redis
```
redis-cli   // connect to Redis inside a container
keys *      // get all keys
```

### Postgres
- Get duration of active queries
```
select pid, usename, client_addr, state, application_name, now()-query_start as duration, query
from pg_catalog.pg_stat_activity
where client_addr is not null
  and state != 'idle';
```
- Get table size
```
select pg_size_pretty(pg_table_size('<schema_name.<table_name>')) as table_size_without_indexes,
       pg_size_pretty(pg_total_relation_size('<schema_name.<table_name>')) as table_size_with_indexes;
```
- Get counters for index scans
```
select relname as "table",
       indexrelname as "index",
       idx_scan as index_scan
from pg_catalog.pg_stat_user_indexes
where schemaname = '<schema_name>';
```
- Get unused indexes
```
select indexrelid::regclass as "index",
       relid::regclass as "table",
       idx_scan as index_scan,
       'DROP INDEX ' || indexrelid::regclass || ';' as drop_index
from pg_catalog.pg_stat_user_indexes
join pg_catalog.pg_index using (indexrelid)
where idx_scan = 0
  and indisunique is false
order by "table";
```
- Get ratio of cache hits to disk reads<br/>
  The higher ratio, the more data is retrieved from the page cache.
```
select sum(heap_blks_read) as disk_reads,
       sum(heap_blks_hit) as cache_hits,
       round(sum(heap_blks_hit) / (sum(heap_blks_hit) + sum(heap_blks_read)) * 100, 2) as ratio
from pg_catalog.pg_statio_user_tables
where relname = '<table_name>';
```
- Get size of page cache
```
select pg_size_pretty(setting::bigint * 8 * 1024) as cache_size
from pg_catalog.pg_settings
where "name" = 'shared_buffers';
```