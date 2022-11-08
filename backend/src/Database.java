package cse216.group4.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;



import java.util.ArrayList;
import java.util.Random;



import java.util.Collections;

public class Database {
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAllUser;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOneUser;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOneUser;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOneUser;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropTable;

    /**
     * A prepared statement for adding a like to a message 
     */
    private PreparedStatement mLike;

    /**
     * A prepared statement for adding a like to a message 
     */
    private PreparedStatement mDislike;

    /**
     * A prepapred statement for removing a like on a message
     */
    private PreparedStatement mUnlike;

    /**
     * A prepared statement for removing a dislike on a message 
     */
    private PreparedStatement mUndislike;

    /**
     * A prepared statement that returns the total number of dislikes on a message 
     */
    private PreparedStatement mDislikeCnt;

    /**
     * A prepared statement that returns the total number of likes on a message 
     */
    private PreparedStatement mLikeCnt;

    /**
     * A prepared statement for creating the user table in our database
     */
    private PreparedStatement uCreateTable;

    /**
     * A prepared statement for dropping the user table in our database
     */
    private PreparedStatement uDropTable;

    /**
     * A prepared statement for deleting a record in the user table
     */
    private PreparedStatement uDeleteOne;

    /**
     * A prepared statement for selecting all records in the user table
     */        
    private PreparedStatement uSelectAll;
            
    /**
     * A prepared statement for selecting one record in the user table
     */        
    private PreparedStatement uSelectOne;

    /**
     * A prepared statement for updating a record in the user table
     */        
    private PreparedStatement uUpdateOne;

    /**
     * A prepared statement for updating a record in the user table
     */        
    private PreparedStatement uUpdateOneComment;
    
    /**
     * A prepared statement for creating the comments table in our database
     */        
    private PreparedStatement cCreateTable;
    
    /**
     * A prepared statement for creating the comments table in our database
     */        
    private PreparedStatement cDropTable;

    /**
     * A prepared statement for deleting a record in the comments table
     */        
    private PreparedStatement cDeleteOne;

     /**
     * A prepared statement for deleting a record in the comments table
     */        
    private PreparedStatement cDeleteOneUser;

    /**
     * A prepared statement for inserting a record in the comments table
     */        
    private PreparedStatement cInsertOne;

    /**
     * A prepared statement for selecting all records in the comments table
     */        
    private PreparedStatement cSelectAll;
    
    /**
     * A prepared statement for selecting all records in the comments table
     */ 
    private PreparedStatement cSelectAllMsg;
    /**
     * A prepared statement for selecting one record in the comments table
     */        
    private PreparedStatement cSelectOne;

    /**
     * A prepared statement for selecting one record in the comments table
     */        
    private PreparedStatement cSelectAllUser;
    
    /**
     * A prepared statement for selecting one record in the comments table
     */        
    private PreparedStatement cSelectOneUser;
    
    /**
     * A prepared statement for updating a record in the comments table
     */        
    private PreparedStatement cUpdateOne;

     /**
     * A prepared statement for updating a record in the comments table
     */        
    private PreparedStatement cUpdateOneUser;
    
    /**
     * A prepared statement for creating the upvotes table in the database
     */        
    private PreparedStatement upCreateTable;
    
    /**
     * A prepared statement for dropping the upvotes table in the database
     */        
    private PreparedStatement upDropTable;

    /**
     * A prepared statement for deleting a record from the upvotes table
     */        
    private PreparedStatement upDeleteOne;

    /**
     * A prepared statement for inserting a record into the upvotes table
     */        
    private PreparedStatement upInsertOne;

    /**
     * A prepared statement for selecting all records from the upvotes table
     */        
    private PreparedStatement upSelectAll;
    
    /**
     * A prepared statement for selecting one record from the upvotes table
     */        
    private PreparedStatement upSelectOne;
    
    /**
     * A prepared statement for selecting one record from the upvotes table
     */        
    private PreparedStatement upSelectAllUser;
    
    /**
     * A prepared statement for selecting one record from the upvotes table
     */        
    private PreparedStatement upSelectOneUser;
     
    /**
     * A prepared statement for creating the downvotes table in the database
     */        
    private PreparedStatement dCreateTable;
    
    /**
     * A prepared statement for dropping the downvotes table from the database
     */        
    private PreparedStatement dDropTable;

    /**
     * A prepared statement for deleting a record in the downvotes table
     */        
    private PreparedStatement dDeleteOne;
    
