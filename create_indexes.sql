/*The sequence and trigger are for adding messages in the java script*/
CREATE SEQUENCE msgID_seq START WITH 1;

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