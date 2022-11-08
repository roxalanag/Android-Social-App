// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an erro
var $: any;

// The 'this' keyword does not behave in JavaScript/TypeScript like it does in
// Java.  Since there is only one NewEntryForm, we will save it to a global, so
// that we can reference it from methods of the NewEntryForm in situations where
// 'this' won't work correctly.
var newEntryForm: NewEntryForm;

/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
class NewEntryForm {
    /**
     * To initialize the object, we say what method of NewEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    constructor() {
        $("#addCancel").click(this.clearForm);
        $("#addButton").click(this.submitForm);
    }

    /**
     * Clear the form's input fields
     */
    clearForm() {
         $("#newTitle").val("");
         $("#newMessage").val("");
        // reset the UI
        $("#addElement").hide();
        $("#editElement").hide();
        $("#showElements").show();
    }

    /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    submitForm() {
        console.log("testing button click");
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let title = "" + $("#newTitle").val();
        let msg = "" + $("#newMessage").val();
        if (title === "" || msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: "/messages",
            dataType: "json",
            data: JSON.stringify({ mSubject: title, mMessage: msg }),
            success: newEntryForm.onSubmitResponse
        });
    }

    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a 
     * result.
     * 
     * @param data The object returned by the server
     */
    private onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form
        if (data.mStatus === "ok") {
            newEntryForm.clearForm();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }
} // end class NewEntryForm












// The 'this' keyword does not behave in JavaScript/TypeScript like it does in
// Java.  Since there is only one NewEntryForm, we will save it to a global, so
// that we can reference it from methods of the NewEntryForm in situations where
// 'this' won't work correctly.
var newComment: NewComment;

/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
class NewComment {
    /**
     * To initialize the object, we say what method of NewEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    constructor() {
        $("#addComCancel").click(this.clearComment);
        $("#addComButton").click(this.submitComment);
    }

    /**
     * Clear the form's input fields
     */
    clearComment() {
        $("#newComment").val("");
        // reset the UI
        $("#addComment").hide();
        $("#editComment").hide();
        $("#showElements").show();
    }

    /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    submitComment() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let comment = "" + $("#newComment").val();
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: "/comments",
            dataType: "json",
            data: JSON.stringify({ cUserId: "jhp222", cMsgId: 2, cBody: comment }),
            success: newComment.onSubmitResponse
        });
    }

    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a 
     * result.
     * 
     * @param data The object returned by the server
     */
    onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form
        if (data.mStatus === "ok") {
            newComment.clearComment();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:");
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }
} // end class new comment











// a global for the main ElementList of the program.  See newEntryForm for 
// explanation
var mainList: ElementList;


/**
 * ElementList provides a way of seeing all of the data stored on the server.
 */
class ElementList {
    /**
     * refresh is the public method for updating messageList
     */
    refresh() {
        // Issue a GET, and then pass the result to update()
        $.ajax({
            type: "GET",
            url: "/messages",
            dataType: "json",
            success: mainList.update
        });
    }

    /**
     * update is the private method used by refresh() to update messageList
     */
    private update(data: any) {
        $("#messageList").html("<table>");
        for (let i = 0; i < data.mData.length; ++i) {

            $("#messageList").append("<tr><td></td></tr><tr><td></td></tr><tr><td>" + "Message ID: " + data.mData[i].mId +
                                     "</td></tr><tr><td>" + "User ID: " + data.mData[i].mUserId +
                                     "</td></tr><tr><td>" + "Title: " + data.mData[i].mSubject +
                                     "</td></tr><tr><td>" + "Body: " + data.mData[i].mMessage +
                                     "</td></tr><tr><td>" + "Likes: " + data.mData[i].mLikes +
                                     "        Dislikes: " + data.mData[i].mDislikes +
                                     "</td></tr><tr>" + mainList.buttons(data.mData[i].mId) + "</tr>");
        } 
        $("#messageList").append("</table>");


        // Find all of the comment buttons, and set their behavior
        $(".combtn").click(mainList.clickComment);

        // Find all of the delete buttons, and set their behavior
        $(".delbtn").click(mainList.clickDelete);

        // Find all of the Edit buttons, and set their behavior
        $(".editbtn").click(mainList.clickEdit);

        // Find all of the Like buttons, and set their behavior
        $(".likebtn").click(mainList.clickLike);

        // Find all of the DisLike buttons, and set their behavior
        $(".dislikebtn").click(mainList.clickDislike);

        // Find all of the Like buttons, and set their behavior
        $(".unlikebtn").click(mainList.clickUnlike);

        // Find all of the DisLike buttons, and set their behavior
        $(".undislikebtn").click(mainList.clickUndislike);
    }


