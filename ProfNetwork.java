/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class ProfNetwork {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of Messenger
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public ProfNetwork (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end ProfNetwork

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
          List<String> record = new ArrayList<String>();
         for (int i=1; i<=numCol; ++i)
            record.add(rs.getString (i));
         result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       if(rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }



   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            ProfNetwork.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      ProfNetwork esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Messenger object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new ProfNetwork (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
		System.out.println("1. View Profile");
                System.out.println("2. Go to Friend List");
                System.out.println("3. Update Profile");
                System.out.println("4. Write a new message");
                System.out.println("5. Request Dashboard");
                System.out.println("6. View messages");
		System.out.println("7. Search For People");
                System.out.println(".........................");
                System.out.println("9. Log out");
                switch (readChoice()){
                   case 1: displayProf	(esql, authorisedUser); break;
		   case 2: FriendList	(esql, authorisedUser); break;
                   case 3: UpdateProfile(esql, authorisedUser); break;
                   case 4: NewMessage	(esql, authorisedUser); break;
                   case 5: ReqDash	(esql, authorisedUser); break;
                   case 6: ViewMessage	(esql, authorisedUser); break;
                   case 7: srcPpl	(esql, authorisedUser); break;
		   case 9: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

 /*
 *     * Creates a new user with privided login, passowrd and phoneNum
 *         * An empty block and contact list would be generated and associated with a user
 *             **/
   public static void CreateUser(ProfNetwork esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();
         System.out.print("\tEnter user email: ");
         String email = in.readLine();
	 //Creating empty contact\block lists for a user
	 String query = String.format("INSERT INTO USR (userId, password, email) VALUES ('%s','%s','%s')", login, password, email);
	 esql.executeUpdate(query);
	 System.out.println ("User successfully created!");
	 }catch(Exception e){
	 	System.err.println (e.getMessage ());
	 }
    }//end
	
    /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(ProfNetwork esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();
         
         String query = String.format("SELECT * FROM USR WHERE userId = '%s' AND password = '%s'", login, password);
         int userNum = esql.executeQuery(query);
         if (userNum > 0) {
                return login;
         }
	 else {
		System.out.print("\tIncorrect userid or password! Please try again! ");
	 }
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

   public static void lookFriendMenu(ProfNetwork esql, String authUse, List<String> names){
      try{
	System.out.print("\nType in the username of the person whose profile you want to see: ");
        String toLook = in.readLine();
	names.add(toLook);
	int validFriend = 1;
	// String outputTesting = "";
	for(int i = names.size()-1; i >= 1; i--){
		// outputTesting = "Trying " + names.get(i-1) + " and " + names.get(i) + "\n";
		//System.out.print(outputTesting);
		String query = String.format("SELECT * FROM CONNECTION_USR C WHERE ((C.userId = '%s' AND C.connectionid = '%s') OR (C.userId = '%s' AND C.connectionid = '%s')) AND C.status = 'Accept'", names.get(i-1), names.get(i), names.get(i), names.get(i-1));
        	if(esql.executeQuery(query) != 1) 
			validFriend = 0;
	}
        //System.out.print("done checking\n");
        if (validFriend != 1){
		System.out.print("Invalid input.\n");
		names.remove(names.size() - 1);
		return;
	}
	//System.out.print("friend menu time\n");
        boolean friendmenu = true;
        while(friendmenu) {
		String out = names.get(names.size()-1) + "'s Profile";
        	System.out.println(out);
		displayProf(esql, names.get(names.size()-1));
		// dispFList(esql, toLook);
                System.out.println("\n---------");
                out = "1. Write " + names.get(names.size()-1) + " a new message";
		System.out.println(out);
		out = "2. View " + names.get(names.size()-1) + "'s friends list";
		System.out.println(out);
                if((names.size() < 4) && (names.size()> 1))
			System.out.println("3. Send Friend Request");
		System.out.println("4. Look at one of their friends");
                System.out.println(".........................");
                System.out.println("9. Go Back");
                switch (readChoice()){
                   case 2: dispFList(esql, names.get(names.size()-1)); break;
                   case 1: NewMessage(esql, authUse); break;
                   case 3: SendRequestTO(esql, authUse, names.get(names.size()-1)); break;
		   case 4: lookFriendMenu(esql, authUse, names); break; 
                   case 9: names.remove(names.size() - 1); friendmenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }

         return;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }
   }//end

   public static void srcPpl(ProfNetwork esql, String authUse){
	try{
	  boolean srchmenu = true;
             while(srchmenu) {
                System.out.println("SEARCH");
                System.out.println("---------");
                System.out.println("1. Search By Name");
                System.out.println("2. View Profile of Person");
                System.out.println(".........................");
                System.out.println("9. Go back");
                switch (readChoice()){
                   case 1:
                        System.out.print("\nWho do you want to search for? ");
          		String srcT= in.readLine();
          		String query = String.format("SELECT userId as Username, name as Name FROM USR WHERE (userId LIKE '%%");
          		query = query + srcT + "%%') OR (name LIKE '%%" + srcT + "%%')";
          		// System.out.print(query);
          		int validIn = esql.executeQuery(query);
          	        if(validIn < 1){
                           System.out.print("\nCould not find anyone with that username or name");
                           break;
                        }
                        esql.executeQueryAndPrintResult(query);
                        break;
                   case 2:
			System.out.print("\nEnter the username of the person whose profile you want to view\n\n\t ");
                        String srcTar= in.readLine();
			System.out.print("\n");
                        String query2 = String.format("SELECT* FROM USR WHERE userId = '%s'", srcTar);
			int validIn2 = esql.executeQuery(query2);
                        if(validIn2 != 1){                           
                           System.out.print("\nCould not find anyone with that username or name");
                           break;
                        }
                        displayProf(esql, srcTar);
                        break;
                   case 9: srchmenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
	}catch(Exception e){
          System.err.println (e.getMessage ());
          return;
	}
   }

   public static void displayProf(ProfNetwork esql, String fName){
      try{
         String query = String.format("SELECT U.name, U.userId, U.email, U.dateOfBirth FROM USR U WHERE U.userId = '%s'", fName);
         System.out.print("\n");
	 esql.executeQueryAndPrintResult(query);
	 System.out.print("\nWork Experience");
	 query = String.format("SELECT W.company, W.role, W.location, W.startDate, W.endDate FROM WORK_EXPR W WHERE W.userId = '%s'", fName);
	 esql.executeQueryAndPrintResult(query);
	 System.out.print("\nEducation");
         query = String.format("SELECT E.instituitionName, E.major, E.degree, E.startDate, E.endDate FROM EDUCATIONAL_DETAILS E WHERE E.userId = '%s'", fName);
         esql.executeQueryAndPrintResult(query);
	 System.out.print("\n");
         return;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }
   }//end


    public static void dispFList(ProfNetwork esql, String authUse){
	try{
		String fListQ = String.format("SELECT C.connectionId AS Friends FROM connection_usr C WHERE C.userId = '%s' AND C.status = 'Accept' UNION SELECT C.userId FROM connection_usr C WHERE C.connectionId = '%s' AND C.status = 'Accept'", authUse, authUse);
                int fListNum = esql.executeQuery(fListQ);
                if (fListNum <= 0){
			System.out.print("No connections yet\n");
                }
                else{
                	esql.executeQueryAndPrintResult(fListQ);
                }
	
	}catch(Exception e){
         System.err.println (e.getMessage ());
	}

    }

// Rest of the functions definition go in here
   /*
   * View friends and can access friends profile. Additionally you can send a connection request or a message to them 
   */
    public static void FriendList(ProfNetwork esql, String authUse){
	try{
	// System.out.print("\tEnter user login: ");
        // String login = in.readLine();
        if (authUse != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("FRIENDS");
                System.out.println("---------");
                System.out.println("1. View Friend List");
                System.out.println("2. View Friend's Profile");
                System.out.println(".........................");
                System.out.println("9. Go back");
                switch (readChoice()){
                   case 1:
			dispFList(esql, authUse);
			break;
                   case 2:
			List<String> names=new ArrayList<String>();
			names.add(authUse);
			lookFriendMenu(esql, authUse, names);
			break;
                   case 9: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
      }catch(Exception e){
         System.err.println (e.getMessage ());
      } 
    }

    /*
    * Updating user's password
    */
    public static void UpdateProfile(ProfNetwork esql, String login){
        try{
         System.out.print("\tEnter new password: ");
         String newPassword = in.readLine();
         String query = String.format("UPDATE USR SET password = '%s' WHERE userId = '%s'", newPassword, login);
         int userNum = esql.executeQuery(query);
         if (userNum > 0)
                System.out.print("\tPassword failed to change! ");
                //return login; // THIS NEEDS TO CHANGE
         return;
        }catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }
    }


    /*
    * Send message to anyone on network 
    */
    public static void NewMessage(ProfNetwork esql, String senderId){
        try{
            System.out.print("\tEnter receiverId: ");
            String receiverId = in.readLine();
            System.out.print("\tEnter draft message you want to send to receiver: ");
            String contents = in.readLine();

            String query = String.format("INSERT INTO MESSAGE (senderId, receiverId, contents, status) VALUES ('%s','%s','%s', '%s')", senderId, receiverId, contents, "Draft");
            esql.executeUpdate(query);

            System.out.print("\tDo you want to send the message you drafted(Y/N)? ");
            contents = in.readLine();
            if(contents.equals("Y")) {
                System.out.print("\tMessage will be sent!\n");
                query = String.format("UPDATE MESSAGE SET status = 'SENT' WHERE receiverId = '%s' AND senderId='%s'", receiverId, senderId);
                esql.executeUpdate(query);
            }
            return ;
        }catch(Exception e){
         System.err.println (e.getMessage ());
         return ;
      }
    }

    /* Send request for connection  */
    public static void SendRequest(ProfNetwork esql, String authU){ //TODO: input validation
	try{
	System.out.print("\n\tWho would you like to send a request to?\n\t");
        String conRec = in.readLine();
	String query = String.format("SELECT * FROM USR WHERE userId = '%s'", conRec);
        int validIn = esql.executeQuery(query);
	if(validIn != 1){
	   System.out.print("\nUsername wrong or does not exist");
	   return;
	}
	List<List<String>> firstLevel  = new ArrayList<List<String>>();
        query= String.format("SELECT DISTINCT C.connectionId FROM connection_usr C WHERE C.userId = '%s' AND (C.status = 'Accept' OR C.status = 'Request') UNION SELECT DISTINCT C.userId FROM connection_usr C WHERE C.connectionId = '%s' AND (C.status = 'Accept' OR C.status = 'Request')", authU, authU);
        // System.out.print("\ngetting list 1");
        firstLevel = esql.executeQueryAndReturnResult(query);
        Iterator<List<String>> listIt = firstLevel.iterator();
        while(listIt.hasNext()){
	   String debug = "Comparing " + listIt.next().get(0) + " and " + conRec;
	   System.out.println(debug);
	   if(listIt.next().get(0).equals(conRec)== true){
	      System.out.println("User is already a friend"); 
	      return;
	   }
	}
        query = String.format("INSERT INTO CONNECTION_USR (userId, connectionId, status) VALUES ('%s', '%s', 'Request')", authU, conRec);
	esql.executeUpdate(query);
	
	System.out.print("Request Sent!\n");
	}catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }
    }

    public static void SendRequestTO(ProfNetwork esql, String authU, String recip){
      try{
	String query = String.format("SELECT * FROM USR WHERE userId = '%s'", recip);
        int validIn = esql.executeQuery(query);
        if(validIn != 1){
           System.out.print("\nUsername wrong or does not exist");
           return;
        }
	List<List<String>> firstLevel  = new ArrayList<List<String>>();
        query= String.format("SELECT DISTINCT C.connectionId FROM connection_usr C WHERE C.userId = '%s' AND (C.status = 'Accept' OR C.status = 'Request') UNION SELECT DISTINCT C.userId FROM connection_usr C WHERE C.connectionId = '%s' AND (C.status = 'Accept' OR C.status = 'Request')", authU, authU);
	firstLevel = esql.executeQueryAndReturnResult(query);
        Iterator<List<String>> listIt = firstLevel.iterator();
        while(listIt.hasNext()){
           if(listIt.next().get(0).equals(recip)== true){
	      System.out.println("User is already a friend"); 
	      return;
	   }
        }

	query = String.format("INSERT INTO CONNECTION_USR (userId, connectionId, status) VALUES ('%s', '%s', 'Request')", authU, recip);
        esql.executeUpdate(query);
        System.out.print("Request Sent!\n");
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return ;
      }
    }

    public static void ReqDash(ProfNetwork esql, String authU){
      try{
        boolean rD = true;
         while(rD) {
            System.out.println("\nRequest Dashboard");
            System.out.println("---------");
            System.out.println("1. View Requests You Made");
            System.out.println("2. View Incoming Requests");    
	    System.out.println("3. Send Requests (limit 5 non friends for new users)\n\t");
	    System.out.println("9. EXIT");
            switch (readChoice()){
               case 1: 
		 String query1 = String.format("SELECT C.connectionId AS Recipient, C.status FROM CONNECTION_USR C WHERE C.userId = '%s'", authU);
                 System.out.print("\n");
                 if(esql.executeQueryAndPrintResult(query1) == 0) {System.out.println("Nothing here\n");};
		 break;
               case 2:
                 String query2 = String.format("SELECT C.userId AS Sender FROM CONNECTION_USR C WHERE C.connectionId = '%s' AND status = 'Request'", authU);
                 System.out.print("\n");
                 esql.executeQueryAndPrintResult(query2);
                 break;
               case 3: 
                 // if the max request has not been sent, input val and then send
                 // Might be able to fix the implementation bellow, but for now...
		 System.out.println("\nNew users can send up to 5 requests\nAll users can request to connect with 2nd and 3rd level connections. Consider doing this from your friendlist! \n\tWho would you like to send a request to?\t");
        	 // test: connection target exist?
		 String conRec = in.readLine();
        	 String query3 = String.format("SELECT * FROM USR U WHERE U.userId = '%s'", conRec);
        	 int validIn3 = esql.executeQuery(query3);
		 if(validIn3 != 1){
           	    System.out.println("Username wrong or does not exist\n");
                    break;
		 }
		 // test: can the user make the connection without friendship level?
		 String queryV = String.format("SELECT * FROM USR U WHERE U.userId = '%s' AND U.fCon > 0", authU);
                 int validR = esql.executeQuery(queryV); 
		 if(validR != 1){
                    List<List<String>> firstLevel  = new ArrayList<List<String>>();
                    List<List<String>> secondLevel = new ArrayList<List<String>>();
                    List<List<String>> thirdLevel  = new ArrayList<List<String>>();
                    query3= String.format("SELECT DISTINCT C.connectionId FROM connection_usr C WHERE C.userId = '%s' AND C.status = 'Accept' UNION SELECT DISTINCT C.userId FROM connection_usr C WHERE C.connectionId = '%s' AND C.status = 'Accept'", authU, authU);
                    // System.out.print("\ngetting list 1");
		    firstLevel = esql.executeQueryAndReturnResult(query3);
                    String temp = "";
                    Iterator<List<String>> listIt = firstLevel.iterator();
                    // System.out.print("\nmaking list of second connections");
		    while(listIt.hasNext()){
                       temp = listIt.next().get(0);
                       query3 = String.format("SELECT DISTINCT C.connectionId FROM connection_usr C WHERE C.userId = '%s' AND C.status = 'Accept' UNION SELECT DISTINCT C.userId FROM connection_usr C WHERE C.connectionId = '%s' AND C.status = 'Accept'", temp, temp);
                       secondLevel.addAll(esql.executeQueryAndReturnResult(query3));
                    }
                    Iterator<List<String>> listIt2 = secondLevel.iterator();
                    // System.out.print("\nmaking list of third connections");
		    while(listIt2.hasNext()){
                       temp = listIt2.next().get(0);
                       query3 = String.format("SELECT DISTINCT C.connectionId FROM connection_usr C WHERE C.userId = '%s' AND C.status = 'Accept' UNION SELECT DISTINCT C.userId FROM connection_usr C WHERE C.connectionId = '%s' AND C.status = 'Accept'", temp, temp);
                       thirdLevel.addAll(esql.executeQueryAndReturnResult(query3));
                    }
                    thirdLevel.addAll(secondLevel);
                    Iterator<List<String>> listIt3 = thirdLevel.iterator();
		    boolean isFriend = false;
		    String debug = "";
		    // System.out.println("\n Is the recip a friend?");
		    while (listIt3.hasNext()){
		       //debug = "Testing " + listIt3.next().get(0) + " == " + conRec; 
		       //System.out.println(debug);
		       if(listIt3.next().get(0).equals(conRec)== true){
                          // System.out.println("\nRecip is friend!");
			  SendRequestTO(esql, authU, conRec);
			  isFriend = true;
			  break;
                       }
		    }
		    if(isFriend == false){System.out.println("Sorry, this person is not a 2nd or 3rd connection\n");}
                 }
		 else {
		   SendRequestTO(esql, authU, conRec);
		   query3 = String.format("UPDATE USR SET fCon = fCon - 1 WHERE userId = '%s'", authU);
		   esql.executeUpdate(query3);
		 }
                 break;
               case 9: rD = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
	  }
         }catch(Exception e){
         System.err.println (e.getMessage ());
         return ;
         }
     }



    /* View user's messages and have the option to delete them*/
    public static void ViewMessage(ProfNetwork esql, String receiverId){
        try{
            String query = String.format("UPDATE MESSAGE SET status = 'Delivered' WHERE status <> 'Read' AND receiverId = '%s'", receiverId);
            esql.executeUpdate(query);

            query = String.format("SELECT * FROM MESSAGE M WHERE M.receiverId = '%s'", receiverId);
            int userNum = esql.executeQueryAndPrintResult(query);
            System.out.print("\tDo you want to mark a message as read(Y/N)? ");
            String contents = in.readLine();
            if(contents.equals("Y")) {
               System.out.print("\tEnter msgId to be marked as read: ");
               String msgId = in.readLine();
               query = String.format("UPDATE MESSAGE SET status = 'Read' WHERE receiverId = '%s' AND msgId='%s'", receiverId, msgId);
               esql.executeUpdate(query);
            }
            if (userNum <= 0){
                System.out.print("\tYou have no messages!\n");
            }
            System.out.println("1. Delete a specific message from a user to Friend List");
            System.out.println("2. Go back");
            switch (readChoice()){
               case 1: DeleteMessage(esql, receiverId); break;
               case 2: break;
            }
        }catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }
    }

    /* Delete a message */
    public static void DeleteMessage(ProfNetwork esql, String userId){
        try{
            System.out.print("\tEnter msgId you want to delete: ");
            String msgId = in.readLine();
            String query = String.format("DELETE FROM MESSAGE WHERE receiverId = '%s' AND msgID = '%s'", userId, msgId);
            esql.executeUpdate(query);
            System.out.print("\tMessage has been deleted! ");
            return;
        }catch(Exception e){
         System.err.println (e.getMessage ());
         return;
      }
    }

}//end ProfNetwork

