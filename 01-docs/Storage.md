## Storage

### Redis
```text
redis-cli     // connect to Redis inside a container
client list   // get a list of connected clients
keys *        // get all keys
```

### Postgres
- Analyze query
```text
explain (analyze, buffers)
select *
from <table_name>;

Analyze:
cost=0.00..10.64 
     0.00 - сколько времени по оценке планировщика потребуется, чтобы получить первые результаты вычитки (в ms)
            например, если postgres делает какую-либо агрегацию, или order by, это время увеличивается
     10.64 - общее время, которое займет вычитка данных по оценке планировщика (в ms)
actual time=0.057..086 - фактическое время, которое занимает вычитка при выполнении запроса (в ms)
rows=15 - сколько записей будет получено в результате вычитки данных
width=63 - количество байт в одной записи, которая получена в результате вычитки
           например, если из 7 полей в записи нам нужно только 3, то мы получим width 3-х полей
           это значение также позволяет посчитать,
               сколько данных база отдаст по сети клиенту в ответ на запрос: actual rows * width

Planning Time - время, которое база данных тратит на анализ запроса и принятие решения, как делать выборку данных
Execution Time - время выборки данных по запросу

Buffers:
shared hit=10 - количество чтений из кэша
read=24       - количество чтений с диска
```

- Get duration of active queries
```sql
select pid, usename, client_addr, state, application_name, now()-query_start as duration, query
from pg_catalog.pg_stat_activity
where client_addr is not null
  and state != 'idle';
```

- Get table size
```sql
select pg_size_pretty(pg_table_size('<schema_name>.<table_name>')) as table_size_without_indexes,
       pg_size_pretty(pg_total_relation_size('<schema_name>.<table_name>')) as table_size_with_indexes;
```

- Get counters for index scans
```sql
select relname as "table",
       indexrelname as "index",
       idx_scan as index_scan
from pg_catalog.pg_stat_user_indexes
where schemaname = '<schema_name>';
```

- Get unused indexes
```sql
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
```sql
select sum(heap_blks_read) as disk_reads,
       sum(heap_blks_hit) as cache_hits,
       round(sum(heap_blks_hit) / (sum(heap_blks_hit) + sum(heap_blks_read)) * 100, 2) as ratio
from pg_catalog.pg_statio_user_tables
where relname = '<table_name>';
```

- Get size of the page cache
```sql
select pg_size_pretty(setting::bigint * 8 * 1024) as cache_size
from pg_catalog.pg_settings
where "name" = 'shared_buffers';
```