        /**
     * clickDelete is the code we run in response to a click of a delete button
     */
    private clickComment() {
        let id = $(this).data("value");

        $("#addComment").show();
        $("#showElements").hide();
        $("#googlebut").hide();
        // $("#addComCancel").click(newComment.clearComment);
        // $("#addComButton").click(newComment.submitComment(id));
    }

    /**
     * clickDelete is the code we run in response to a click of a delete button
     */
    private clickDelete() {
        // for now, just print the ID that goes along with the data in the row
        // whose "delete" button was clicked
        let id = $(this).data("value");
        $.ajax({
            type: "DELETE",
            url: "/messages/" + id,
            dataType: "json",
            // TODO: we should really have a function that looks at the return
            //       value and possibly prints an error message.
            success: mainList.refresh
        })
    }

    /**
     * clickEdit is the code we run in response to a click of a delete button
     */
    private clickEdit() {
        // as in clickDelete, we need the ID of the row
        let id = $(this).data("value");
        $.ajax({
            type: "GET",
            url: "/messages/" + id,
            dataType: "json",
            success: editEntryForm.init
        });
    }

    /**
     * clickLike is the code we run in response to a click of a like button
     */
     private clickLike() {
        // as in clickEdit and clickDelete, we need the ID of the row
        let id = $(this).data("value");
        $.ajax({
            type: "PUT",
            url: "/messages/" + id + "/like",
            dataType: "json",
            success: mainList.refresh
        });
    }

    /**
     * clickLike is the code we run in response to a click of a like button
     */
     private clickUnlike() {
        // as in clickEdit and clickDelete, we need the ID of the row
        let id = $(this).data("value");
        $.ajax({
            type: "PUT",
            url: "/messages/" + id + "/unlike",
            dataType: "json",
            success: mainList.refresh
        });
    }

    /**
     * clickDislike is the code we run in response to a click of a dislike button
     */
     private clickDislike() {
        // as in clickLike we need the ID of the row
        let id = $(this).data("value");
        $.ajax({
            type: "PUT",
            url: "/messages/" + id + "/dislike",
            dataType: "json",
            success: mainList.refresh
        });
    }

    /**
     * clickLike is the code we run in response to a click of a like button
     */
     private clickUndislike() {
        // as in clickEdit and clickDelete, we need the ID of the row
        let id = $(this).data("value");
        $.ajax({
            type: "PUT",
            url: "/messages/" + id + "/undislike",
            dataType: "json",
            success: mainList.refresh
        });
    }

    /**
     * buttons() adds a 'delete' button, 'edit' button, 'like' button,
     * 'dislike' button to the HTML for each row
     */
    private buttons(id: string): string {
        return "<td><button class='editbtn' data-value='" + id + "'>Edit</button></td>"
            + "<td><button class='delbtn' data-value='" + id + "'>Delete</button></td>"  
            + "<td><button class='likebtn' data-value='" + id + "'>Like</button></td>"
            + "<td><button class='dislikebtn' data-value='" + id + "'>Dislike</button></td>"
            + "<td><button class='undislikebtn' data-value='" + id + "'>Undislike</button></td>"
            + "<td><button class='unlikebtn' data-value='" + id + "'>Unlike</button></td>"
            + "<td><button class='combtn' data-value='" + id + "'>Comment</button></td>";
    }