    /**
     * A prepared statement for inserting a record in the downvotes table
     */        
    private PreparedStatement dInsertOne;
    
    /**
     * A prepared statement for selecting all records in the downvotes table
     */        
    private PreparedStatement dSelectAll;

    /**
     * A prepared statement for selecting a record from the downvotes table
     */        
    private PreparedStatement dSelectOne;
    
    /**
     * A prepared statement for selecting all records from the downvotes table
     */        
    private PreparedStatement dSelectAllUser;

    /**
     * A prepared statement for select a record from the downvotes table
     */        
    private PreparedStatement dSelectOneUser;

    /**
     * A prepared statement for authenticating a user
     */        
    private PreparedStatement aWebLogin;
            
      

    /**
     * RowData is like a struct in C: we use it to hold data, and we allow 
     * direct access to its fields.  In the context of this Database, RowData 
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database.  RowData and the 
     * Database are tightly coupled: if one changes, the other should too.
     */
    public static class RowData {
        /**
         * The ID of this row of the database
         */
        int mId;
        /**
         * The Subject stored in this row
         */
        String mSubject;
        /**
         * The message stored in this row
         */
        String mMessage;
        /**
         * The number of likes on a message 
         */
        int mLikes;
        /**
         * The number of dislikes on a message 
         */
        int mDislikes;
        /**
         * The user id
         */
        String mUserId;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowData(int id, String subject, String message, int likes, int dislikes, String user_id) {
            mId = id;
            mSubject = subject;
            mMessage = message;
            mLikes = likes;
            mDislikes = dislikes;
            mUserId = user_id;
        }
    }
    
    //user row data
    public static class RowDataUser {
        /**
         * The ID of this row of the database
         */
        int uId;
        /**
         * The firstname stored in this row
         */
        String uFirstName;
        /**
         * The lastname stored in this row
         */
        String uLastName;
        /**
         * The email stored in this row
         */
        String uEmail;

        /**
         * The user comment stored in this row
         */
        String uComment;

        /**
         * The token stored in this row 
         */
        int uUserToken;
        /**
         * The user id stored in this row 
         */
        String uUserId;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowDataUser(String first_name, String last_name, String email, String comment, int user_token, String user_id) {
            uFirstName = first_name;
            uLastName = last_name;
            uEmail = email;
            uComment = comment;
            uUserToken = user_token;
            uUserId = user_id;
        }
    }

    //comment row data
    public static class RowDataComment {
        /**
         * The ID of this row of the database
         */
        int cId;
        /**
         * The user id stored in this row
         */
        String cUserId;
        /**
         * The msg id stored in this row
         */
        int cMsgId;
        /**
         * The body stored in this row 
         */
        String cBody;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowDataComment(int id, String user_id, int msg_id, String body) {
            cId = id;
            cUserId = user_id;
            cMsgId = msg_id;
            cBody = body;
        }
    }

    //upvote row data
    public static class RowDataUpVote {
        /**
         * The ID of this row of the database
         */
        int upId;
        /**
         * The user id stored in this row
         */
        String upUserId;
        /**
         * The msg id stored in this row
         */
        int upMsgId;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowDataUpVote(int id, String user_id, int msg_id) {
            upId = id;
            upUserId = user_id;
            upMsgId = msg_id;
        }
    }

