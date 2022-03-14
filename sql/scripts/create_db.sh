#!/bin/bash
export DB_NAME=$USER"_DB"
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd)"
psql -h localhost -p $PGPORT $DB_NAME < $DIR/../src/create_tables.sql
psql -h localhost -p $PGPORT $DB_NAME < $DIR/../src/load_data.sql
psql -h localhost -p $PGPORT $DB_NAME < $DIR/../src/create_indexes.sql
