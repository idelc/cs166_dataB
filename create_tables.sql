DROP TABLE WORK_EXPR;
DROP TABLE EDUCATIONAL_DETAILS;
DROP TABLE MESSAGE;
DROP TABLE CONNECTION_USR;
DROP TABLE USR;


CREATE TABLE USR(
	userId varchar(10) UNIQUE NOT NULL, 
	password varchar(10) NOT NULL,
	email text NOT NULL,
	name char(50),
	dateOfBirth date,
	Primary Key(userId));


CREATE TABLE WORK_EXPR(
	userId char(10) NOT NULL, 
	company char(50) NOT NULL, 
	role char(50) NOT NULL,
	location char(50),
	startDate date,
	endDate date,
	PRIMARY KEY(userId,company,role,startDate),
	FOREIGN KEY (userID) REFERENCES USR(userID));

CREATE TABLE EDUCATIONAL_DETAILS(
	userId char(10) NOT NULL, 
	instituitionName char(50) NOT NULL, 
	major char(50) NOT NULL,
	degree char(50) NOT NULL,
	startDate date,
	endDate date,
	PRIMARY KEY(userId,major,degree),
	FOREIGN KEY (userID) REFERENCES USR(userID));

CREATE TABLE MESSAGE(
	msgId integer UNIQUE NOT NULL, 
	senderId char(10) NOT NULL,
	receiverId char(10) NOT NULL,
	contents char(500) NOT NULL,
	sendTime timestamp,
	deleteStatus integer,
	status char(30) NOT NULL,
	PRIMARY KEY(msgId));

CREATE TABLE CONNECTION_USR(
	userId char(10) NOT NULL, 
	connectionId char(10) NOT NULL, 
	status char(30) NOT NULL,
	PRIMARY KEY(userId,connectionId),
	FOREIGN KEY (userID) REFERENCES USR(userID));