#!/bin/bash
export DB_NAME=$USER"_DB"
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd)"
pqsl -p $PGPORT $DB_NAME < $DIR/../src/create_tables.sql
pqsl -p $PGPORT $DB_NAME < $DIR/../src/load_data.sql
pqsl -p $PGPORT $DB_NAME < $DIR/../src/create_indexes.sql
