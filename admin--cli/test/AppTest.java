package cse216.group4.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName ){
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite(){
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testBlankSubject(){
        //connect to heroku database
        String db_url = "postgres://xxisiesawjprsa:6b0211e1b28fbde0e7de3ab1e74c05c127621ffc311ef4c9c1fe43942be5bef6@ec2-3-220-214-162.compute-1.amazonaws.com:5432/ddr5lh5mvpujgi";

        // Get a fully-configured connection to the database, or exit 
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;
        
        //test if no subject is entered
        String subject = "";
        String message = "test";
        int likes = 6;
        int dislikes = 3;
        int res = db.insertRow(subject, message, likes, dislikes);

        //test if not message is entered

    }

    public void testBlankMessage(){
        //connect to heroku database
        String db_url = "postgres://xxisiesawjprsa:6b0211e1b28fbde0e7de3ab1e74c05c127621ffc311ef4c9c1fe43942be5bef6@ec2-3-220-214-162.compute-1.amazonaws.com:5432/ddr5lh5mvpujgi";

        // Get a fully-configured connection to the database, or exit 
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;
        
        //test if no subject is entered
        String subject = "Test";
        String message = "";
        int likes = 9;
        int dislikes = 3;
        int res = db.insertRow(subject, message, likes, dislikes);

        //test if not message is entered

    }

    public void testInvalidRowID(){
        //connect to heroku database
        String db_url = "postgres://xxisiesawjprsa:6b0211e1b28fbde0e7de3ab1e74c05c127621ffc311ef4c9c1fe43942be5bef6@ec2-3-220-214-162.compute-1.amazonaws.com:5432/ddr5lh5mvpujgi";

        // Get a fully-configured connection to the database, or exit 
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;
        
        // int id = 6;
        // int res = db.deleteRow(id);
        // System.out.println("  " + res + " rows deleted");

        //test if not message is entered
    }
}
