/*Ivann De La Cruz(SID: 862081651), Kenneth Alvarez(SID: 862026376)*/

COPY USR(
	userId, 
	password,
	email,
	name,
	dateOfBirth
)
FROM 'USR.csv'
DELIMITER ',' CSV HEADER;


COPY WORK_EXPR(
	userId, 
	company, 
	role,
	location,
	startDate,
	endDate
)
FROM 'Work_Ex.csv'
DELIMITER ',' CSV HEADER;


COPY EDUCATIONAL_DETAILS(
	userId, 
	instituitionName, 
	major,
	degree,
	startDate,
	endDate
)
FROM 'Edu_Det.csv'
DELIMITER ',' CSV HEADER;


COPY MESSAGE(
	msgId, 
	senderId,
	receiverId,
	contents,
	sendTime,
	deleteStatus,
	status
)
FROM 'Message.csv'
DELIMITER ',' CSV HEADER;


COPY CONNECTION_USR(
	userId, 
	connectionId, 
	status
)
FROM 'Connection.csv'
DELIMITER ',' CSV HEADER;
