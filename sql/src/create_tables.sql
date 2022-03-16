/*Ivann De La Cruz(SID: 862081651), Kenneth Alvarez(SID: 862026376)*/

DROP TABLE WORK_EXPR;
DROP TABLE EDUCATIONAL_DETAILS;
DROP TABLE MESSAGE;
DROP TABLE CONNECTION_USR;
DROP TABLE USR;


CREATE TABLE USR(
        userId varchar(30) UNIQUE NOT NULL,
        password varchar(30) NOT NULL,
        email text NOT NULL,
        name char(50),
        dateOfBirth date,
	fCon integer,
        Primary Key(userId));


CREATE TABLE WORK_EXPR(
        userId varchar(30) NOT NULL,
        company char(50) NOT NULL,
        role char(50) NOT NULL,
        location char(50),
        startDate date,
        endDate date,
        PRIMARY KEY(userId,company,role,startDate),
        FOREIGN KEY (userID) REFERENCES USR(userID));

CREATE TABLE EDUCATIONAL_DETAILS(
        userId varchar(30) NOT NULL,
        instituitionName char(50) NOT NULL,
        major char(50) NOT NULL,
        degree char(50) NOT NULL,
        startDate date,
        endDate date,
        PRIMARY KEY(userId,major,degree),
        FOREIGN KEY (userID) REFERENCES USR(userID));

CREATE TABLE MESSAGE(
        msgId integer UNIQUE NOT NULL,
        senderId varchar(30) NOT NULL,
        receiverId varchar(30) NOT NULL,
        contents char(500) NOT NULL,
        sendTime timestamp,
        deleteStatus integer,
        status char(30) NOT NULL,
        PRIMARY KEY(msgId),
        FOREIGN KEY (senderID) REFERENCES USR(userID),
        FOREIGN KEY (receiverID) REFERENCES USR(userID));

CREATE TABLE CONNECTION_USR(
        userId varchar(30) NOT NULL,
        connectionId char(30) NOT NULL,
        status char(30) NOT NULL,
        PRIMARY KEY(userId,connectionId),
        FOREIGN KEY (userID) REFERENCES USR(userID));