    //downvote row data
    public static class RowDataDownVote {
        /**
         * The ID of this row of the database
         */
        int dId;
        /**
         * The user id stored in this row
         */
        String dUserId;
        /**
         * The msg id stored in this row
         */
        int dMsgId;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowDataDownVote(int id, String user_id, int msg_id) {
            dId = id;
            dUserId = user_id;
            dMsgId = msg_id;
        }
    }


    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private Database() {

    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String db_url) {
        // Create an un-configured Database object
        Database db = new Database();

        /// Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to find postgresql driver: " + "Cause: " + cnfe.getCause() 
            + " Exception: " + cnfe.getException());
            return null;
        } catch (URISyntaxException s) {
            System.out.println("URI Syntax Error");
            return null;
        }

        // Attempt to create all of our prepared statements.  If any of these 
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "tblData"
            //     as constants, and then build the strings for the statements
            //     from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception

            //Messages Table
            db.mCreateTable = db.mConnection.prepareStatement(
                " CREATE TABLE  tblData                       " + 
                " (id           SERIAL          PRIMARY KEY , " + 
                "  subject      VARCHAR(50)     NOT NULL    , " + 
                "  message      VARCHAR(500)    NOT NULL    , " + 
                "  likes        INTEGER         DEFAULT 0   , " +
                "  dislikes     INTEGER         DEFAULT 0   , " +
                "  user_id      VARCHAR(50)     NOT NULL    , " +
                "  CHECK        (likes >= 0)                , " + 
                "  CHECK        (dislikes >= 0)             ) " );
            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE tblData");

            // Standard CRUD operations
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM tblData WHERE id = ?");
            db.mDeleteOneUser = db.mConnection.prepareStatement("DELETE FROM tblData WHERE id = ? AND user_id=?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO tblData VALUES (default, ?, ?, ?, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM tblData");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from tblData WHERE id=?");
            db.mSelectAllUser = db.mConnection.prepareStatement("SELECT * FROM tblData WHERE user_id=?");
            db.mSelectOneUser = db.mConnection.prepareStatement("SELECT * from tblData WHERE id=? AND user_id=?");
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE tblData SET message = ?, subject = ? WHERE id = ?");
            db.mUpdateOneUser = db.mConnection.prepareStatement("UPDATE tblData SET message = ?, subject = ? WHERE id = ? AND user_id=?"); 
            db.mLike      = db.mConnection.prepareStatement("UPDATE tblData SET likes = likes + 1 WHERE id = ?"); 
            db.mDislike   = db.mConnection.prepareStatement("UPDATE tblData SET dislikes = dislikes + 1 WHERE id = ?");
            db.mUnlike    = db.mConnection.prepareStatement("UPDATE tblData SET likes = likes - 1 WHERE id = ?"); 
            db.mUndislike = db.mConnection.prepareStatement("UPDATE tblData SET dislikes = dislikes - 1 WHERE id = ?");
            db.mLikeCnt   = db.mConnection.prepareStatement("SELECT likes FROM tblData WHERE id = ? ");
            db.mDislikeCnt= db.mConnection.prepareStatement("SELECT dislikes FROM tblData WHERE id = ? ");
            
            //Users Table
            db.uCreateTable = db.mConnection.prepareStatement(
                " CREATE TABLE  userData                      " + 
                " (id           SERIAL          PRIMARY KEY , " +
                "  user_id      VARCHAR(50)     NOT NULL    , " + 
                "  first_name   VARCHAR(100)    NOT NULL    , " + 
                "  last_name    VARCHAR(100)    NOT NULL    , " + 
                "  email        VARCHAR(100)    NOT NULL    , " +
                "  comment      VARCHAR(200)    NOT NULL    , " +
                "  user_token   INTEGER         DEFAULT 0   ) " );
            db.uDropTable = db.mConnection.prepareStatement("DROP TABLE userData");

            // Standard CRUD operations for users table
            db.uDeleteOne = db.mConnection.prepareStatement("DELETE FROM userData WHERE user_id = ?");
            db.aWebLogin = db.mConnection.prepareStatement("INSERT INTO userData VALUES (default, ?, ?, ?, ?, ?, ?)");
            db.uSelectAll = db.mConnection.prepareStatement("SELECT * FROM userData");
            db.uSelectOne = db.mConnection.prepareStatement("SELECT * from userData WHERE user_id =?");
            db.uUpdateOne = db.mConnection.prepareStatement("UPDATE userData SET user_token = ? WHERE user_id = ?");
            db.uUpdateOneComment = db.mConnection.prepareStatement("UPDATE userData SET comment = ? WHERE user_id = ?");
            
            //Comments Table
            db.cCreateTable = db.mConnection.prepareStatement(
                " CREATE TABLE  commentData                   " + 
                " (id           SERIAL          PRIMARY KEY , " + 
                "  user_id      VARCHAR(50)     NOT NULL    , " + 
                "  msg_id       INTEGER         DEFAULT 0   , " + 
                "  body         VARCHAR(MAX)    NOT NULL    ) " );
            db.cDropTable = db.mConnection.prepareStatement("DROP TABLE commentData");

            // Standard CRUD operations
            db.cDeleteOne = db.mConnection.prepareStatement("DELETE FROM commentData WHERE id = ?");
            db.cDeleteOneUser = db.mConnection.prepareStatement("DELETE FROM commentData WHERE id = ? AND user_id=?");
            db.cInsertOne = db.mConnection.prepareStatement("INSERT INTO commentData VALUES (default, ?, ?, ?)");
            db.cSelectAll = db.mConnection.prepareStatement("SELECT * FROM commentData");
            db.cSelectOne = db.mConnection.prepareStatement("SELECT * from commentData WHERE id=?");
            db.cSelectAllMsg = db.mConnection.prepareStatement("SELECT * from commentData WHERE msg_id=?");
            db.cSelectAllUser = db.mConnection.prepareStatement("SELECT * FROM commentData WHERE user_id=?");
            db.cSelectOneUser = db.mConnection.prepareStatement("SELECT * from commentData WHERE id=? AND user_id=?");
            db.cUpdateOne = db.mConnection.prepareStatement("UPDATE commentData SET body = ? WHERE id = ?");
            db.cUpdateOneUser = db.mConnection.prepareStatement("UPDATE commentData SET body = ? WHERE id = ? AND user_id=?"); 


            //Upvotes table
            db.upCreateTable = db.mConnection.prepareStatement(
                " CREATE TABLE  upvoteData                       " + 
                " (id           SERIAL          PRIMARY KEY    , " + 
                "  user_id      VARCHAR(50)     NOT NULL       , " + 
                "  msg_id       INTEGER         DEFAULT 0      ) " );
            db.upDropTable = db.mConnection.prepareStatement("DROP TABLE upvoteData");

            // Standard CRUD operations
            db.upDeleteOne = db.mConnection.prepareStatement("DELETE FROM upvoteData WHERE id = ?");
            db.upInsertOne = db.mConnection.prepareStatement("INSERT INTO upvoteData VALUES (default, ?, ?)");
            db.upSelectAll = db.mConnection.prepareStatement("SELECT * FROM upvoteData");
            db.upSelectOne = db.mConnection.prepareStatement("SELECT * from upvoteData WHERE id=?");
            db.upSelectAllUser = db.mConnection.prepareStatement("SELECT * FROM upvoteData WHERE user_id=?");
            db.upSelectOneUser = db.mConnection.prepareStatement("SELECT * from upvoteData WHERE id=? AND user_id=?");

            //Downvotes table
            db.dCreateTable = db.mConnection.prepareStatement(
                " CREATE TABLE  downvoteData                     " + 
                " (id           SERIAL          PRIMARY KEY    , " + 
                "  user_id      VARCHAR(50)     NOT NULL       , " + 
                "  msg_id       INTEGER         DEFAULT 0      ) " );
            db.dDropTable = db.mConnection.prepareStatement("DROP TABLE downvoteData");

            // Standard CRUD operations
            db.dDeleteOne = db.mConnection.prepareStatement("DELETE FROM downvoteData WHERE id = ?");
            db.dInsertOne = db.mConnection.prepareStatement("INSERT INTO downvoteData VALUES (default, ?, ?)");
            db.dSelectAll = db.mConnection.prepareStatement("SELECT * FROM downvoteData");
            db.dSelectOne = db.mConnection.prepareStatement("SELECT * from downvoteData WHERE id=?");
            db.dSelectAllUser = db.mConnection.prepareStatement("SELECT * FROM downvoteData WHERE user_id=?");
            db.dSelectOneUser = db.mConnection.prepareStatement("SELECT * from downvoteData WHERE id=? AND user_id=?");
            
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        System.out.println("Disconnecting...");
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * Insert a row into the database
     * 
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRow(String subject, String message, String userid) {
        int count = 0;
        try {
            if ((subject != null && !subject.isEmpty()) && (message != null && !message.isEmpty())) {
                mInsertOne.setString(1, subject);
                mInsertOne.setString(2, message);
                // Initialize likes and dislikes to 0 
                mInsertOne.setInt(3, 0); 
                mInsertOne.setInt(4, 0);
                mInsertOne.setString(5, userid);
                count += mInsertOne.executeUpdate();
            } else {
                System.out.println("ERROR: Subject or Message is null.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all subjects and their ID
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectAll() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), 
                                    rs.getString("subject"), 
                                    rs.getString("message"), 
                                    rs.getInt("likes"),
                                    rs.getInt("dislikes"),
                                    rs.getString("user_id")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOne(int id) {
        RowData res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("id"), 
                                  rs.getString("subject"), 
                                  rs.getString("message"), 
                                  rs.getInt("likes"),
                                  rs.getInt("dislikes"),
                                  rs.getString("user_id")); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    
    /**
     * Query the database for a list of all messages
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectAllUserMessages(String user_id) {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            mSelectAllUser.setString(1, user_id);
            ResultSet rs = cSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), 
                                    rs.getString("subject"), 
                                    rs.getString("message"), 
                                    rs.getInt("likes"),
                                    rs.getInt("dislikes"),
                                    rs.getString("user_id")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * @param user_id the user id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOneUserMessage(int id, String user_id) {
        RowData res = null;
        try {
            mSelectOneUser.setInt(1, id);
            mSelectOneUser.setString(2, user_id);
            ResultSet rs = mSelectOneUser.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("id"), 
                                  rs.getString("subject"), 
                                  rs.getString("message"), 
                                  rs.getInt("likes"),
                                  rs.getInt("dislikes"),
                                  rs.getString("user_id")); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRow(int id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRowUserMessage(int id, String user_id) {
        int res = -1;
        try {
            mDeleteOneUser.setInt(1, id);
            mDeleteOneUser.setString(2, user_id);
            res = mDeleteOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id The id of the row to update
     * @param message The new message contents
     * @param subject: The new subject contents
     * @param likes: the number of likes the message has
     * 
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOne(int id, String subject, String message) {
        int res = -1;
        try {
            mUpdateOne.setString(2, subject);
            mUpdateOne.setString(1, message);
            mUpdateOne.setInt(3, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id The id of the row to update
     * @param message The new message contents
     * @param subject: The new subject contents
     * @param likes: the number of likes the message has
     * 
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOneUserMessage(int id, String user_id, String subject, String message) {
        int res = -1;
        try {
            mUpdateOneUser.setString(2, subject);
            mUpdateOneUser.setString(1, message);
            mUpdateOneUser.setInt(3, id);
            mUpdateOneUser.setString(4, user_id);
            res = mUpdateOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create tblData.  If it already exists, this will print an error
     */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            System.out.println("Error: Table already exists");
            e.printStackTrace();
        }
    }

    /**
     * Remove tblData from the database.  If it does not exist, this will print
     * an error.
     */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetch the number of likes for a message in the database
     * @param id The id of the message 
     * 
     * @return total number of likes
     */
    int getLikes(int id ){
        int likeCount = -1;
        try {
            mLikeCnt.setInt(1, id);
            ResultSet rs = mLikeCnt.executeQuery();
            if (rs.next()) {
                likeCount = rs.getInt("likes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likeCount;
    }

    /**
     * Fetch the number of dislikes for a message in the database
     * @param id The id of the message 
     * 
     * @return total number of dislikes
     */
    int getDislikes(int id ){
        int dislikeCount = -1;
        try {
            mDislikeCnt.setInt(1, id);
            ResultSet rs = mDislikeCnt.executeQuery();
            if (rs.next()) {
                dislikeCount = rs.getInt("dislikes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dislikeCount;
    }

    /**
     * Add a like to a row in the Databse
     * @param id The id of the row to update
     * 
     * @return total number of likes
     */
    int like(int id) {
        //int likeCount = 0;
        int likes = -1;
        try{
            mLike.setInt(1, id);
            mLike.executeUpdate();
            likes = getLikes(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likes;
    }

     /**
     * remove a like from a row in the Databse
     * @param id The id of the row to update
     * 
     * @return total number of likes
     */
    int dislike(int id) {
        int dislikes = -1;
        try{
            mDislike.setInt(1, id);
            mDislike.executeUpdate();
            dislikes = getDislikes(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dislikes;
    }

    /**
     * Remove a like from a row in the tblData table 
     * @param id The id of the row to update in tblData table 
     * 
     * @return The new total number of likes
     */
    int unLike(int id) {
        int result = -1;
        try{
            mUnlike.setInt(1, id);
            result = mUnlike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Remove a dislike from a row in the tblData table 
     * @param id The id of the row to update in the tblData table 
     * 
     * @return The new total number of likes
     */
    int unDislike(int id) {
        int result = -1;
        try{
            mUndislike.setInt(1, id);
            result = mUndislike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }




    //userTableMethods

    /**
     * Insert a row into the database
     * 
     * @param first_name The subject for this new row
     * @param last_name The message body for this new row
     * @param email The email for this new row
     * @param comment The user comment for this new row
     * @param user_token The user token for this new row
     * @param user_id The user id for this new row
     * @return The number of rows that were inserted
     */
    int insertRowUser(String first_name, String last_name, String email, String comment, int user_token, String user_id) {
        int count = 0;
        try {
            if ((first_name != null && !first_name.isEmpty()) && (last_name != null && !last_name.isEmpty()) && (user_id != null && !user_id.isEmpty())) {
                aWebLogin.setString(1, first_name);
                aWebLogin.setString(2, last_name);
                aWebLogin.setString(3, email);
                aWebLogin.setString(4, comment);
                aWebLogin.setInt(5, user_token);
                aWebLogin.setString(6, user_id);
                count += aWebLogin.executeUpdate();
            } else {
                System.out.println("ERROR: firstname, lastname, token or id is null.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all users
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowDataUser> selectAllUser() {
        ArrayList<RowDataUser> res = new ArrayList<RowDataUser>();
        try {
            ResultSet rs = uSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowDataUser(rs.getString("first_name"),
                                    rs.getString("last_name"), 
                                    rs.getString("email"),
                                    rs.getString("comment"),
                                    rs.getInt("user_token"),
                                    rs.getString("user_id")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param userid The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataUser selectOneUser(String userid) {
        RowDataUser res = null;
        try {
            uSelectOne.setString(1, userid);
            ResultSet rs = uSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowDataUser(rs.getString("first_name"), 
                                  rs.getString("last_name"), 
                                  rs.getString("email"),
                                  rs.getString("comment"), 
                                  rs.getInt("user_token"),
                                  rs.getString("user_id")); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param userid The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRowUser(String userid) {
        int res = -1;
        try {
            uDeleteOne.setString(1, userid);
            res = uDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the user for a row in the database
     * @param user_token The new usertoken contents
     * @param user_id The new userid contents
     *
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOneUser(int user_token, String user_id) {
        int res = -1;
        try {
            uUpdateOne.setInt(1, user_token);
            uUpdateOne.setString(2, user_id);
            res = uUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

     /**
     * Update the user for a row in the database
     * @param user_token The new usertoken contents
     * @param user_id The new userid contents
     *
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOneUserComment(String comment, String user_id) {
        int res = -1;
        try {
            uUpdateOneComment.setString(2, user_id);
            uUpdateOneComment.setString(1, comment);
            res = uUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create userData.  If it already exists, this will print an error
     */
    void createTableUser() {
        try {
            uCreateTable.execute();
        } catch (SQLException e) {
            System.out.println("Error: Table already exists");
            e.printStackTrace();
        }
    }

    /**
     * Remove userData from the database.  If it does not exist, this will print
     * an error.
     */
    void dropTableUser() {
        try {
            uDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Comments Table Methods

    /**
     * Insert a row into the database
     * 
     * @param user_id The user id for this new row
     * @param msg_id The message id for this new row
     * @param body The body for this new row
     * @return The number of rows that were inserted
     */
    int insertRowComment(String user_id, int msg_id, String body) {
        int count = 0;
        try {
            if ((user_id != null && !user_id.isEmpty()) && (body != null && !body.isEmpty()) && (msg_id != 0)) {
                cInsertOne.setString(1, user_id);
                cInsertOne.setInt(2, msg_id);
                cInsertOne.setString(3, body);
                count += cInsertOne.executeUpdate();
            } else {
                System.out.println("ERROR: body or ids are null.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all comments
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowDataComment> selectAllComment() {
        ArrayList<RowDataComment> res = new ArrayList<RowDataComment>();
        try {
            ResultSet rs = cSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowDataComment(rs.getInt("id"), 
                                    rs.getString("user_id"), 
                                    rs.getInt("msg_id"), 
                                    rs.getString("body")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of all comments
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowDataComment> selectAllUserComment(String user_id) {
        ArrayList<RowDataComment> res = new ArrayList<RowDataComment>();
        try {
            cSelectAllUser.setString(1, user_id);
            ResultSet rs = cSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowDataComment(rs.getInt("id"), 
                                    rs.getString("user_id"), 
                                    rs.getInt("msg_id"), 
                                    rs.getString("body")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of all comments
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowDataComment> selectAllCommentMsg(int msg_id) {
        ArrayList<RowDataComment> res = new ArrayList<RowDataComment>();
        try {
            cSelectAllUser.setInt(1, msg_id);
            ResultSet rs = cSelectAllMsg.executeQuery();
            while (rs.next()) {
                res.add(new RowDataComment(rs.getInt("id"), 
                                    rs.getString("user_id"), 
                                    rs.getInt("msg_id"), 
                                    rs.getString("body")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * @param user_id the user id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataComment selectOneUserComment(int id, String user_id) {
        RowDataComment res = null;
        try {
            cSelectOneUser.setInt(1, id);
            cSelectOneUser.setString(2, user_id);
            ResultSet rs = cSelectOneUser.executeQuery();
            if (rs.next()) {
                res = new RowDataComment(rs.getInt("id"), 
                                  rs.getString("user_id"), 
                                  rs.getInt("msg_id"), 
                                  rs.getString("body")); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataComment selectOneComment(int id) {
        RowDataComment res = null;
        try {
            cSelectOne.setInt(1, id);
            ResultSet rs = cSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowDataComment(rs.getInt("id"), 
                                  rs.getString("user_id"), 
                                  rs.getInt("msg_id"), 
                                  rs.getString("body")); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRowComment(int id) {
        int res = -1;
        try {
            cDeleteOne.setInt(1, id);
            res =cDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRowCommentUser(int id, String user_id) {
        int res = -1;
        try {
            cDeleteOneUser.setInt(1, id);
            cDeleteOneUser.setString(2, user_id);
            res =cDeleteOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the comment for a row in the database
     * 
     * @param id The id of the row to update
     * @param body The new body contents
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOneComment(int id, String body) {
        int res = -1;
        try {
            uUpdateOne.setString(1, body);
            uUpdateOne.setInt(2, id);
            res = cUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the comment for a row in the database
     * 
     * @param id The id of the row to update
     * @param body The new body contents
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOneCommentUser(int id, String body, String user_id) {
        int res = -1;
        try {
            cUpdateOneUser.setString(1, body);
            cUpdateOneUser.setInt(2, id);
            cUpdateOneUser.setString(3, user_id);
            res = cUpdateOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create commentData.  If it already exists, this will print an error
     */
    void createTableComment() {
        try {
            cCreateTable.execute();
        } catch (SQLException e) {
            System.out.println("Error: Table already exists");
            e.printStackTrace();
        }
    }

    /**
     * Remove commentData from the database.  If it does not exist, this will print
     * an error.
     */
    void dropTableComment() {
        try {
            cDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //UpVotes Table Methods
    /**
     * Insert a row into the database
     * 
     * @param user_id The user id for this new row
     * @param msg_id The message id for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRowUpVotes(String user_id, int msg_id) {
        int count = 0;
        try {
            if ((user_id != null && !user_id.isEmpty()) && (msg_id != 0)) {
                upInsertOne.setString(1, user_id);
                upInsertOne.setInt(2, msg_id);
                count += upInsertOne.executeUpdate();
            } else {
                System.out.println("ERROR: ids are null.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all likes
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowDataUpVote> selectAllUpVotes() {
        ArrayList<RowDataUpVote> res = new ArrayList<RowDataUpVote>();
        try {
            ResultSet rs = upSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowDataUpVote(rs.getInt("id"), 
                                    rs.getString("user_id"), 
                                    rs.getInt("msg_id")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of all likes
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowDataUpVote> selectAllUserUpVotes(String user_id) {
        ArrayList<RowDataUpVote> res = new ArrayList<RowDataUpVote>();
        try {
            upSelectAllUser.setString(1, user_id);
            ResultSet rs = upSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowDataUpVote(rs.getInt("id"), 
                                    rs.getString("user_id"), 
                                    rs.getInt("msg_id")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * @param user_id the user id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataUpVote selectOneUserUpVote(int id, String user_id) {
        RowDataUpVote res = null;
        try {
            upSelectOne.setInt(1, id);
            upSelectOneUser.setString(2, user_id);
            ResultSet rs = upSelectOneUser.executeQuery();
            if (rs.next()) {
                res = new RowDataUpVote(rs.getInt("id"), 
                                  rs.getString("user_id"), 
                                  rs.getInt("msg_id")); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataUpVote selectOneUpVote(int id) {
        RowDataUpVote res = null;
        try {
            upSelectOne.setInt(1, id);
            ResultSet rs = upSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowDataUpVote(rs.getInt("id"), 
                                  rs.getString("user_id"), 
                                  rs.getInt("msg_id")); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRowUpVote(int id) {
        int res = -1;
        try {
            upDeleteOne.setInt(1, id);
            res = upDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    
    /**
     * Create upvote table.  If it already exists, this will print an error
     */
    void createTableUpVote() {
        try {
            upCreateTable.execute();
        } catch (SQLException e) {
            System.out.println("Error: Table already exists");
            e.printStackTrace();
        }
    }

    /**
     * Remove upvote table from the database.  If it does not exist, this will print
     * an error.
     */
    void dropTableUpVote() {
        try {
            upDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //DownVotes Table Methods
    /**
     * Insert a row into the database
     * 
     * @param user_id The user id for this new row
     * @param msg_id The message id for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRowDownVotes(String user_id, int msg_id) {
        int count = 0;
        try {
            if ((user_id != null && !user_id.isEmpty()) && (msg_id != 0)) {
                dInsertOne.setString(1, user_id);
                dInsertOne.setInt(2, msg_id);
                count += dInsertOne.executeUpdate();
            } else {
                System.out.println("ERROR: ids are null.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all likes
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowDataDownVote> selectAllDownVotes() {
        ArrayList<RowDataDownVote> res = new ArrayList<RowDataDownVote>();
        try {
            ResultSet rs = dSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowDataDownVote(rs.getInt("id"), 
                                    rs.getString("user_id"), 
                                    rs.getInt("msg_id")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of all likes
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowDataDownVote> selectAllUserDownVotes(String user_id) {
        ArrayList<RowDataDownVote> res = new ArrayList<RowDataDownVote>();
        try {
            dSelectAllUser.setString(1, user_id);
            ResultSet rs = dSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowDataDownVote(rs.getInt("id"), 
                                    rs.getString("user_id"), 
                                    rs.getInt("msg_id")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * @param user_id the user id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataDownVote selectOneUserDownVote(int id, String user_id) {
        RowDataDownVote res = null;
        try {
            dSelectOne.setInt(1, id);
            dSelectOneUser.setString(2, user_id);
            ResultSet rs = dSelectOneUser.executeQuery();
            if (rs.next()) {
                res = new RowDataDownVote(rs.getInt("id"), 
                                  rs.getString("user_id"), 
                                  rs.getInt("msg_id")); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataDownVote selectOneDownVote(int id) {
        RowDataDownVote res = null;
        try {
            dSelectOne.setInt(1, id);
            ResultSet rs = dSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowDataDownVote(rs.getInt("id"), 
                                  rs.getString("user_id"), 
                                  rs.getInt("msg_id")); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRowDownVote(int id) {
        int res = -1;
        try {
            dDeleteOne.setInt(1, id);
            res = dDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    
    /**
     * Create upvote table.  If it already exists, this will print an error
     */
    void createTableDownVote() {
        try {
            dCreateTable.execute();
        } catch (SQLException e) {
            System.out.println("Error: Table already exists");
            e.printStackTrace();
        }
    }

    /**
     * Remove upvote table from the database.  If it does not exist, this will print
     * an error.
     */
    void dropTableDownVote() {
        try {
            dDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /*
     * oauth
     */
    int weblogin(String idTokenString) {        
        /*https://stackoverflow.com/questions/37172082/android-what-is-transport-and-jsonfactory-in-googleidtokenverifier-builder*/
        String redirectString = "https://young-springs-75062.herokuapp.com";
        String googleClientID = "866901746692-rf781eb45qt63ad1rk0oke2unhfrk6mq.apps.googleusercontent.com";
        String googleClientSecret = "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";
        String authURL = "https://accounts.google.com/o/oauth2/auth";
        String tokenURL = "https://oauth2.googleapis.com/token";
        String projectId = "cse-216-328017";

        //params:
        String state = ""; //random hash
        String responseType = "code";
        String scope = "openid email";

        //redirect to authURL with params

        int result = -1;
        try{
            //https://developers.google.com/identity/sign-in/web/backend-auth
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
            // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(googleClientID))
                .build();
            
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // Get user identifier
                String userId = payload.getSubject();

                // Get profile information from payload
                String firstname = (String) payload.get("given_name");
                String lastname = (String) payload.get("family_name");
                String email = (String) payload.get("email");
                //String usertoken = idToken;
                //https://www.tutorialspoint.com/obtain-the-hash-code-for-a-string-in-java
                //usertoken.hashCode()
                Random randNum = new Random();
                int tokenid = randNum.nextInt(10000);
                result = tokenid;
                insertRowUser(firstname, lastname, email, "", tokenid, userId);
                //check if user exists
                /* if(selectOneUser(userId) != null)
                {
                    try{
                        int tokenid = randNum.nextInt(10000);
                        updateOneUser(tokenid, userId);
                        result = tokenid;
                    } catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                    
                }
                else 
                {
                    try {
                        int tokenid = randNum.nextInt(10000);
                        insertRowUser(firstname, lastname, email, tokenid, userId);
                        result = tokenid;
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                } */
            } else {
                System.out.println("Invalid ID token.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}