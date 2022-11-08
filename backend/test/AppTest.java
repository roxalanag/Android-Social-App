package cse216.group4.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for App.java
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testBlankSubject()
    {
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
        String id = "abc";
        int res = db.insertRow(subject, message, id);
        assertTrue(res == 0);
    }

    public void testBlankMessage(){
        //connect to heroku database
        String db_url = "postgres://xxisiesawjprsa:6b0211e1b28fbde0e7de3ab1e74c05c127621ffc311ef4c9c1fe43942be5bef6@ec2-3-220-214-162.compute-1.amazonaws.com:5432/ddr5lh5mvpujgi";

        // Get a fully-configured connection to the database, or exit 
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;
        
        //test if no message is entered
        String subject = "Test";
        String message = "";
        String id = "abc";
        int res = db.insertRow(subject, message, id);
        assertTrue(res == 0);
    }

    public void testInvalidRowID(){
        //connect to heroku database
        String db_url = "postgres://xxisiesawjprsa:6b0211e1b28fbde0e7de3ab1e74c05c127621ffc311ef4c9c1fe43942be5bef6@ec2-3-220-214-162.compute-1.amazonaws.com:5432/ddr5lh5mvpujgi";

        // Get a fully-configured connection to the database, or exit 
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;
        
        int id = 2000;
        int res = db.deleteRow(id);
        assertTrue(res == 0);
        //System.out.println("  " + res + " rows deleted");
    }

    public void testRowDataConstructor() {
        Database.RowData test = new Database.RowData(1, "subject", "Here is a test message", 3, 7, "user_id");
        assertTrue(test.mId == 1);
        assertTrue(test.mSubject.equals("subject"));
        assertTrue(test.mMessage.equals("Here is a test message"));
        assertTrue(test.mLikes == 3);
        assertTrue(test.mDislikes == 7);
        assertTrue(test.mUserId.equals("user_id"));
    }
    public void testOauthInvalidToken()
    {
        //String token = "ya29.a0ARrdaM95xpldKXtVz2-Crrktbhc90qn2YDBaaDnZ7t7xUrCoENTPz0Jnu58D36uEXvBT0n5_74HA8y3ABtv-Gehk5iTsA6_mLaLMc-R-IizOLmLFdewawO0RHNlgK88kwV_xr0kn5AfIALaGO6D55ZtxYknQ";
        //connect to heroku database
        String db_url = "postgres://xxisiesawjprsa:6b0211e1b28fbde0e7de3ab1e74c05c127621ffc311ef4c9c1fe43942be5bef6@ec2-3-220-214-162.compute-1.amazonaws.com:5432/ddr5lh5mvpujgi";
        // Get a fully-configured connection to the database, or exit 
        // immediately
        Database db = Database.getDatabase(db_url);

        int result = db.weblogin("eyJhbGciOiJSUzI1NiIsImtpZCI6ImFkZDhjMGVlNjIzOTU0NGFmNTNmOTM3MTJhNTdiMmUyNmY5NDMzNTIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXpwIjoiODY2OTAxNzQ2NjkyLXJmNzgxZWI0NXF0NjNhZDFyazBva2UydW5oZnJrNm1xLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiODY2OTAxNzQ2NjkyLXJmNzgxZWI0NXF0NjNhZDFyazBva2UydW5oZnJrNm1xLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTE0MDU1MjU2OTA0NTM4Mjc3OTUyIiwiaGQiOiJsZWhpZ2guZWR1IiwiZW1haWwiOiJjbXcyMjNAbGVoaWdoLmVkdSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiR0ZuLXpweUptdXc5WFozWFdUVExQZyIsIm5hbWUiOiJDYWl0bHluIFdhZ25lciIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQVRYQUp4Zm1fMmJaLUd5MGpGQmFMMVNlRkJPRlFqeWFLLUNmQjZVR2dHVT1zOTYtYyIsImdpdmVuX25hbWUiOiJDYWl0bHluIiwiZmFtaWx5X25hbWUiOiJXYWduZXIiLCJsb2NhbGUiOiJlbiIsImlhdCI6MTYzNDc2NzU3MSwiZXhwIjoxNjM0NzcxMTcxLCJqdGkiOiJiYWU3NGNkMzVkN2MxZGNjZDExMjJhMGVlNWM4ZDM3YTZkOGE0ODAwIn0.PSUCGHLKjP2Ute2Qa-FGyzG7ntiwQIBZiN7UG3zNQhf3FGSV0KqtepZxqA0tF0i8hsW9NIQ20GOKY1MQUyiKTlcLCyKpdfrgacNr3LdvCCuDMrKTYx4_ylWRi_r3BAXpfVUv4j0tCXO48TwNNL3lyblPK5kfEKzUA_UtV2gBgqAjJqRBbDdGXqzU_WaQfZBGbbeS4xAlmT34G61TvSRkQ4zKNavZFs5eSThQCxu1jE38wuaq4J0FYWmZVU29IUzkuO58oWGbJUJDz1B_uprbsHN-8WAqk3w5mHnxQY5VuT8wFsic8Aw9G_nrBfHC0089XUtU31CQesaDpyntakzjVQ");
        assertTrue(result == -1);

    }
    public void testOauth()
    {
        //String token = "ya29.a0ARrdaM95xpldKXtVz2-Crrktbhc90qn2YDBaaDnZ7t7xUrCoENTPz0Jnu58D36uEXvBT0n5_74HA8y3ABtv-Gehk5iTsA6_mLaLMc-R-IizOLmLFdewawO0RHNlgK88kwV_xr0kn5AfIALaGO6D55ZtxYknQ";
        //connect to heroku database
        /*String db_url = "postgres://xxisiesawjprsa:6b0211e1b28fbde0e7de3ab1e74c05c127621ffc311ef4c9c1fe43942be5bef6@ec2-3-220-214-162.compute-1.amazonaws.com:5432/ddr5lh5mvpujgi";
        String token = "123";
        // Get a fully-configured connection to the database, or exit 
        // immediately
        Database db = Database.getDatabase(db_url);

        int result = db.weblogin(token);
        assertTrue(result > 0);*/

    }
}