    private comment(data: any) {


    }
} // end class ElementList


















// a global for the main ElementList of the program.  See newEntryForm for 
// explanation
var comList: CommentList;


/**
 * ElementList provides a way of seeing all of the data stored on the server.
 */
class CommentList {
    /**
     * refresh is the public method for updating messageList
     */
    refresh() {
        // Issue a GET, and then pass the result to update()
        $.ajax({
            type: "GET",
            url: "/comments",
            dataType: "json",
            success: comList.update
        });
    }

    /**
     * update is the private method used by refresh() to update messageList
     */
    private update(data: any) {
        $("#commentList").html("<table>");
        for (let i = 0; i < data.mData.length; ++i) {

            $("#commentList").append("<tr><td></td></tr><tr><td></td></tr><tr><td>" + "Message ID: " + data.mData[i].cId +
                                     "</td></tr><tr><td>" + "User ID: " + data.mData[i].cUserId +
                                     "</td></tr><tr><td>" + "Comment: " + data.mData[i].cBody +
                                     "</td></tr><tr>" + comList.buttons(data.mData[i].cId) + "</tr>");
        } 
        $("#commentList").append("</table>");

        // Find all of the Edit buttons, and set their behavior
        $(".editbtn").click(comList.clickEdit);

        // Find all of the Edit buttons, and set their behavior
        $(".delbtn").click(comList.clickDelete);
    }

    /**
     * clickEdit is the code we run in response to a click of a delete button
     */
    private clickEdit() {
        // as in clickDelete, we need the ID of the row
        let id = $(this).data("value");
        $.ajax({
            type: "GET",
            url: "/comments/" + id,
            dataType: "json",
            success: editComment.init
        });
    }

    private clickDelete() {
        // for now, just print the ID that goes along with the data in the row
        // whose "delete" button was clicked
        let id = $(this).data("value");
        $.ajax({
            type: "DELETE",
            url: "/comments/" + id,
            dataType: "json",
            success: comList.refresh
        })
    }

    /**
     * buttons() adds a 'delete' button, 'edit' button, 'like' button,
     * 'dislike' button to the HTML for each row
     */
    private buttons(id: string): string {
        return "<td><button class='editbtn' data-value='" + id + "'>Edit</button></td>"
        + "<td><button class='delbtn' data-value='" + id + "'>Delete</button></td>";
    }
} // end class ElementList
















// a global for the EditEntryForm of the program.  See newEntryForm for 
// explanation
var editEntryForm: EditEntryForm;

/**
 * EditEntryForm encapsulates all of the code for the form for editing an entry
 */
class EditEntryForm {
    /**
     * To initialize the object, we say what method of EditEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    constructor() {
        $("#editCancel").click(this.clearForm);
        $("#editButton").click(this.submitForm);
    }

    /**
     * init() is called from an AJAX GET, and should populate the form if and 
     * only if the GET did not have an error
     */
    init(data: any) {
        if (data.mStatus === "ok") {
            $("#editTitle").val(data.mData.mSubject);
            $("#editMessage").val(data.mData.mMessage);
            $("#editId").val(data.mData.mId);
            $("#editCreated").text(data.mData.mCreated);
            // show the edit form
            $("#addElement").hide();
            $("#editElement").show();
            $("#showElements").hide();
        }
        else if (data.mStatus === "error") {
            window.alert("Error: " + data.mMessage);
        }
        else {
            window.alert("An unspecified error occurred");
        }
    }

    /**
     * Clear the form's input fields
     */
    clearForm() {
        $("#editTitle").val("");
        $("#editMessage").val("");
        $("#editId").val("");
        $("#editCreated").text("");
        // reset the UI
        $("#addElement").hide();
        $("#editElement").hide();
        $("#showElements").show();
    }

