package cse216.group4.backend;

/**
 * UserRequest provides a format for clients to present user information to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class UserRequest {
    /**
     * User first name
     */
    public String uFirstName;

    /**
     * User last name
     */
    public String uLastName;

    /**
     * User email
     */
    public String uEmail;
    
    /**
     * User token
     */
    public int uToken;

    /**
     * User id
     */
    public String uUserId;

    public String uBackToken;

}