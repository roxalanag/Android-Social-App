# Back-End Server 

Technical Debt:

    Database.java
        - using constants to create prepared statements 
        - create like table 
            - foreign key references messages table 
            - foreign key references user table 
        - create dislike table 
            - foreign key references messages table 
            - foreign key references user table 
         
May Need For Future Phases:

    Database.java

        1. user ID of the person writing a message --> in RowData
        2. user ID of the person liking/disliking a message  --> in likeTbl
        3. relational table for user ID and like/dislike ID 

    App.java

        1. route for validating user ID 

    Testing 

        1. unit tests for user IDs

Works Cited:
https://stackoverflow.com/questions/2601978/how-to-check-if-my-string-is-equal-to-null
