#Ivann De La Cruz(SID: 862081651), Kenneth Alvarez(SID: 862026376)

#! /bin/bash
export DB_NAME=$USER"_DB"
echo "creating db named ... "$DB_NAME
createdb -h localhost -p $PGPORT $DB_NAME
pg_ctl status

cp $PWD/data/*.csv $PGDATA/
