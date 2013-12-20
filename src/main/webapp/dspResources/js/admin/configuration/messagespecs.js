$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
});

$(function() {
    //Fade out the updated/created message after being displayed.
    if ($('.alert').length > 0) {
        $('.alert').delay(2000).fadeOut(1000);
    }
    
    //This function will save the messgae type field mappings
    $('#saveDetails').click(function() {
        $('#action').val('save');
        
        //Need to make sure all required fields are marked if empty.
        var hasErrors = 0;
        hasErrors = checkFormFields();

        if (hasErrors == 0) { 
            $('#messageSpecs').submit();
        }
    });

    $('#next').click(function(event) {
        $('#action').val('next');

        var hasErrors = 0;
        hasErrors = checkFormFields();

        if (hasErrors == 0) {
            $('#messageSpecs').submit();
        }
    });

});

function checkFormFields() {
    var hasErrors = 0;
    
    return hasErrors;
}


