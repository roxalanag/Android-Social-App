var describe: any;
var it: any;
var expect: any;
var attr: any;
var $: any;

describe("Tests of basic math functions", function() {
    it("Adding 1 should work", function() {
        var foo = 0;
        foo += 1;
        expect(foo).toEqual(1);
    });

    it("Subtracting 1 should work", function () {
        var foo = 0;
        foo -= 1;
        expect(foo).toEqual(-1);
    });
});

it("UI Test: Add Button Hides Listing", function(){
    // click the button for showing the add button
    $('#showFormButton').click();
    // expect that the add form is not hidden
    expect($("#addElement").attr("style").indexOf("display: none;")).toEqual(-1);
    // expect tha tthe element listing is hidden
    expect($("#showElements").attr("style").indexOf("display: none;")).toEqual(0);
    // reset the UI, so we don't mess up the next test
    $('#addCancel').click();

    // expect that the add form is not hidden
    expect($("#addComment").attr("style").indexOf("display: none;")).toEqual(-1);
    // expect tha tthe element listing is hidden
    expect($("#editComment").attr("style").indexOf("display: none;")).toEqual(0);

    // expect that the add form is not hidden
    expect($("#addComCancel").attr("style").indexOf("display: none;")).toEqual(-1);
    // expect tha tthe element listing is hidden
    expect($("#addComButton").attr("style").indexOf("display: none;")).toEqual(0);

});