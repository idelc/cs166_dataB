/*Ivann De La Cruz(SID: 862081651), Kenneth Alvarez(SID: 862026376)*/

/*The sequence and trigger are for adding messages in the java script*/
CREATE SEQUENCE msgID_seq START WITH 27813;

CREATE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION func_name ()
RETURNS "trigger" AS
$BODY$
BEGIN
NEW.msgid = nextval('msgID_seq');

RETURN NEW;

END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE TRIGGER newMessage BEFORE INSERT
ON Message FOR EACH ROW
EXECUTE PROCEDURE func_name();

CREATE OR REPLACE FUNCTION initCon()
RETURNS "trigger" AS
$BODY$
BEGIN
NEW.fCon = 5;

RETURN NEW;

END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE TRIGGER newUsr BEFORE INSERT
ON USR FOR EACH ROW
EXECUTE PROCEDURE initCon();
                                 
