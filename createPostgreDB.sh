#! /bin/bash
export DB_NAME=$USER"_DB"
echo "creating db named ... "$DB_NAME
createdb -h localhost -p $PGPORT $DB_NAME
pg_ctl status

cp Reformatted_Data/*.csv $PGDATA/
