$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
});

$(function() {
    //Fade out the updated/created message after being displayed.
    if ($('.alert').length > 0) {
        $('.alert').delay(2000).fadeOut(1000);
    }


    $('#saveDetails').click(function(event) {
        $('#action').val('save');
       var hasErrors = 0;
        hasErrors = checkform();
        if (hasErrors == 0) {
            $("#configuration").submit();
        }

    });

    $('#next').click(function(event) {
        $('#action').val('next');
        var hasErrors = 0;
        hasErrors = checkform();
        if (hasErrors == 0) {
            $("#configuration").submit();
        }
    });

});

function checkform() {
    var errorFound = 0;
    
    //Check to make sure an organization is selected
    if($('#organization').val() === '') {
        $('#orgDiv').addClass("has-error");
        $('#configOrgMsg').addClass("has-error");
        $('#configOrgMsg').html('The organization is a required field.');
        errorFound = 1;
    }
    
    //Check to make sure a message type is selected
    if($('#messageTypeId').val() === '') {
        $('#messageTypeDiv').addClass("has-error");
        $('#configMessageTypeMsg').addClass("has-error");
        $('#configMessageTypeMsg').html('The authorized user is a required field.');
        errorFound = 1;
    }
    
    //Check to make sure a configuration name is entered
    if($('#configName').val() === '') {
        $('#configNameDiv').addClass("has-error");
        $('#configNameMsg').addClass("has-error");
        $('#configNameMsg').html('The configuration name is a required field.');
        errorFound = 1;
    }
    
    
    return errorFound;
    
}


