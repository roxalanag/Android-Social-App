package cse216.group4.backend;

/**
 * SimpleRequest provides a format for clients to present Subject and message 
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class SimpleRequest {
    /**
     * The Subject being provided by the client.
     */
    public String mSubject;

    /**
     * The message being provided by the client.
     */
    public String mMessage;

    /**
     * Number of likes
     */
    public int mLikes;

    /**
     * Number of Dislikes
     */
    public int mDislikes;
    
    /**
     * Number of msg id
     */
    public int cMsgId;

    /**
     * Comment Body
     */
    public String cBody;

}