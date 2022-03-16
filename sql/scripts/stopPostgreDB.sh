#Ivann De La Cruz(SID: 862081651), Kenneth Alvarez(SID: 862026376)

#! /bin/bash
pg_ctl -o "-c unix_socket_directories=$PGSOCKETS -p $PGPORT" -D $PGDATA -l $folder/logfile stop
