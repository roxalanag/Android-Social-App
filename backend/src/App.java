package cse216.group4.backend;

/**
 * Import the Spark package, so that we can make use of the API endpoints:
 * GET, POST, DELETE, PUT
 */
import spark.Spark;
import java.util.Random;

/**
 * Import Google's JSON library
 * Map is used to get enviroment variables
 */
import com.google.gson.*;
import java.util.Map;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {
    public static void main(String[] args) {

        /**
         * gson provides us with a way to turn JSON into objects, and objects
         * into JSON.
         * 
         * NB: it must be final, so that it can be accessed from our lambdas
         * 
         * NB: Gson is thread-safe.  See 
         * https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
         */
        final Gson gson = new Gson();

        // get the Postgres configuration from the environment
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        
        // Get the port on which to listen for requests
        Spark.port(getIntFromEnv("PORT", 4567));

        // Get a fully-configured connection to the database, or exit 
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null){
            System.out.println("Error: unable to connect to database");
            return;
        }
    
        // Set up the location for serving static files.  If the STATIC_LOCATION
        // environment variable is set, we will serve from it.  Otherwise, serve
        // from "/web"
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        String cors_enabled = env.get("CORS_ENABLED");
        if (cors_enabled.equals("TRUE")) {
            final String acceptCrossOriginRequestsFrom = "*";
            final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
            final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
            enableCORS(acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
        }

        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        }); 

        /**
         * GET route that returns all message, Subjects, Ids, and likes.  All we do is get 
         * the data, embed it in a StructuredResponse, turn it into JSON, and 
         * return it.  If there's no data, we return "[]", so there's no need
         * for error handling. 
         */
        Spark.get("/messages", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAll()));
        });


        /** GET route that returns everything for a single row in the DataStore.
         * The ":id" suffix in the first parameter to get() becomes 
         * request.params("id"), so that we can get the requested row ID.  If 
         * ":id" isn't a number, Spark will reply with a status 500 Internal
         * Server Error.  Otherwise, we have an integer, and the only possible 
         * error is that it doesn't correspond to a row with data.
         */
        Spark.get("/messages/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Database.RowData data = db.selectOne(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

         /** GET route that returns everything for a single row in the DataStore.
         * The ":id" suffix in the first parameter to get() becomes 
         * request.params("id"), so that we can get the requested row ID.  If 
         * ":id" isn't a number, Spark will reply with a status 500 Internal
         * Server Error.  Otherwise, we have an integer, and the only possible 
         * error is that it doesn't correspond to a row with data.
         */
        Spark.get("/messages/:id/:userid", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Database.RowData data = db.selectOneUserMessage(idx, useridx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

         /** GET route that returns everything for a single row in the DataStore.
         * The ":id" suffix in the first parameter to get() becomes 
         * request.params("id"), so that we can get the requested row ID.  If 
         * ":id" isn't a number, Spark will reply with a status 500 Internal
         * Server Error.  Otherwise, we have an integer, and the only possible 
         * error is that it doesn't correspond to a row with data.
         */
        Spark.get("/messages/:userid", (request, response) -> {
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllUserMessages(useridx)));
        
        });
        

        /** POST route for adding a new element to the Database.  This will read
         * JSON from the body of the request, turn it into a SimpleRequest 
         * object, extract the Subject and message, insert them, and return the 
         * ID of the newly created row.
        */ 
        Spark.post("/messages", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            UserRequest ureq = gson.fromJson(request.body(), UserRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            // NB: insertRow checks for null Subject and message
            int newId = db.insertRow(req.mSubject, req.mMessage, ureq.uUserId);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        /**
         * PUT route for liking a message
         * users can like messages by sending a JSON with the id of the row in which likes is to be incremented
         * increments like by 1 for the message with id = idx
         **/
        Spark.put("/messages/:id/like", (request, response) -> {
            //If we can't get an ID or can't parse the JSON, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            gson.fromJson(request.body(), SimpleRequest.class);
            //ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.like(idx); 
            return gson.toJson(new StructuredResponse("ok", null, result));
        });

        /**
         * PUT route for unliking a message
         * users can like messages by sending a JSON with the id of the row in which likes is to be deincremented
         * deincrements likes by 1 for the message with id = idx
         **/
        Spark.put("/messages/:id/unlike", (request, response) -> {
            //If we can't get an ID or can't parse the JSON, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            gson.fromJson(request.body(), SimpleRequest.class);
            //ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.unLike(idx); 
            return gson.toJson(new StructuredResponse("ok", null, result));
        });

        /**
         *  PUT route for disliking a message.  
         *  users can dislike messages by sending a JSON with the id of the row in which dislikes is to be incremented
         *  increments dislikes by 1 for message with id = idx
         */
        Spark.put("/messages/:id/dislike", (request, response) -> {
            //If we can't get an ID or can't parse the JSON, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            gson.fromJson(request.body(), SimpleRequest.class);
            //ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.dislike(idx); 
            return gson.toJson(new StructuredResponse("ok", null, result));
        });

        /**
         *  PUT route for undisliking a message.  
         *  users can undislike messages by sending a JSON with the id of the row in which dislikes is to be deincremented
         *  deincrements dislikes by 1 for message with id = idx
         */
        Spark.put("/messages/:id/undislike", (request, response) -> {
            //If we can't get an ID or can't parse the JSON, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            gson.fromJson(request.body(), SimpleRequest.class);
            //ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.unDislike(idx); 
            return gson.toJson(new StructuredResponse("ok", null, result));
        });

        // PUT route for updating a row in the DataStore.  This is almost exactly the same as POST
        Spark.put("/messages/:id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.updateOne(idx, req.mSubject, req.mMessage);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        // PUT route for updating a row in the DataStore.  This is almost exactly the same as POST
        Spark.put("/messages/:id/:userid", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.updateOneUserMessage(idx, useridx, req.mSubject, req.mMessage);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        // DELETE route for removing a row from the DataStore
        Spark.delete("/messages/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            int result = db.deleteRow(idx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });
        
        // DELETE route for removing a row from the DataStore
        Spark.delete("/messages/:id/:userid", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            int result = db.deleteRowUserMessage(idx, useridx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });
        


        //Users
        /**
         * GET route that returns all users.  All we do is get 
         * the data, embed it in a StructuredResponse, turn it into JSON, and 
         * return it.  If there's no data, we return "[]", so there's no need
         * for error handling. 
         */
        Spark.get("/users", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllUser()));
        });

        /** GET route that returns everything for a single row in the DataStore.
         * The ":id" suffix in the first parameter to get() becomes 
         * request.params("id"), so that we can get the requested row ID.  If 
         * ":id" isn't a number, Spark will reply with a status 500 Internal
         * Server Error.  Otherwise, we have an integer, and the only possible 
         * error is that it doesn't correspond to a row with data.
         */
        Spark.get("/users/:userid", (request, response) -> {
            //int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            String useridx = request.params("userid");
            response.status(200);
            response.type("application/json");
            Database.RowDataUser data = db.selectOneUser(useridx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", useridx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        /** POST route for adding a new element to the Database.  This will read
         * JSON from the body of the request, turn it into a UserRequest 
         * object, extract the Subject and message, insert them, and return the 
         * ID of the newly created row.
        */ 
        Spark.post("/auth/weblogin", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            UserRequest req = gson.fromJson(request.body(), UserRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            // NB: insertRow checks for null Subject and message
            int newId = db.weblogin(req.uBackToken);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

         // PUT route for updating a row in the DataStore.  This is almost exactly the same as POST
         Spark.put("/auth/logout/:userid", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            //int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.updateOneUser(0, useridx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + useridx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        // PUT route for updating a row in the DataStore.  This is almost exactly the same as POST
        Spark.put("/auth/new/session/:userid", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            //int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Random randNum = new Random();
            int tokenid = randNum.nextInt(10000);
            int result = db.updateOneUser(tokenid, useridx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + useridx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        // PUT route for updating a row in the DataStore.  This is almost exactly the same as POST
        Spark.put("/users/:userid", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            //int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            UserRequest req = gson.fromJson(request.body(), UserRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.updateOneUser(req.uToken, useridx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + useridx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

         // PUT route for updating a row in the DataStore.  This is almost exactly the same as POST
         Spark.put("/users/usercomment/:userid", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            //int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            UserRequest req = gson.fromJson(request.body(), UserRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.updateOneUserComment(req.uEmail, useridx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + useridx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        // DELETE route for removing a row from the DataStore
        Spark.delete("/users/:userid", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            //int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            int result = db.deleteRowUser(useridx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + useridx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });


        //Comments
        /**
         * GET route that returns all comments.  All we do is get 
         * the data, embed it in a StructuredResponse, turn it into JSON, and 
         * return it.  If there's no data, we return "[]", so there's no need
         * for error handling. 
         */
        Spark.get("/comments", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllComment()));
        });

        
        Spark.get("/comments/msg/:msgid", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            int idx = Integer.parseInt(request.params("msgid"));
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllCommentMsg(idx)));
        });

        /** GET route that returns everything for a single row in the DataStore.
         * The ":id" suffix in the first parameter to get() becomes 
         * request.params("id"), so that we can get the requested row ID.  If 
         * ":id" isn't a number, Spark will reply with a status 500 Internal
         * Server Error.  Otherwise, we have an integer, and the only possible 
         * error is that it doesn't correspond to a row with data.
         */
        Spark.get("/comments/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Database.RowDataComment data = db.selectOneComment(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        /** GET route 
         */
        Spark.get("/comments/:id/:userid", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Database.RowDataComment data = db.selectOneUserComment(idx, useridx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        /**
         * GET route that returns all comments for a user 
         */
        Spark.get("/comments/:userid", (request, response) -> {
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllUserComment(useridx)));
        });

        /** POST route for adding a new element to the Database.  This will read
         * JSON from the body of the request, turn it into a SimpleRequest 
         * object, extract the Subject and message, insert them, and return the 
         * ID of the newly created row.
        */ 
        Spark.post("/comments", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            SimpleRequest sreq = gson.fromJson(request.body(), SimpleRequest.class);
            UserRequest req = gson.fromJson(request.body(), UserRequest.class);

            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            // NB: insertRow checks for null Subject and message
            int newId = db.insertRowComment(req.uUserId, sreq.cMsgId, sreq.cBody);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });


        // PUT route for updating a row in the DataStore.  This is almost exactly the same as POST
        Spark.put("/comments/:id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.updateOneComment(idx, req.cBody);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        // PUT route for updating a row in the DataStore.  This is almost exactly the same as POST
        Spark.put("/comments/:id/:userid", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.updateOneCommentUser(idx, req.cBody, useridx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        // DELETE route for removing a row from the DataStore
        Spark.delete("/comments/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            int result = db.deleteRowComment(idx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });
        
         // DELETE route for removing a row from the DataStore
         Spark.delete("/comments/:id/:userid", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            int result = db.deleteRowCommentUser(idx, useridx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });
    

        //UpVotes
        /**
         * GET route that returns all upvotes.  All we do is get 
         * the data, embed it in a StructuredResponse, turn it into JSON, and 
         * return it.  If there's no data, we return "[]", so there's no need
         * for error handling. 
         */
        Spark.get("/upvotes", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllUpVotes()));
        });

        /** GET route that returns everything for a single row in the DataStore.
         * The ":id" suffix in the first parameter to get() becomes 
         * request.params("id"), so that we can get the requested row ID.  If 
         * ":id" isn't a number, Spark will reply with a status 500 Internal
         * Server Error.  Otherwise, we have an integer, and the only possible 
         * error is that it doesn't correspond to a row with data.
         */
        Spark.get("/upvotes/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Database.RowDataUpVote data = db.selectOneUpVote(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        /** GET route 
         */
        Spark.get("/upvotes/:id/:userid", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Database.RowDataUpVote data = db.selectOneUserUpVote(idx, useridx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        /**
         * GET route that returns all comments for a user 
         */
        Spark.get("/upvotes/:userid", (request, response) -> {
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllUserUpVotes(useridx)));
        });

        /** POST route for adding a new element to the Database.  This will read
         * JSON from the body of the request, turn it into a SimpleRequest 
         * object, extract the Subject and message, insert them, and return the 
         * ID of the newly created row.
        */ 
        Spark.post("/upvotes", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            SimpleRequest sreq = gson.fromJson(request.body(), SimpleRequest.class);
            UserRequest req = gson.fromJson(request.body(), UserRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            // NB: insertRow checks for null Subject and message
            int newId = db.insertRowUpVotes(req.uUserId, sreq.cMsgId);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // DELETE route for removing a row from the DataStore
        Spark.delete("/upvotes/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            int result = db.deleteRowUpVote(idx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        //Down Votes
        /**
         * GET route that returns all down votes.  All we do is get 
         * the data, embed it in a StructuredResponse, turn it into JSON, and 
         * return it.  If there's no data, we return "[]", so there's no need
         * for error handling. 
         */
        Spark.get("/downvotes", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllDownVotes()));
        });

        /** GET route that returns everything for a single row in the DataStore.
         * The ":id" suffix in the first parameter to get() becomes 
         * request.params("id"), so that we can get the requested row ID.  If 
         * ":id" isn't a number, Spark will reply with a status 500 Internal
         * Server Error.  Otherwise, we have an integer, and the only possible 
         * error is that it doesn't correspond to a row with data.
         */
        Spark.get("/downvotes/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Database.RowDataDownVote data = db.selectOneDownVote(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        /** GET route 
         */
        Spark.get("/downvotes/:id/:userid", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Database.RowDataDownVote data = db.selectOneUserDownVote(idx, useridx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        /**
         * GET route that returns all comments for a user 
         */
        Spark.get("/downvotes/:userid", (request, response) -> {
            String useridx = request.params("userid");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAllUserDownVotes(useridx)));
        });

        /** POST route for adding a new element to the Database.  This will read
         * JSON from the body of the request, turn it into a SimpleRequest 
         * object, extract the Subject and message, insert them, and return the 
         * ID of the newly created row.
        */ 
        Spark.post("/downvotes", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            UserRequest req = gson.fromJson(request.body(), UserRequest.class);
            SimpleRequest sreq = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            // NB: insertRow checks for null Subject and message
            int newId = db.insertRowDownVotes(req.uUserId, sreq.cMsgId);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // DELETE route for removing a row from the DataStore
        Spark.delete("/downvotes/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            int result = db.deleteRowDownVote(idx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });
    
    
    
    
    }

    /**
     * Set up CORS headers for the OPTIONS verb, and for every response that the
     * server sends.  This only needs to be called once.
     * 
     * @param origin The server that is allowed to send requests to this server
     * @param methods The allowed HTTP verbs from the above origin
     * @param headers The headers that can be sent with a request from the above
     *                origin
     */
    private static void enableCORS(String origin, String methods, String headers) {
        // Create an OPTIONS route that reports the allowed CORS headers and methods
        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });


        /**
         * 'before' is a decorator, which will run before any 
         *  get/post/put/delete.  
         *  it will put three extra CORS headers into the response
         */
        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
    }

    /**
     * Get an integer environment varible if it exists, and otherwise return the
     * default value.
     * 
     * @envar      The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }
}