    /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    submitForm() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let title = "" + $("#editTitle").val();
        let msg = "" + $("#editMessage").val();
        // NB: we assume that the user didn't modify the value of #editId
        let id = "" + $("#editId").val();
        if (title === "" || msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "PUT",
            url: "/messages/" + id,
            dataType: "json",
            data: JSON.stringify({ mSubject: title, mMessage: msg }),
            success: editEntryForm.onSubmitResponse
        });
    }

    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a 
     * result.
     * 
     * @param data The object returned by the server
     */
    private onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form and refresh the main 
        // listing of messages
        if (data.mStatus === "ok") {
            editEntryForm.clearForm();
            mainList.refresh();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }
} // end class EditEntryForm








// a global for the EditEntryForm of the program.  See newEntryForm for 
// explanation
var editComment: EditComment;

/**
 * EditEntryForm encapsulates all of the code for the form for editing an entry
 */
class EditComment {
    /**
     * To initialize the object, we say what method of EditEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    constructor() {
        $("#editComCancel").click(this.clearComment);
        $("#editComButton").click(this.submitComment);
    }

    /**
     * init() is called from an AJAX GET, and should populate the form if and 
     * only if the GET did not have an error
     */
    init(data: any) {
        if (data.mStatus === "ok") {
            $("#editComment").val(data.mData.cBody);
            $("#editId").val(data.mData.cId);
            // show the edit form
            $("#addElement").hide();
            $("#editElement").hide();
            $("#googlebut").hide();
            $("#editComment").show();
            $("#showElements").hide();
        }
        else if (data.mStatus === "error") {
            window.alert("Error: ");
        }
        else {
            window.alert("An unspecified error occurred");
        }
    }

    /**
     * Clear the form's input fields
     */
    clearComment() {
        $("#editComment").val("");
        $("#editId").val("");
        $("#editCreated").text("");
        // reset the UI
        $("#addComment").hide();
        $("#editComment").hide();
        $("#editComment").hide();
        $("#showElements").show();
    }

    /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    submitComment() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let comment = "" + $("#editComment").val();
        // NB: we assume that the user didn't modify the value of #editId
        let id = "" + $("#editId").val();
        if (comment === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "PUT",
            url: "/comments/" + id,
            dataType: "json",
            data: JSON.stringify({ cBody: comment}),
            success: editComment.onSubmitResponse
        });
    }

    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a 
     * result.
     * 
     * @param data The object returned by the server
     */
    private onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form and refresh the main 
        // listing of messages
        if (data.mStatus === "ok") {
            editComment.clearComment();
            mainList.refresh();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:");
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }
} // end class EditEntryForm






// Run some configuration code when the web page loads
$(document).ready(function () {
    
    // Create the object that controls the "New Entry" form
    newEntryForm = new NewEntryForm();

    // Create the object that controls the "New Comment" form
    newComment = new NewComment();

    // Create the object for the main data list, and populate it with data from
    // the server
    mainList = new ElementList();
    mainList.refresh();

    comList = new CommentList();
    comList.refresh();

    // Create the object that controls the "Edit Entry" form
    editEntryForm = new EditEntryForm();

    // Create the object that controls the "Edit Entry" form
    editComment = new EditComment();

    // set up initial UI state
    $("#editElement").hide();
    $("#addElement").hide();

    $("#editComment").hide();
    $("#addComment").hide();

    $("#profile").hide();

    $("#googlebut").show();

    $("#showElements").show();

    // set up the "Add Message" button
    $("#showFormButton").click(function () {
        $("#addElement").show();
        $("#showElements").hide();
        $("#profile").hide();
        $("#googlebut").hide();
        $("#editComment").hide();
        $("#addComment").hide();
    });

    $("#showProfButton").click(function () {
        $("#profile").show();
        $("#showElements").hide();
        $("#googlebut").hide();
    });
});