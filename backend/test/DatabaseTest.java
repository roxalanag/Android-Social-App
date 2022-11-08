package cse216.group4.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class DatabaseTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DatabaseTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DatabaseTest.class);
    }

    /**
     * Ensure that getDatabase executes correctly
     */
    public void testConstructor() {
        String db_url = "test url";
        Database db = Database.getDatabase(db_url);

        //assertTrue(db.mConnection.equals(db_url));
    }
}